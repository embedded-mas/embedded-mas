package embedded.mas.bridges.jacamo;

import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class default_serial_internal_action extends DefaultInternalAction {

	private Arduino4EmbeddedMas serial = null;

	/*
	 * Params:
	 * 0 - Serial port
	 * 1 - Baudrate
	 * 2 - Content to send to serial
	 */
	@Override
	public Object execute( TransitionSystem ts, Unifier un, Term[] args ) throws Exception {
		String port = args[0].toString().replaceAll("^\"+|\"+$", "");
		//System.out.println("[default_serial_internal_action] sending to serial : " + port + args[1].toString() + args[2].toString());
		try {
			if(serial==null) {
				serial = new Arduino4EmbeddedMas(port, Integer.parseInt(args[1].toString()));
				serial.openConnection();
			}
			serial.write(args[2].toString());
			return true;
		} catch (Exception e) {
			return false;
		}		

	}

}
