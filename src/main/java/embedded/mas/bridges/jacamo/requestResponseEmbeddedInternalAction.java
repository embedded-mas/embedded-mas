package embedded.mas.bridges.jacamo;

import static jason.asSyntax.ASSyntax.createAtom;

import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;
import embedded.mas.bridges.ros.RosMaster;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

public class requestResponseEmbeddedInternalAction extends EmbeddedInternalAction {

	@Override
	/**
	 * args:
	 * 0. DeviceName
	 * 1. ActionName
	 * 2. Parameters
	 * 3. Return variable
	 */
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {  
		if(ts.getAg() instanceof EmbeddedAgent) {

			//find the device the action is applied upon
			DefaultDevice device = null;
			String deviceName = args[0].toString().replaceAll("\"(.+)\"", "$1");
			for(DefaultDevice dev:((EmbeddedAgent)ts.getAg()).getDevices()) {
				if(dev.getId().toString().equals(deviceName)) {
					device = dev;
					break;
				}
			}					
			if(device==null) throw new Exception("Device " + deviceName + " not found.");
			Atom actionName = createAtom(args[1].toString().replaceAll("\"(.+)\"", "$1"));	

			//Check whether the current device class is adapted to execute request-response embedded actions.
			//New kinds of devices must be adapted here to execute embedded actions
			if(!(device instanceof RosMaster))
				throw new Exception("Embedded request return action " + actionName + "not available in " + deviceName);

			EmbeddedAction action = device.getEmbeddedAction(createAtom(args[1].toString().replaceAll("\"(.+)\"", "$1")));	

			if(action!=null) { 	//Case 1. The device has an EmbeddedAction
				if(args[2] instanceof ListTermImpl){ //if arguments in args[2] are a list
					Object[] arguments  = null;
					if(((ListTermImpl)args[2]).size()>0) {

						arguments = new Object[((ListTermImpl)args[2]).size()];			
						for(int i=0;i<((ListTermImpl)args[2]).size();i++) {
							if(((ListTermImpl)args[2]).get(i) instanceof ListTermImpl) { //if the i-th parameter is a nested list, handle it as an array
								arguments[i] = ((ListTermImpl)(((ListTermImpl)args[2]).get(i))).toArray(); //turn the nested list in array
							}
							else
							if(((ListTermImpl)args[2]).get(i) instanceof NumberTermImpl)
								arguments[i] = ((ListTermImpl)args[2]).get(i);
							else
								arguments[i] = ((ListTermImpl)args[2]).get(i).toString().replaceAll("\"(.+)\"", "$1");
						}
					}
					if(device instanceof RosMaster) {
						return ((RosMaster)device).execEmbeddedAction(actionName.toString(), arguments, args[3], un);

					}
					//handling by other kinds of devices must be implemented here, as an else clause	
					else
						return false;
				}
				else {
					if(device instanceof RosMaster) 
						return ((RosMaster)device).execEmbeddedAction(actionName.toString(), new Object[] {args[2]}, args[3], un);
					else 
						return false;
					//handling by other kinds of devices must be implemented here, as an else clause

				}
			}


			else {//Case 2. The action is implemented as java code in the device (old style)
				//no device adapted to this case so far
				return false;
			}

		}else return false;

	}



}
