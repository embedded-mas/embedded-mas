package embedded.mas.bridges.jacamo.actuation;



import java.util.List;
import java.util.ArrayList;

import jason.asSyntax.Atom;

public abstract class DefaultActuation {
	
	private Atom id;
	private List<Atom> parameters;

	public DefaultActuation(Atom id) {
		this(id, null);
	}

	public DefaultActuation(Atom id, List<Atom> parameters) {
		this.id = id;
		this.parameters = parameters;
	}

	public Atom getId() {
		return id;
	}

	public List<Atom> getParameters() {
		return parameters;
	}

	public void addParameter(Atom parameter) {
		if(this.parameters==null)
			parameters = new ArrayList<Atom>();
		this.parameters.add(parameter);
	}

	@Override
	public String toString() {
		return "DefaultActuation [id=" + id + ", parameters=" + parameters + "]";
	}


	

}
