/**
 * This class implements a serial device with a customized execution of actuation sets (by the method doExecActuationSet).
 * In the default implementation (in DefaultDevice class), the actuation instructions are sent to the hardware one at a time.
 * The customized implementation provided by this class packages all the actuations in a single instruction, to send all the actuation instructions 
 * of a set to the hardware at the same time. It has proven faster.
 * 
 * On the hardware side, the controller software must implement a protocol where the actuation instructions have the form:
 * actuator.actuation1;...;actuation.actuation_n;__eom__
 * 
 * where __eom__ indicates the end of the instruction.
 * 
 * e.g.: motor_front_left.forward;motor_front_right.forward;motor_rear_left.forward;motor_rear_right.forward;__eom__
 * 
 * 
 * IMPORTANT: this requires to handle the default implementations of any device (e.g. wait), 
 * which are, by default, handled in the method doExecActuation of DefaultDevice 
 * 
 */

package embedded.mas.bridges.jacamo;

import java.util.ArrayList;

import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;

public class SerialDevice4EmbeddedProtocol extends SerialDevice {

	public SerialDevice4EmbeddedProtocol(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);
	}



	@Override
	public boolean execActuationSet(ArrayList<ActuationDevice> actuations, Unifier un) {	
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < actuations.size(); i++) {
			ActuationDevice act = actuations.get(i);
			if(act.getActuation().getId().toString().equals("wait")) {
				doWait((long) ((NumberTermImpl)act.getActuation().getParametersAsArray()[0]).solve());
			}
			else {
				s.append(act.getActuator().getId())
				.append(".")
				.append(act.getActuation().getId());

				if (i < actuations.size() - 1) {
					s.append(";");
				}
			}
		}
		return this.getMicrocontroller().write(s.toString());
	}

}
