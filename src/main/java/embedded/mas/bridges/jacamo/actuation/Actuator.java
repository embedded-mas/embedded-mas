package embedded.mas.bridges.jacamo.actuation;

import java.util.HashSet;
import java.util.Iterator;

import jason.asSyntax.Atom;

public class Actuator {

	private Atom id;
	private HashSet<DefaultActuation> actuations = new HashSet<DefaultActuation>();
	
	public Actuator(Atom id) {
		super();
		this.id = id;
	}

	public HashSet<DefaultActuation> getActuations() {
		return actuations;
	}

	public void setActuations(HashSet<DefaultActuation> actuations) {
		this.actuations = actuations;
	}

	public Atom getId() {
		return id;
	}
	
	public void addActuation(DefaultActuation actuation) {
		this.actuations.add(actuation);
	}
	
	
	public void removeActuation(DefaultActuation actuation) {
		this.actuations.remove(actuation);
	}
	
	/**
	 * Returns true if the actuator contains an actuation identified by the parameter actuationId
	 * @param actuatorId
	 * @return
	 */
	public boolean hasActuation(Atom actuationId) {
		Iterator<DefaultActuation> actuationIt = this.actuations.iterator();
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
	public DefaultActuation getActuationById(Atom actuationId) {
		Actuation result = null;
		Iterator<DefaultActuation> actuationIt = this.getActuations().iterator();
		while(actuationIt.hasNext()) {
			DefaultActuation currentActuation = actuationIt.next();
			if(currentActuation.getId().equals(actuationId))
				return currentActuation;
		}
		
		return result;
	}

	@Override
	public String toString() {
		String actuationsStr = "";
		if(actuations.size()>0) {
		   for(DefaultActuation actuation : actuations)
			   actuationsStr = actuationsStr.concat(actuation.getId().toString() + ", ");
		   actuationsStr = actuationsStr.substring(0, actuationsStr.length()-2);
		}
		return "[actuator id=" + id + " actuations={"+actuationsStr +"}]";
	}
	
	
}
