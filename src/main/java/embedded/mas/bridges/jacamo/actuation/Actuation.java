package embedded.mas.bridges.jacamo.actuation;

import java.util.List;
import java.util.ArrayList;

import jason.asSyntax.Atom;

public class Actuation {
	
	private Atom id;
	private List<Atom> parameters;

	public Actuation(Atom id) {
		this(id, new ArrayList<Atom>());
	}

	public Actuation(Atom id, List<Atom> parameters) {
		super();
		this.id = id;
		this.parameters = parameters;
	}

	public Atom getId() {
		return id;
	}
	

	public List<Atom> getParameters() {
		return parameters;
	}

	public void setParameters(List<Atom> parameters) {
		this.parameters = parameters;
	}


	public void addParameter(Atom parameter) {
		this.parameters.add(parameter);
	}

	public void removeParameter(Atom parameter) {
		this.parameters.remove(parameter);
	}

	@Override
	public String toString() {
		String result = this.id + "(";
		for(Atom parameter:parameters)
			result = result.concat(parameter + ", ");
		if(parameters.size()>0)
		   result = result.substring(0, result.length()-2);
		result = result + ")";


		return result;
	}
}
