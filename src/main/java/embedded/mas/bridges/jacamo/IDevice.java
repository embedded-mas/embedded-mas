package embedded.mas.bridges.jacamo;

import java.util.Collection;
import jason.asSyntax.Literal;

public interface ISensor {
	
	
	
	/* Returns a collection of percepts from the sensor */
	public Collection<Literal> getPercepts();

}