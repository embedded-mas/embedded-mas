package embedded.mas.bridges.jacamo.actuation;

import java.util.HashSet;

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
	
	
}
