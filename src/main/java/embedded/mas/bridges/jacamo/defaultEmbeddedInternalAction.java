package embedded.mas.bridges.jacamo;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;



public class defaultEmbeddedInternalAction extends EmbeddedInternalAction {

	@Override
	/**
	 * args:
	 * 0. DeviceName
	 * 1. ActionName
	 * 2. Parameters
	 */
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {  
		if(ts.getAg() instanceof EmbeddedAgent) {

			//find the device the action is applyied upon
			DefaultDevice device = null;
			String deviceName = args[0].toString().replaceAll("\"(.+)\"", "$1");
			for(DefaultDevice dev:((EmbeddedAgent)ts.getAg()).getDevices()) {
				if(dev.getId().toString().equals(deviceName)) {
					device = dev;
					break;
				}
			}					
			if(device==null) throw new Exception("Device " + deviceName + " not found.");

			EmbeddedAction action = device.getEmbeddedAction(createAtom(args[1].toString().replaceAll("\"(.+)\"", "$1")));	
			Atom actionName = createAtom(args[1].toString().replaceAll("\"(.+)\"", "$1"));			
			if(action!=null) { 	//Case 1. The device has an EmbeddedAction
				if(args[2] instanceof ListTermImpl){ //if arguments in args[2] are a list 
					Object[] arguments = new Object[((ListTermImpl)args[2]).size()];			
					for(int i=0;i<((ListTermImpl)args[2]).size();i++) {
						if(((ListTermImpl)args[2]).get(i) instanceof NumberTermImpl)
							arguments[i] = ((ListTermImpl)args[2]).get(i);
						else
						   arguments[i] = ((ListTermImpl)args[2]).get(i).toString().replaceAll("\"(.+)\"", "$1");
					}

					//Check whether the current device class is adapted to execute embedded actions. 
					//New kinds of devices must be adapted here to execute embedded actions
					if(SerialDevice.class.isAssignableFrom(device.getClass())||
							LiteralDevice.class.isAssignableFrom(device.getClass())) {
						return device.execEmbeddedAction(actionName,arguments,un);
					}else throw new Exception("Embedded action " + actionName + "not available in " + deviceName);
				}
				else return device.execEmbeddedAction(actionName, new Object[] {args[2]},un);
			}


			else {//Case 2. The action is implemented as java code in the device (old style)				
				if(args[2] instanceof ListTermImpl){ //if arguments in args[2] are a list 
					Term[] arguments = new Term[((ListTermImpl)args[2]).size()];			
					for(int i=0;i<((ListTermImpl)args[2]).size();i++) {
						arguments[i] = adaptTerm(((ListTermImpl)args[2]).get(i));
					}
					return device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), arguments,un);
				}
				else { //default condition
					return device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), new String[]{args[2].toString().replaceAll("\"(.+)\"", "$1")},un);
				}

			}


			
		}else return false;
	}
	
	private Term adaptTerm(Term t) {
		if(t.toString().matches("\"(.+)\"")) 
			return new StringTermImpl(t.toString().replaceAll("\"(.+)\"", "$1")); 
		return t;
		
	}
}
