package embedded.mas.bridges.jacamo.actuation;

import java.util.HashMap;

import jason.asSyntax.Atom;
import jason.asSyntax.Term;

public abstract class DefaultActuation<T> {
	
	private Atom id;
	private T parameters;
	private HashMap<String, Object> defaultValues = new HashMap<String, Object>();

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

	
	
	/**
	 * Clear values of parameters that do not have a default value.
	 * Useful when an actuation needs to back to its original state after getting a parameter value.
	 */
	public abstract void setParametersToDefaultState();
	
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
	
//	/**
//	 * Produce an array of parameters
//	 * @param params
//	 * @return
//	 */
//	public final Term[] setParamValues(Term[] p) {		
//		Term[] params = new Term[this.parameterSize()];
//		Term[] actuation_param_values = this.getParametersAsArray();
//		int k=0,l=0;											
//		for(int i=0;i<this.parameterSize();i++)
//			if(actuation_param_values[k]==null) {
//				params[i] = p[l++]; 
//				k++;
//			}
//			else
//				params[i] = actuation_param_values[k++];
//		return params;
//		
//	}


	
	/**
	 * Set the parameter values from an input array of terms, starting at the initialPosition
	 * 
	 * @return the index of the latest parameter taken from the input array
	 * 
	 * The parameter array may be larger than the amount of required parameters;
	 * For instance, let 
	 *   (i) p=[a,b,c,d,e,f,g] and 
	 *   (ii) an actuation with three parameters s.t. one of them is non-changeable
	 *   (iii) initialPosition = 3
	 *   
	 * In this case, the taken parameters must be [d,e] and the return must be 4.
	 * 
	 */
	public abstract int setParamValues(Object[] p, int initialPosition);

	
}
