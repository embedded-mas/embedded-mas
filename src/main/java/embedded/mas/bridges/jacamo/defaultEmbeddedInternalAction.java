package embedded.mas.bridges.jacamo;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;



public class defaultEmbeddedInternalAction extends EmbeddedInternalAction {

	@Override
	/**
	 * args:
	 * 0. DeviceName
	 * 1. ActionName
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

			 
			if(args[2] instanceof ListTermImpl){ //if arguments in args[2] are a list 
				String[] arguments = new String[((ListTermImpl)args[2]).size()];			
				for(int i=0;i<((ListTermImpl)args[2]).size();i++) {
					arguments[i] = ((ListTermImpl)args[2]).get(i).toString().replaceAll("\"(.+)\"", "$1");
				}
				device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), arguments);
			}
			else { //default condition
				device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), new String[]{args[2].toString().replaceAll("\"(.+)\"", "$1")});
			}


			return true;
		}else return false;
	}
}
