package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringJoiner;

import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.exception.EmbeddedActionException;
import embedded.mas.exception.EmbeddedActionNotFoundException;
import embedded.mas.exception.PerceivingException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class SerialDevice extends DefaultDevice {
	
	private HashMap<Literal, Literal> beliefMap = new HashMap<Literal, Literal>();

	public SerialDevice(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);
	}

	@Override
	public Collection<Literal> getPercepts() throws PerceivingException {
		return null;
	}


	@Override
	public IPhysicalInterface getMicrocontroller() {
		return (IPhysicalInterface) this.microcontroller;
	}

	@Override
	public boolean execEmbeddedAction(Atom actionName, Object[] args, Unifier un) {
		EmbeddedAction action = getEmbeddedAction(actionName);		
		String actuation = ((SerialEmbeddedAction)action).getActuationName().toString();
		if(args.length>0){
		   StringJoiner joiner = new StringJoiner(",");
  		   for(int i=0;i<args.length;i++)
  		      joiner.add(args[i].toString());
  		   actuation = actuation + "(" + joiner + ")";
  		}
		if(action instanceof SerialEmbeddedAction) {
			return this.getMicrocontroller().write(actuation);
		}
		return false;
	}

	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un)
			throws EmbeddedActionNotFoundException, EmbeddedActionException {
		return false;
	}
	
	
	public void addBeliefCustomizator(Literal functorOrigin, Literal functorTarget) {
		this.beliefMap.put(functorOrigin, functorTarget);	
	}
	
	public Literal customizeBelief(Literal belief) {
		Literal bel = beliefMap.get(belief.getFunctor());
		if(bel==null) return belief;
		return bel;
	}


	@Override
	protected boolean doExecSpecificActuation(ActuationDevice actuation, Unifier un) {
		// TODO Auto-generated method stub
		return false;
	}
	
}



