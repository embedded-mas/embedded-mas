package embedded.mas.bridges.jacamo.actuation.ros;

import java.util.HashMap;
import java.util.Map;

import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public abstract class ROSActuation extends DefaultActuation<ServiceParameters> {

	public ROSActuation(Atom id, ServiceParameters parameters) {
		super(id, parameters);
	}


	@Override
	public int parameterSize() {
		return this.getParameters().paramCount();
	}

	@Override
	public Term[] getParametersAsArray() {	
		Term[] t = new Term[this.getParameters().paramCount()];
		internal_getNestedParametersAsArray(this.getParameters(), this.getDefaultParameterValues(), t, 0);	
		return t;


	}

	private int internal_getNestedParametersAsArray(ServiceParameters sp, HashMap<String, Object> dp, Term [] array, int index) {
		//		if(dp==null) { //if there is not default parameters
		//			for(int j=0;j<array.length;j++) 
		//				array[index++]=null;
		//			return index;
		//		}
		for(ServiceParam p : sp) { //for each parameter
			if(p.getParamValue() instanceof ServiceParameters){//if the current parameter is a nested serviceParameters
				if(dp!=null && dp.get(p.getParamName())!=null && dp.get(p.getParamName()) instanceof HashMap<?, ?>)//if the default parameters hashmap has a key corresponding to the current parameter
					index =  internal_getNestedParametersAsArray((ServiceParameters)p.getParamValue(), (HashMap<String, Object>) dp.get(p.getParamName()), array, index); //recursively checks the nested parameters
				else  
					index =  internal_getNestedParametersAsArray((ServiceParameters)p.getParamValue(), null, array, index); //recursively checks the nested parameters
			}
			else { //default case: it is not a nested parameters case
				if(dp!=null && dp.get(p.getParamName())!=null) {
					if(dp.get(p.getParamName()) instanceof Number)
						array[index++] = new NumberTermImpl(dp.get(p.getParamName()).toString());
					else
						array[index++] = (Term) dp.get(p.getParamName());
				}
				else
					//array[index++] = null;
					if(p.getParamValue() instanceof Number)
						array[index++] = new NumberTermImpl(p.getParamValue().toString());
					else
						array[index++] = (Term) p.getParamValue();			

			}
		}

		return index;

	}


	@Override
	public void setDefaultParameterValues(HashMap<String, Object> defaultValues) {
		if(internal_setDefaultParameterValues(defaultValues, this.getParameters()))
			super.setDefaultParameterValues(defaultValues);
	}

	private boolean internal_setDefaultParameterValues(HashMap<String, Object> defaultValues, ServiceParameters parameters) {
		if(defaultValues!=null)
			for(Map.Entry<String, Object> item : defaultValues.entrySet()) {
				if(parameters.getServiceParamByName(item.getKey()) == null) //if there is not a parameter that matches the defaulta param name
					return false;
				if(item.getValue() instanceof HashMap<?, ?> && parameters.getServiceParamByName(item.getKey()).getParamValue() instanceof ServiceParameters) {
					if(internal_setDefaultParameterValues((HashMap<String, Object>) item.getValue(), (ServiceParameters)parameters.getServiceParamByName(item.getKey()).getParamValue()) == false )
						return false;
				}
				else {
					parameters.getServiceParamByName(item.getKey()).setChangeable(true);
					parameters.getServiceParamByName(item.getKey()).setParamValue(item.getValue());
					parameters.getServiceParamByName(item.getKey()).setChangeable(false);
				}
			}
		return true;

	}

	@Override
	public void setParametersToDefaultState() {
		this.getParameters().setToDefaultState();

	}



}
