package embedded.mas.bridges.ros;

import com.fasterxml.jackson.databind.JsonNode;
import static embedded.mas.bridges.jacamo.Utils.jsonToPredArguments;

import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.LiteralDevice;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;

import static jason.asSyntax.ASSyntax.parseLiteral;
import static jason.asSyntax.ASSyntax.createAtom;

public class RosMaster extends LiteralDevice {


	public RosMaster(Atom id, IRosInterface microcontroller) {
		super(id, microcontroller);
	}

	protected final boolean serviceRequest(String serviceName, ServiceParameters parameters) {
		if(parameters==null)
			return ((DefaultRos4EmbeddedMas) microcontroller).serviceRequest(serviceName, null);	 
		else
			return ((DefaultRos4EmbeddedMas) microcontroller).serviceRequest(serviceName, parameters.toJson()); 
	}

	protected Literal serviceRequestResponse(String serviceName, ServiceParameters parameters) {
		try {
			if(parameters==null)
				return parseLiteral(jsonToPredArguments(((DefaultRos4EmbeddedMas) microcontroller).serviceRequestResponse(serviceName, null).get("values")));			
			else
				return parseLiteral(jsonToPredArguments(((DefaultRos4EmbeddedMas) microcontroller).serviceRequestResponse(serviceName, parameters.toJson())));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (TokenMgrError e) {
			e.printStackTrace();
		}
		return null;
	}

	protected final boolean blockingServiceRequest(String serviceName, ServiceParameters parameters) {
		JsonNode serviceReply = ((DefaultRos4EmbeddedMas) microcontroller).serviceRequestResponse(serviceName,parameters.toJson());		
		try {
			return serviceReply.get("result").asBoolean(); //return true whether the request is successful
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un) {
		EmbeddedAction action = this.embeddedActions.get(createAtom(actionName));
		if(action!=null)
			if(action instanceof TopicWritingAction) {
				((TopicWritingAction)action).setValue(args[0]);
				this.getMicrocontroller().execEmbeddedAction(action);
			}	
			else
				if(action instanceof ServiceRequestAction) {
					for(int i=0;i<args.length;i++) { //set service params
						//TODO: implement service response handling
						((ServiceRequestAction)action).getServiceParameters().get(i).setParamValue(args[i]);						
					}					
					this.getMicrocontroller().execEmbeddedAction(action);
				}
		return true;	
	}

	@Override
	public boolean execEmbeddedAction(Atom actionName,Object[] args) {
		EmbeddedAction action = this.embeddedActions.get(actionName);
		if(action!=null)
			if(action instanceof TopicWritingAction) {
				((TopicWritingAction)action).setValue(args[0]);
				this.getMicrocontroller().execEmbeddedAction(action);
			}	
			else
				if(action instanceof ServiceRequestAction) {
					for(int i=0;i<args.length;i++) { //set service params
						((ServiceRequestAction)action).getServiceParameters().get(i).setParamValue(args[i]);						
					}					
					this.getMicrocontroller().execEmbeddedAction(action);
				}
		return true;
	}
	
	
	
}
