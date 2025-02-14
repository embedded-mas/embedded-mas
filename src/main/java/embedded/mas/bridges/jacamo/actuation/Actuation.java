package embedded.mas.bridges.jacamo.actuation;

import java.util.List;
import java.util.ArrayList;

import jason.asSyntax.Atom;

public class Actuation extends DefaultActuation {


	public Actuation(Atom id) {
		super(id);
	}

	public Actuation(Atom id, List<Atom> parameters) {
		super(id, parameters);
	}

	/*public void addParameter(Atom parameter) {
		this.getParameters().add(parameter);
	}*/

	public void removeParameter(Atom parameter) {
		this.getParameters().remove(parameter);
	}

	@Override
	public String toString() {
		String result = this.getId() + "(";
		if(this.getParameters()!=null) {
			for(Object parameter:this.getParameters())
				result = result.concat(parameter + ", ");
			if(getParameters().size()>0)
				result = result.substring(0, result.length()-2);
		}
		result = result + ")";


		return result;
	}
}
