package embedded.mas.bridges.jacamo.actuation;

import java.util.HashMap;
import java.util.Map;

import jason.asSyntax.Atom;
import jason.asSyntax.Term;

public abstract class DefaultActuation<T> {
	
	private Atom id;
	private T parameters;
	private HashMap<String, Object> defaultValues = new HashMap<String, Object>();
	
	//The parameter mapping records pairs (a,b) s.t. a is the name of the actuation parameter and b is the name of the action parameter
	private Map<Object, Atom> paramMapping = new HashMap<Object,Atom>();

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

	
	
	
	public Map<Object, Atom> getParamMapping() {
		return this.paramMapping;
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

	
	public abstract DefaultActuation<T> clone();
	
	
	@Override
	public String toString() {
		return "DefaultActuation [id=" + id + ", parameters=" + parameters + ", defaultValues=" + defaultValues
				+ ", paramMapping=" + paramMapping + "]";
	}

	public abstract Term[] getParametersAsArray();
	
	
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

	
	public abstract boolean setParamActionMapping(Object actuationParam, Atom actionParam);

	/**
	 * Set the params of the actuation based on action parameters.
	 * The action parameters are a map (x,y) where 
	 *    (i) x is the action parameter identifier
	 *    (ii) y is the parameter value
	 * 
	 * @param actionParams
	 * @return
	 */
	public abstract boolean setParamValuesFromMapping(Map<Atom,Object> actionParams);
	
}
