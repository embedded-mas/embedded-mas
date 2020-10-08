package embedded.mas.bridges.jacamo;

import java.io.IOException;

import embedded.mas.comm.serial.DefaultJava2Serial;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class DefaultSerialInternalAction extends DefaultInternalAction {

	/*
	 * Params:
	 * 0 - Serial port
	 * 1 - Baudrate
	 * 2 - Content to sendo to serial
	 */
	@Override
	public Object execute( TransitionSystem ts, Unifier un, Term[] args ) throws Exception {

		/*DefaultJava2Serial serial = new DefaultJava2Serial(args[0].toString(), Integer.parseInt(args[1].toString()));
		try {
			serial.send(args[2].toString());
			return true;
		} catch (Exception e) {
			return false;
		}*/
		System.out.println("Defaulg serial internal action " + args[0] + ", " + args[1] + ", " + args[2]);
		return true;

	}

}
