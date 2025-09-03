package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
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
	
	public boolean hasActuator(Atom actuatorId);
	
	/**
	 * 
	 * @param actuations: Set of actuations to be executed (implemented through ArrayList to be faster than Set implementations)
	 * @param un
	 * @return true whether the execution is successful, false otherwise
	 */
	public boolean execActuationSet(ArrayList<ActuationDevice> actuations, Unifier un);
	
	public boolean doExecActuation(ActuationDevice actuation, Unifier un);

}
