package embedded.mas.bridges.jacamo.actuation;

import java.util.HashSet;
import java.util.Iterator;

import jason.asSyntax.Atom;

public class Actuator {

	private Atom id;
	private HashSet<Actuation> actuations = new HashSet<Actuation>();
	
	public Actuator(Atom id) {
		super();
		this.id = id;
	}

	public HashSet<Actuation> getActuations() {
		return actuations;
	}

	public void setActuations(HashSet<Actuation> actuations) {
		this.actuations = actuations;
	}

	public Atom getId() {
		return id;
	}
	
	public void addActuation(Actuation actuation) {
		this.actuations.add(actuation);
	}
	
	
	public void removeActuation(Actuation actuation) {
		this.actuations.remove(actuation);
	}
	
	/**
	 * Returns true if the actuator contains an actuation identified by the parameter actuationId
	 * @param actuatorId
	 * @return
	 */
	public boolean hasActuation(Atom actuationId) {
		Iterator<Actuation> actuationIt = this.actuations.iterator();
		if(!actuationIt.hasNext()) return false;
		while(actuationIt.hasNext())
			if(actuationIt.next().getId()==actuationId)
				return true;
		return false;
	}
	
	
	/**
	 * Returns the actuation identified by the parameter actuatorId.
	 * Returns null if the device does not have such actuation
	 */
	public Actuation getActuationById(Atom actuationId) {
		Actuation result = null;
		Iterator<Actuation> actuationIt = this.getActuations().iterator();
		while(actuationIt.hasNext()) {
			Actuation currentActuation = actuationIt.next();
			if(currentActuation.getId().equals(actuationId))
				return currentActuation;
		}
		
		return result;
	}

	@Override
	public String toString() {
		String actuationsStr = "";
		if(actuations.size()>0) {
		   for(Actuation actuation : actuations)
			   actuationsStr = actuationsStr.concat(actuation.getId().toString() + ", ");
		   actuationsStr = actuationsStr.substring(0, actuationsStr.length()-2);
		}
		return "[actuator id=" + id + " actuations={"+actuationsStr +"}]";
	}
	
	
}
