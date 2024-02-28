package embedded.mas.bridges.jacamo;

import java.util.Collection;

import embedded.mas.exception.InvalidActuationException;
import embedded.mas.exception.InvalidActuatorException;
import embedded.mas.exception.PerceivingException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public interface IDevice {
	
	
	
	/* Returns a collection of percepts from the sensor */
	public Collection<Literal> getPercepts() throws PerceivingException;
	
	public Atom getId();
	
	public void addEmbeddedAction(EmbeddedAction embeddedAction);
	
	public void removeEmbeddedAction(EmbeddedAction embeddedAction);
	
	public IEmbeddedAction getEmbeddedAction(Atom actionName);
	
	public boolean execEmbeddedAction(Atom actionName, Object[] args, Unifier un);
	
	public boolean execActuation(Atom actuatorId, Atom actuationId, Object[] args,  Unifier un) throws InvalidActuatorException, InvalidActuationException;
	
	public boolean hasActuator(Atom actuatorId);

}
