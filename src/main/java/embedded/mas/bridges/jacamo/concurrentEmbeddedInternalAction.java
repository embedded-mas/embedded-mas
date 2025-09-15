package embedded.mas.bridges.jacamo;

import static jason.asSyntax.ASSyntax.createAtom;

import embedded.mas.exception.EmbeddedActionException;
import embedded.mas.exception.EmbeddedActionNotFoundException;
import jason.asSemantics.ConcurrentInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

public class concurrentEmbeddedInternalAction extends ConcurrentInternalAction {
	
	
	private Object[] listToArguments(ListTermImpl args) {
		Object[] arguments = new Object[args.size()];			
		for(int i=0;i<args.size();i++) {
			if(args.get(i) instanceof ListTermImpl)
				arguments[i] = listToArguments((ListTermImpl) args.get(i));
			else
				if(args.get(i) instanceof NumberTermImpl)
					arguments[i] = args.get(i);
				else
					if(args.get(i)==Literal.LTrue)
						arguments[i] = Boolean.TRUE;
					else
						if(args.get(i)==Literal.LFalse)
							arguments[i] = Boolean.FALSE;
						else
							arguments[i] = args.get(i).toString().replaceAll("\"(.+)\"", "$1");
		}		
		return arguments;
	}

	@Override
	/**
	 * args:
	 * 0. DeviceName
	 * 1. ActionName
	 */
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {  
		int timeout = 5000;

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
					if(device==null) throw new RuntimeException("Device " + deviceName + " not found.");

					EmbeddedAction action = device.getEmbeddedAction(createAtom(args[1].toString().replaceAll("\"(.+)\"", "$1")));	
					Atom actionName = createAtom(args[1].toString().replaceAll("\"(.+)\"", "$1"));			
					if(action!=null) { 	//Case 1. The device has an EmbeddedAction
						if(args[2] instanceof ListTermImpl){ //if arguments in args[2] are a list 
							Object[] arguments = listToArguments((ListTermImpl)args[2]);

							//Check whether the current device class is adapted to execute embedded actions. 
							//New kinds of devices must be adapted here to execute embedded actions
							if(SerialDevice.class.isAssignableFrom(device.getClass())||
									LiteralDevice.class.isAssignableFrom(device.getClass())) {
								r =  device.execEmbeddedAction(actionName,arguments,un);
							}else throw new RuntimeException("Embedded action " + actionName + "not available in " + deviceName);
						}
						else r =  device.execEmbeddedAction(actionName, new Object[] {args[2]},un);
					}


					else {//Case 2. The action is implemented as java code in the device (old style)				
						if(args[2] instanceof ListTermImpl){ //if arguments in args[2] are a list 
							Term[] arguments = new Term[((ListTermImpl)args[2]).size()];			
							for(int i=0;i<((ListTermImpl)args[2]).size();i++) {
								arguments[i] = adaptTerm(((ListTermImpl)args[2]).get(i));
							}
							try {
								r =  device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), arguments,un);
							} catch (EmbeddedActionNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (EmbeddedActionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else { //default condition
							try {
								r =  device.execEmbeddedAction(args[1].toString().replaceAll("\"(.+)\"", "$1"), new String[]{args[2].toString().replaceAll("\"(.+)\"", "$1")},un);
							} catch (EmbeddedActionNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (EmbeddedActionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

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
		failInt(ts, intentionKey);

	}
	protected Term adaptTerm(Term t) {
		if(t.toString().matches("\"(.+)\"")) 
			return new StringTermImpl(t.toString().replaceAll("\"(.+)\"", "$1")); 
		return t;

	}

}
