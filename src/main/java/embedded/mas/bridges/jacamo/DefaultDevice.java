package embedded.mas.bridges.jacamo;

import java.util.Collection;

import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

/*
 * The base implementation of sensor between agent and sensor.
 * 
 * The method getPercepts is supposed to collect a list of values from the sensor converted to Literal. 
 * The connection to the sensor is supposed to be implemented in that method.
 * 
 * 
 */

public abstract class DefaultSensor implements ISensor {
	
	protected Atom id;



	public DefaultSensor(Atom id) {
		super();
		this.id = id;
	}


	/* Returns a collection of percepts from the sensor */
	public abstract Collection<Literal> getPercepts();


	public Atom getId() {
		return id;
	}

	public void setId(Atom id) {
		this.id = id;
	}


}
