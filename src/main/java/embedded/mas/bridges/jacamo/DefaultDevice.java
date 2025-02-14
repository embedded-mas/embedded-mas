package embedded.mas.bridges.jacamo;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import embedded.mas.bridges.jacamo.actuation.Actuator;
import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import embedded.mas.exception.EmbeddedActionException;
import embedded.mas.exception.EmbeddedActionNotFoundException;
import embedded.mas.exception.InvalidActuationException;
import embedded.mas.exception.InvalidActuatorException;
import embedded.mas.exception.PerceivingException;

import jason.asSemantics.Unifier;
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
	private HashMap<Atom, EmbeddedAction> embeddedActions = new HashMap<Atom, EmbeddedAction>();
	private HashSet<Actuator> actuators = new HashSet<Actuator>();

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

	
	@Deprecated
	public HashMap<Atom, EmbeddedAction> getEmbeddedActions(){
		return this.embeddedActions;
	}
	
	
	@Deprecated
	public void addEmbeddedAction(EmbeddedAction embeddedAction) {
		this.embeddedActions.put(embeddedAction.getActionName(), embeddedAction);

	}

	@Deprecated
	public void removeEmbeddedAction(EmbeddedAction embeddedAction) {
		this.embeddedActions.remove(embeddedAction.getActionName());

	}

	@Deprecated
	public final EmbeddedAction getEmbeddedAction(Atom actionName) {
		return embeddedActions.get(actionName);
	}

	
	
	public HashSet<Actuator> getActuators() {
		return actuators;
	}

	public void addActuator(Actuator actuator) {
		this.actuators.add(actuator);
	}
	
	public void removeActuator(Actuator actuator) {
		this.actuators.remove(actuator);
	}
	
	/**
	 * Returns true if the device contains an actuator identified by the parameter actuatorId
	 * @param actuatorId
	 * @return
	 */
	public boolean hasActuator(Atom actuatorId) {
		Iterator<Actuator> actuatorIt = this.actuators.iterator();
		if(!actuatorIt.hasNext()) return false;
		while(actuatorIt.hasNext())
			if(actuatorIt.next().getId()==actuatorId)
				return true;
		return false;
		//return this.actuators.contains(actuatorId);
	}
	
	/**
	 * Returns the actuator identified by the parameter actuatorId.
	 * Returns null if the defice does not have such actuator
	 */
	public Actuator getActuatorById(Atom actuatorId) {
		Actuator result = null;
		Iterator<Actuator> actuatorIt = this.getActuators().iterator();
		while(actuatorIt.hasNext()) {
			Actuator currentActuator = actuatorIt.next();
			if(currentActuator.getId().equals(actuatorId))
				return currentActuator;
		}
		
		return result;
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
	public final boolean execEmbeddedAction(String actionName, Object[] args) throws EmbeddedActionNotFoundException,EmbeddedActionException{
		return execEmbeddedAction(actionName, args, null);
	}

	public abstract boolean execEmbeddedAction(String actionName, Object[] args, Unifier un) throws EmbeddedActionNotFoundException,EmbeddedActionException;

    @Override
	public boolean execEmbeddedAction(Atom actionName, Object[] args, Unifier un) {		
		return false;
	}
	
	
    @Override
	public final boolean execActuation(Atom actuatorId, Atom actuationId, Object[] args, Unifier un) throws InvalidActuatorException, InvalidActuationException {
		Actuator actuator = this.getActuatorById(actuatorId);
		if(actuator==null) throw new InvalidActuatorException("Actuator " + this.getId().toString() +"." +actuatorId.toString() + " not found.");
		
		DefaultActuation actuation = actuator.getActuationById(actuationId);
		if(actuation==null) throw new InvalidActuationException("Actuation " + this.getId().toString() +"." +actuatorId.toString()+"." + actuationId.toString() + " not found.");
		
		
		return doExecActuation(actuatorId, actuationId, args, un);
	}
    
    
    /**
     * Implements the actual execution of the actuation actuationId in the actuator actuatorId.
     * @param actuatorId
     * @param actuationId
     * @param args
     * @return
     */
    protected abstract boolean doExecActuation(Atom actuatorId, Atom actuationId, Object[] args, Unifier un);
	

}
