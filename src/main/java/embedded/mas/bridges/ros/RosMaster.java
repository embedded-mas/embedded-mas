package embedded.mas.bridges.ros;

import com.fasterxml.jackson.databind.JsonNode;

import embedded.mas.bridges.jacamo.LiteralDevice;
import jason.asSyntax.Atom;
import static jason.asSyntax.ASSyntax.createAtom;

public class RosMaster extends LiteralDevice {


	public RosMaster(Atom id, IRosInterface microcontroller) {
		super(id, microcontroller);
	}

	protected final boolean serviceRequest(String serviceName, ServiceParameters parameters) {
		return ((DefaultRos4EmbeddedMas) microcontroller).serviceRequest(serviceName, parameters.toJson()); 
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
	public boolean execEmbeddedAction(String actionName, Object[] args) {
	/*	EmbeddedAction action = this.embeddedActions.get(createAtom(actionName));
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
		return true;*/
		return false;
	}

	@Override
	public boolean execEmbeddedAction(Atom actionName,Object[] args) {
		/*EmbeddedAction action = this.embeddedActions.get(actionName);
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
		return true;*/
		return false;
	}
	
	
	
}
