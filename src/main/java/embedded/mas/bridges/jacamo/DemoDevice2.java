package embedded.mas.bridges.jacamo;

import java.util.Collection;

import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.exception.EmbeddedActionException;
import embedded.mas.exception.EmbeddedActionNotFoundException;
import embedded.mas.exception.PerceivingException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class DemoDevice2 extends DefaultDevice {

	public DemoDevice2(Atom id, SensorValueCollector collector, SensorValueTransformer transformer) {
		super(id, collector, transformer);
		// TODO Auto-generated constructor stub
	}

	public DemoDevice2(Atom id, IExternalInterface microcontroller) {
		super(id, microcontroller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Literal> getPercepts() throws PerceivingException {
		Collection collected = this.collector.collect();
		if(collected==null) return null;
		System.out.println(collected.getClass().getName());
		return this.transformer.transform(collected);
		
	}

	@Override
	public IExternalInterface getMicrocontroller() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un)
			throws EmbeddedActionNotFoundException, EmbeddedActionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean doExecSpecificActuation(ActuationDevice actuation, Unifier un) {
		// TODO Auto-generated method stub
		return false;
	}

}
