package embedded.mas.bridges.jacamo.actuation;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class Actuation extends DefaultActuation<List<Atom>> {

	private HashMap<Atom, Object> parameterValues = new HashMap<Atom, Object>();


	public Actuation(Atom id) {
		this(id, new ArrayList<Atom>());
	}

	public Actuation(Atom id, List<Atom> parameters) {
		super(id, parameters);
	}

	/*public void addParameter(Atom parameter) {
		this.getParameters().add(parameter);
	}*/

	public void setParameterValue(Atom paramName, Object paramValue) {
		//forbid changing parameters that have a default value
		if(this.getDefaultParameterValues()!=null && this.getDefaultParameterValues().get(paramName.toString())!=null) return;

		this.parameterValues.put(paramName, paramValue);
	}

	public Object getParameterValue(Atom paramName) {
		Object value;
		if(this.getDefaultParameterValues() == null) 
			value = null;
		else
			value = this.getDefaultParameterValues().get(paramName.toString());
		if(value != null) return value;
		if(this.parameterValues.get(paramName)==null) return null;
		return this.parameterValues.get(paramName);
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
			for(Object parameter:this.getParameters()) {
				result = result.concat(parameter.toString()).concat("/");
				Object value = this.getParameterValue((Atom) parameter);
				if(value == null)
					result = result.concat("null");
				else
					result = result.concat(value.toString());


				//				if(this.getDefaultParameterValues()!=null) 
				//					if(this.getDefaultParameterValues().get(parameter.toString())!=null)
				//						result = result + "/" + this.getDefaultParameterValues().get(parameter.toString());								 
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
				if(this.getParameterValue(a)!=null) {
					Object paramValue = this.getParameterValue(a);
					if (paramValue instanceof Integer)
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
		for(Atom p : this.getParameters()) //for each parameter
			if(this.getDefaultParameterValues().get(p.toString())==null) //if there is not a default value
				this.parameterValues.remove(p);
	}

	@Override
	public int setParamValues(Object[] p, int initialPosition) {		
		int parameterPosition = 0, //current position in the actuation parameters list
				valuesPosition = initialPosition; //current position in the given parameters array

		while(parameterPosition < this.parameterSize() &&//while there is some parameter to set
				valuesPosition < p.length) //while the array of values has some value to set		
			if(this.getDefaultParameterValues()==null ||  this.getDefaultParameterValues().get(this.getParameters().get(parameterPosition).toString())==null)  //if the corresponding actuation parameter is not a default one  				
				this.setParameterValue(this.getParameters().get(parameterPosition++), p[valuesPosition++]);	
			else
				parameterPosition++;
		return --valuesPosition;

	}




}
