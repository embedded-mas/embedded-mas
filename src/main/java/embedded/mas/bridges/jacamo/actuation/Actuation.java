package embedded.mas.bridges.jacamo.actuation;

import java.util.Map;
import java.util.LinkedHashMap;

import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;

/**
 * TODO: Replace LinkedHashMap by hashmap when the Action parameters are finished	
 * 
 *
 */

public class Actuation extends DefaultActuation<LinkedHashMap<Atom, Object>> {



	public Actuation(Atom id) {
		this(id, new LinkedHashMap<Atom, Object>());
	}

	public Actuation(Atom id, LinkedHashMap<Atom, Object> parameters) {
		super(id, parameters);
	}

	public void setParameterValue(Atom paramName, Object paramValue) {
		//forbid changing parameters that have a default value
		if(this.getDefaultParameterValues()!=null && this.getDefaultParameterValues().get(paramName.toString())!=null) return;

		this.getParameters().put(paramName, paramValue);
	}

	public Object getParameterValue(Atom paramName) {
		Object value;
		if(this.getDefaultParameterValues() == null) 
			value = null;
		else
			value = this.getDefaultParameterValues().get(paramName.toString());
		if(value != null) return value;
		if(this.getParameters().get(paramName)==null) return null;
		return this.getParameters().get(paramName);
	}

	public void removeParameter(Atom parameter) {
		this.getParameters().remove(parameter);
	}
	

	public void setDefaultParameterValue(String key, Object value) {		
		this.getDefaultParameterValues().put(key, value);
	}


	@Override
	public String toString() {		
		String result = this.getId() + "(";
		if(this.getParameters()!=null) {
			for(Map.Entry<Atom, Object> parameter : this.getParameters().entrySet()) {
				result = result.concat(parameter.getKey().toString()).concat("/");
				Object value = parameter.getValue();
				if(value == null)
					result = result.concat("null");
				else
					result = result.concat(value.toString());
				if(this.getParamMapping().get(parameter.getKey())!=null)
					result = result.concat("("+ this.getParamMapping().get(parameter.getKey()) +")");
				result = result.concat( ", ");
				if(this.getDefaultParameterValues()!=null && this.getDefaultParameterValues().get(parameter)!=null) {
					result = result + this.getDefaultParameterValues().get(parameter);

				}
			}
			if(getParameters().size()>0)
				result = result.substring(0, result.length()-2);
		}
		if(this.getParamMapping()!=null) result = result + "[mapping: " + this.getParamMapping() + "]";


		return result;
	}

	@Override
	public int parameterSize() {
		return this.getParameters().size();
	}

	@Override
	public DefaultActuation<LinkedHashMap<Atom, Object>> clone() {
		return new Actuation(this.getId(), (LinkedHashMap<Atom, Object>) this.getParameters().clone());
	}

	@Override
	public Term[] getParametersAsArray() {
		Term[] params = new Term[getParameters().size()];
		int i = 0;
		for(Atom a : getParameters().keySet()) {			
			if(getDefaultParameterValues()==null || getDefaultParameterValues().get(a.toString())==null) {
				if(this.getParameterValue(a)!=null) {
					Object paramValue = this.getParameterValue(a);
					if (paramValue instanceof Integer | paramValue instanceof Float | paramValue instanceof Double)
						params[i++] = new NumberTermImpl(paramValue.toString());
					else
						params[i++] = (Term) paramValue;
				}
				else
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

	@Override
	public void setParametersToDefaultState() {
		for(Atom p : this.getParameters().keySet()) //for each parameter
			if(this.getDefaultParameterValues().get(p.toString())==null) //if there is not a default value
				this.getParameters().put(p, null);
	}

	@Override
	public int setParamValues(Object[] p, int initialPosition) {		
		int parameterPosition = 0, //current position in the actuation parameters list
				valuesPosition = initialPosition; //current position in the given parameters array

		int cont = 0;
		Object[] params = this.getParameters().keySet().toArray();
		while(parameterPosition < this.parameterSize() &&//while there is some parameter to set
				valuesPosition < p.length) //while the array of values has some value to set		
			if(this.getDefaultParameterValues()==null ||  this.getDefaultParameterValues().get(params[parameterPosition].toString())==null)  //if the corresponding actuation parameter is not a default one  				
				this.setParameterValue((Atom) params[parameterPosition++], p[valuesPosition++]);	
			else
				parameterPosition++;	
		return --valuesPosition;

	}



	@Override
	public boolean setParamActionMapping(Object actuationParam, Atom actionParam) {
		if(actuationParam instanceof String)
			actuationParam = createAtom((String)actuationParam);
		if(!(actuationParam instanceof Atom)) return false;		
		this.getParamMapping().put(actuationParam, actionParam);
		return true;
	}



	/**
	 * Set the params of the actuation based on action parameters.
	 * The action parameters are a map (x,y) where 
	 *    (i) x is the action parameter identifier
	 *    (ii) y is the parameter value
	 * 
	 * @param actionParams
	 * @return
	 */
	@Override
	public boolean setParamValuesFromMapping(Map<Atom, Object> actionParams) {
		for(Map.Entry<Object, Atom> mapping : this.getParamMapping().entrySet())   //for each mapping (actuationParameter, actionParameter) set in this actuation
			if(actionParams.get(mapping.getValue())!=null)   //if the received parameter has a mapping corresponding to the action parameter associated to the current actuation parameter
				this.getParameters().put((Atom)mapping.getKey(), actionParams.get(mapping.getValue()));
		return true;
	}




}
