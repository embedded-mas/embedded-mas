/**
 * This class represents an interface with a microcontroller whose sensor data can be converted to literal.
 */

package embedded.mas.bridges.jacamo;

import java.util.Collection;
import java.util.List;

import embedded.mas.exception.PerceivingException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class LiteralDevice extends DefaultDevice implements IDevice {

		
	public LiteralDevice(Atom id, ILiteralListInterface microcontroller) {
		super(id, microcontroller);	
	}

	@Override
	public Collection<Literal> getPercepts() throws PerceivingException {
		List<Literal> beliefs = microcontroller.read();
		return beliefs;
	}

	@Override
	public boolean execEmbeddedAction(String topic,  Object[] args, Unifier un) {
		return false;
	}

	@Override
	public ILiteralListInterface getMicrocontroller() {
		return (ILiteralListInterface) this.microcontroller;
	}

	@Override
	public boolean doExecActuation(Atom actuatorId, Atom actuationId, Object[] args, Unifier un) {
		// TODO Auto-generated method stub
		return false;
	}
	


}
