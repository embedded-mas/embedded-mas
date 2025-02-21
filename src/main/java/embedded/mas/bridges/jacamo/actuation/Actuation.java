package embedded.mas.bridges.jacamo.actuation;

import java.util.List;
import java.util.ArrayList;

import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class Actuation extends DefaultActuation<List<Atom>> {




	public Actuation(Atom id) {
		this(id, new ArrayList<Atom>());
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
			for(Object parameter:this.getParameters()) {
				result = result.concat(parameter.toString());
				if(this.getDefaultParameterValues()!=null) 
					if(this.getDefaultParameterValues().get(parameter.toString())!=null)
						result = result + "/" + this.getDefaultParameterValues().get(parameter.toString());								 
				result = result.concat( ", ");
				if(this.getDefaultParameterValues()!=null && this.getDefaultParameterValues().get(parameter)!=null) {
					result = result + this.getDefaultParameterValues().get(parameter);

				}
			}
			if(getParameters().size()>0)
				result = result.substring(0, result.length()-2);
		}
		result = result + ")";


		return result;
	}

	@Override
	public int parameterSize() {
		return this.getParameters().size();
	}

	@Override
	public DefaultActuation<List<Atom>> clone() {
		return new Actuation(this.getId(), this.getParameters());
	}

	@Override
	public Term[] getParametersAsArray() {
		Term[] params = new Term[getParameters().size()];
		int i = 0;
		for(Atom a : getParameters()) {			
			if(getDefaultParameterValues()==null || getDefaultParameterValues().get(a.toString())==null) {
				params[i++] = null;				
			}
			else {
				Object defaultValue = getDefaultParameterValues().get(a.toString());
				if (defaultValue instanceof Integer)
					params[i++] = new NumberTermImpl(defaultValue.toString());
				else
				   params[i++] = (Term) defaultValue;
			}
		}
		return params;

	}

}
