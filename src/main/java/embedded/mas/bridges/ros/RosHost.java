/**
 * This class just adds some clarity for the jason-ROS machinery. The core functions are in the RosMaster.
 */

package embedded.mas.bridges.ros;

import java.util.Collection;

import embedded.mas.exception.PerceivingException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class RosHost extends RosMaster {

	public RosHost(Atom id, IRosInterface microcontroller) {
		super(id, microcontroller);
	}


	/**
	 * 
	 * Just to provide an illustrative name for the execEmbeddedAction methor from the superclass
	 */
	public boolean act(Atom actionName, Object[] args, Unifier un) {
		return super.execEmbeddedAction(actionName, args, un);
	}

	@Override
	public Collection<Literal> getPercepts() throws PerceivingException {
		return super.getPercepts();
	}

	
	
	
}
