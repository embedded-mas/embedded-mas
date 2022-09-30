package embedded.mas.bridges.ros;

import com.fasterxml.jackson.databind.JsonNode;

import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.LiteralDevice;
import jason.asSyntax.Atom;
import static jason.asSyntax.ASSyntax.createAtom;

public class RosMaster extends LiteralDevice {

	/*public RosMaster(Atom id, DefaultRos4EmbeddedMas microcontroller) {
		super(id, microcontroller);
	}*/

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
		/*System.out.println("[RosMaster] action " + actionName + " size: " + this.embeddedActions.size());
		for(Atom keys : this.embeddedActions.keySet()) {
			System.out.println("[RosMaster] ->> " + keys.toString() + " - " + this.embeddedActions.get(keys).getActionName());
			
		}*/
		EmbeddedAction action = this.embeddedActions.get(createAtom(actionName));
		//System.out.println("[RosMaster] action " + action.getActionName().toString() + " - " + action.getClass().getName());
		if(action!=null)
			if(action instanceof TopicWritingAction) {
				//System.out.println("[RosMaster] vai executar action " + action.getActionName().toString() + " - " + action.getClass().getName());
				((TopicWritingAction)action).setValue(args[0]);
				this.getMicrocontroller().execEmbeddedAction(action);
			}	
			else
				if(action instanceof ServiceRequestAction) {
					for(int i=0;i<args.length;i++) { //set service params
						((ServiceRequestAction)action).getServiceParameters().get(i).setParamValue(args[i]);
						this.getMicrocontroller().execEmbeddedAction(action);
					}					
				}
	return true;	
	}
}
