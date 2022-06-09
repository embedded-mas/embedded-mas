package embedded.mas.bridges.jacamo;

import embedded.mas.exception.EmbeddedActionException;
import embedded.mas.exception.EmbeddedActionNotFoundException;
import jason.asSemantics.ConcurrentInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

public class concurrentEmbeddedInternalAction extends ConcurrentInternalAction {

	@Override
	/**
	 * args:
	 * 0. DeviceName
	 * 1. ActionName
	 * 2. Timeout (optional) - default 1000 milliseconds
	 */
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {  
		int timeout;
		if(args.length==2)
			timeout = Integer.parseInt(args[2].toString());
		else
			timeout = 1000;

		// suspend the intention by a given timeout
		final String key = suspendInt(ts, "embeddedAction", timeout); 

		startInternalAction(ts, new Runnable() { // to not block the agent thread, start a thread that performs the task and resume the intention latter
			public void run() {
				boolean r = false;
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
					//if(device==null) { throw new Exception("Device " + deviceName + " not found."); }
					

					try {
						if(args[2] instanceof ListTermImpl){ //if arguments in args[2] are a list 
							String[] arguments = new String[((ListTermImpl)args[2]).size()];			
							for(int i=0;i<((ListTermImpl)args[2]).size();i++) {
								arguments[i] = ((ListTermImpl)args[2]).get(i).toString().replaceAll("\"(.+)\"", "$1");
							}
							r = device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), arguments);
						}
						else { //default condition
							r = device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), new String[]{args[2].toString().replaceAll("\"(.+)\"", "$1")});
						}
					} catch (EmbeddedActionNotFoundException e) {
						e.printStackTrace();
					} catch (EmbeddedActionException e) {
						e.printStackTrace();
					}



				}


				if (r==true)
					resumeInt(ts, key); // resume the intention with success
				else
					failInt(ts, key); // resume the intention with fail
			}
		});

		return true;
	}

	@Override
	public void timeout(TransitionSystem ts, String intentionKey) {


	}

}
