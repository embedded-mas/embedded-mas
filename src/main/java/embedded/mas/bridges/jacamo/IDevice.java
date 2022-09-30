package embedded.mas.bridges.jacamo;

import java.util.Collection;

import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public interface IDevice {
	
	
	
	/* Returns a collection of percepts from the sensor */
	public Collection<Literal> getPercepts() throws PerceivingException;
	
	public Atom getId();
	
	public void addEmbeddedAction(EmbeddedAction embeddedAction);
	
	public void removeEmbeddedAction(EmbeddedAction embeddedAction);
	
	public IEmbeddedAction getEmbeddedAction(Atom actionName);
	
	public boolean execEmbeddedAction(Atom actionName);

}