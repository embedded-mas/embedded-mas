package embedded.mas.bridges.jacamo;

import java.util.Collection;
import java.util.HashMap;

import embedded.mas.bridges.ros.TopicWritingAction;
import embedded.mas.exception.EmbeddedActionException;
import embedded.mas.exception.EmbeddedActionNotFoundException;
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
	protected IExternalInterface microcontroller;
	protected HashMap<Atom, EmbeddedAction> embeddedActions = new HashMap<Atom, EmbeddedAction>();

	public DefaultDevice(Atom id, IExternalInterface microcontroller) {
		this.id = id;
		this.microcontroller = microcontroller;
	}


	/* Returns a collection of percepts from the sensor */
	public abstract Collection<Literal> getPercepts() throws PerceivingException;


	public Atom getId() {
		return id;
	}

	public void setId(Atom id) {
		this.id = id;
	}

	public abstract IExternalInterface getMicrocontroller();

	public void addEmbeddedAction(EmbeddedAction embeddedAction) {
		this.embeddedActions.put(embeddedAction.getActionName(), embeddedAction);

	}

	public void removeEmbeddedAction(EmbeddedAction embeddedAction) {
		this.embeddedActions.remove(embeddedAction.getActionName());

	}

	public final EmbeddedAction getEmbeddedAction(Atom actionName) {
		for(Atom k : embeddedActions.keySet()) {
		}
		
		return embeddedActions.get(actionName);
	}

	/**
	 * Each device is supposed to enabled a set of actions (to be imposed on the
	 * actuators). From this method, the device is supposed to properly trigger the
	 * action identified by the actionName. Returns true for success and false for
	 * fail
	 * 
	 * @param actionName
	 * @return
	 */
	public abstract boolean execEmbeddedAction(String actionName, Object[] args)
			throws EmbeddedActionNotFoundException, EmbeddedActionException;

	@Override
	public boolean execEmbeddedAction(Atom actionName) {		
		return false;
	}
	
	
	
	

}
