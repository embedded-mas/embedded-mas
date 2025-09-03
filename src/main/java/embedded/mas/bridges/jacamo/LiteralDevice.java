/**
 * This class represents an interface with a microcontroller whose sensor data can be converted to literal.
 */

package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.exception.PerceivingException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public abstract class LiteralDevice extends DefaultDevice implements IDevice {

		
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
	public boolean execActuationSet(ArrayList<ActuationDevice> actuations, Unifier un) {
		boolean result = true;
		Iterator<ActuationDevice> it = actuations.iterator();
		while(it.hasNext()&&result==true) {
			ActuationDevice act = it.next();
 			this.doExecActuation(act, un);
		}
		return result;
	}



}
