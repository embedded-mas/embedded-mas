package embedded.mas.bridges.ros;
import jason.asSyntax.ASSyntax;

import com.fasterxml.jackson.databind.JsonNode;
import static embedded.mas.bridges.jacamo.Utils.jsonToPredArguments;

import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.LiteralDevice;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
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
				return parseLiteral(jsonToPredArguments(((DefaultRos4EmbeddedMas) microcontroller).serviceRequestResponse(serviceName, parameters.toJson()).get("values")));
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
		/*EmbeddedAction action = this.embeddedActions.get(createAtom(actionName));

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
		return true;*/
		return false;
	}



	public boolean execEmbeddedAction(
			String actionName,
			Object[] args,
			Term returnArg,
			Unifier un) throws Exception {

		EmbeddedAction action;
		IRosInterface rosInterface;

		action = this.getEmbeddedActions().get(
				createAtom(actionName)
		);

		if (action == null) {
			throw new Exception(
					"Action "
					+ actionName
					+ " was not found."
			);
		}

		rosInterface =
				(IRosInterface) this.getMicrocontroller();

		if (action instanceof ServiceRequestAction) {
			Literal response;
			ServiceRequestAction serviceAction;

			serviceAction =
					(ServiceRequestAction) action;

			if (args == null) {
				response = this.serviceRequestResponse(
						serviceAction.getServiceName(),
						null
				);
			} else {
				for (int index = 0;
						index < args.length;
						index++) {

					serviceAction
							.getServiceParameters()
							.get(index)
							.setParamValue(args[index]);
				}

				response = this.serviceRequestResponse(
						serviceAction.getServiceName(),
						serviceAction.getServiceParameters()
				);
			}

			return un.unifies(
					response,
					returnArg
			);
		}

		if (action instanceof RosActionClientAction) {
			String goalId;

			goalId = rosInterface.sendActionGoal(
					(RosActionClientAction) action,
					args
			);

			return un.unifies(
					returnArg,
					ASSyntax.createString(goalId)
			);
		}

		throw new Exception(
				"Unsupported action type: "
				+ action.getClass().getName()
		);
	}


	@Override
	public boolean execEmbeddedAction(
			Atom actionName,
			Object[] args,
			Unifier un) {

		EmbeddedAction action;
		IRosInterface rosInterface;

		action = this.getEmbeddedActions().get(
				actionName
		);

		if (action == null) {
			return false;
		}

		rosInterface =
				(IRosInterface) this.getMicrocontroller();

		if (action instanceof TopicWritingAction) {
			TopicWritingAction topicAction;

			topicAction =
					(TopicWritingAction) action;

			topicAction.setParamValues(args);

			this.getMicrocontroller()
					.execEmbeddedAction(topicAction);

			return true;
		}

		if (action instanceof ServiceRequestAction) {
			ServiceRequestAction serviceAction;

			serviceAction =
					(ServiceRequestAction) action;

			serviceAction.setParamValues(args);

			this.getMicrocontroller()
					.execEmbeddedAction(serviceAction);

			return true;
		}

		if (action instanceof RosActionCancelAction) {
			String goalId;

			if (args == null) {
				return false;
			}

			if (args.length != 1) {
				return false;
			}

			goalId = args[0].toString();

			goalId = goalId.replaceAll(
					"^\"|\"$",
					""
			);

			return rosInterface.cancelActionGoal(
					(RosActionCancelAction) action,
					goalId
			);
		}

		return false;
	}


	/**
	 * Check whether the arguments are consistent.
	 */
	private boolean checkArrayArguments(Object[] args) {
		for(int i=0;i<args.length;i++) {
			if(args[i].getClass().isArray()&&((Object[])args[i]).length>1) {
				Object vClass =  ((Object[])args[i])[0].getClass(); //check the class of the 1st element of the array
				for(int j=1;j<((Object[])args[i]).length;j++) { //check whether the remainder elements are of different class
					if( ((Object[])args[i])[j].getClass()!=vClass)
						return false;
				}
			}
		}
		return true;
	}


}
