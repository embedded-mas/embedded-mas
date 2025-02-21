package embedded.mas.bridges.jacamo.actuation;

import java.util.HashMap;

import jason.asSyntax.Atom;
import jason.asSyntax.Term;

public abstract class DefaultActuation<T> {
	
	private Atom id;
	private T parameters;
	private HashMap<String, Object> defaultValues;

	public DefaultActuation(Atom id) {
		this(id, null);
	}

	public DefaultActuation(Atom id, T parameters) {
		this.id = id;
		this.parameters = parameters;
	}

	public Atom getId() {
		return id;
	}

	public void setParameters(T parameters) {
		this.parameters = parameters;
	}
	
	public T getParameters() {
		return parameters;
	}
	
	
	
	public HashMap<String, Object> getDefaultParameterValues() {
		return this.defaultValues;
	}

	public void setDefaultParameterValues(HashMap<String, Object> defaultValues) {		
		this.defaultValues = defaultValues;
	}

	/*
	 * Get the number of parameters (the counting strategy depends on the parameter type of each subclass)
	 */
	public abstract int parameterSize();

	/*public void addParameter(Atom parameter) {
		if(this.parameters==null)
			parameters = new ArrayList<Atom>();
		this.parameters.add(parameter);
	}*/
	
	//public abstract Term[] getParameterValuesAsArrayOfTerms(Term[] parameterValues);

	@Override
	public String toString() {
		return "DefaultActuation [id=" + id + ", parameters=" + parameters + ", default parameter values: "+ defaultValues +"]";
	}

	
	public abstract DefaultActuation<T> clone();
	
	
	public abstract Term[] getParametersAsArray();

}
