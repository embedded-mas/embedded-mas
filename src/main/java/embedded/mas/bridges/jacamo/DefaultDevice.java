package embedded.mas.bridges.jacamo;

import java.util.Collection;

import embedded.mas.exception.PerceivingException;
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

public abstract class DefaultDevice implements IDevice {
	
	protected Atom id;
	protected IPhysicalInterface microcontroller;
	


	public DefaultDevice(Atom id, IPhysicalInterface microcontroller) {
		this.id = id;
		this.microcontroller = microcontroller;
	}


	/* Returns a collection of percepts from the sensor */
	public abstract Collection<Literal> getPercepts() throws PerceivingException;;


	public Atom getId() {
		return id;
	}

	public void setId(Atom id) {
		this.id = id;
	}
	
	/**
	 *  Each device is supposed to enabled a set of actions (to be imposed on the actuators).
	 *  From this method, the device is supposed to properly trigger the action identified by the actionName.
	 *  Returns true for success and false for fail
	 * 
	 * @param actionName
	 * @return
	 */
	public abstract boolean execEmbeddedAction(String actionName, Object[] args);


}
