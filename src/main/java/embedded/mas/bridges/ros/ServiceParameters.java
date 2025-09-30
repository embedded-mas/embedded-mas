package embedded.mas.bridges.ros;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

public class ServiceParameters extends ArrayList<ServiceParam> {

	HashMap<String, Object> defaultParamValues = new HashMap<String, Object>(); 

	public JsonNode toJson() {
		String s = "";
		for(ServiceParam p:this) {
			s = s.concat(p.toJsonValue()) + ",";		
		}
		if(s.length()>0)
			s = s.substring(0, s.length()-1); //remove the last comma
		try {
			return new ObjectMapper().readTree("{"+s+"}");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean addParameter(String parameterName, Object ParameterValue) {
		this.add(new ServiceParam(parameterName, ParameterValue));
		return true;		
	}

	/* set a list of values to a list of parameterss */
	public boolean setValues(Object[] values) {
		if(values.length!=this.size())
			return false;
		for(int i=0;i<values.length;i++) {
			if((values[i] instanceof ListTermImpl)|(values[i] instanceof Object[])) {	//if the i-th parameter is a list						
				if(this.get(i).getParamValue() instanceof ServiceParameters) { //if the value is a list, the corresponding param must be a list of parameters
					Object[] v = null;
					if(values[i] instanceof ListTermImpl) {
						v = ((ListTermImpl)values[i]).toArray();
					}
					else
						if(values[i] instanceof Object[]) {
							v = (Object[]) values[i];
						}
					((ServiceParameters)this.get(i).getParamValue()).setValues(v);
				}
				else	
					return false;
			}
			else
			{
				this.get(i).setParamValue(values[i]);
			}
		}
		return true;
	}


	/**
	 * Return the number of parameters.
	 * It may be different of the size attribute in the case of nested parameters
	 */
	public int paramCount() {
		return this.privateParamCount(0);

	}


	private int privateParamCount(int count) {
		for(ServiceParam p : this)
			if(p.getParamValue() instanceof ServiceParameters)
				count = count + ((ServiceParameters)p.getParamValue()).paramCount();
			else
				count++;
		return count;
	}

	@Override
	public String toString() {
		String s = "";
		for(int i=0;i<this.size();i++) {
			s = s.concat(this.get(i).getParamName());
			if(this.get(i).getParamValue() != null) {
				if(this.get(i).getParamValue() instanceof ServiceParameters)
					s = s.concat("[").concat(this.get(i).getParamValue().toString().concat("]"));
				else
					s = s.concat("/").concat(this.get(i).getParamValue().toString());
			}
			else
				s = s.concat("/null");
			if(i<this.size()-1) 
				s = s.concat(", ");
		}
		return s;
	}


	/**
	 * Fill the parameter values with values from an plain array of object.
	 * @param array - plain array  (i.e. no nested arrays)  of terms to be assigned to the parameter values. Parameters with default values must be ignored in this array (i.e. must not have corresponding value)
	 * @return true if success, false otherwise
	 */
	public boolean setValuesFromArray(Object[] array) {
		//return internal_setValuesFromArray(array, 0)!=-1;
		return setParamValues(array, 0)==array.length-1;
	}


	private int internal_setValuesFromArray(Object[] array, int index) {
		//TODO: check whether the array size does not match the parameter count
		for(ServiceParam p : this) //for each service param
			if(p.getParamValue() instanceof ServiceParameters) { //if the current param is a set of nested parameters
				index = ((ServiceParameters)p.getParamValue()).internal_setValuesFromArray(array, index);
				if(index==-1) 
					return -1;									
			}
			else 
				if(p.isChangeable())
					p.setParamValue(array[index++]);
				else
					index++;


		return index;
	}


	public ServiceParam getServiceParamByName(String paramName) {
		for(ServiceParam p : this)
			if(p.getParamName().equals(paramName))
				return p;
		return null;
	}

	public void setToDefaultState() {
		for(ServiceParam p : this) //for each service para,
			if(p.getParamValue() instanceof ServiceParameters) { //if the current param is a set of nested parameters
				((ServiceParameters)p.getParamValue()).setToDefaultState(); //recursively set the nested parameters to the default state
			}
			else
				if(p.isChangeable()) //if the parameter is not a default one (it is changeable)
					p.setParamValue(null); //set param value to null		
	}

	/**
	 * 
	 * @param p array of parameters
	 * @param initialPosition initial position to be considered in the array of parameters
	 * @return the index of the latest parameter taken from the input array
	 * 
	 * For instance, let 
	 *   (i) p=[a,b,c,d,e,f,g] and 
	 *   (ii) an actuation with three parameters s.t. one of them is non-changeable
	 *   (iii) initialPosition = 3
	 *   
	 * In this case, the taken parameters must be [d,e] and the return must be 4.
	 */


	public int setParamValues(Object[] p, int initialPosition) {
		int valuesPosition = initialPosition; //current position in the given parameters array
		for(ServiceParam param : this) //for each service parameter
			if(param.getParamValue() instanceof ServiceParameters) { //if the current param is a set of nested parameters
				if(p[valuesPosition] instanceof ListTermImpl && ((ListTermImpl)p[valuesPosition]).get(0) instanceof ListTermImpl) { 
					setListOfParamValues((ServiceParameters)param.getParamValue(), (ListTermImpl)p[valuesPosition]);
					valuesPosition++;
				}
				else
					valuesPosition = ((ServiceParameters)param.getParamValue()).setParamValues(p, valuesPosition)+1; //recursively set the nested parameters to the default state
			}
			else 
				if(param.isChangeable()) { //if the parameter is not a default one (it is changeable)
					param.setParamValue(p[valuesPosition++]); //set param value to null		
				}
		return --valuesPosition;
	}



	/***
	 * Atribuir os valores em p aos parâmetros em parameters
	 * 
	 * 
	 * parameters é o array de parameters (ex. [arrray_parameter_0[y11/null, y12/null], arrray_parameter_1[y21/null, y22/null], arrray_parameter_2[y31/null, y32/null]])
	 * p é a lista de parâmetros a ser atribuída (ex.: [[2,3],[4,5],[6,7]])
         * 
         * resultado  esperado: [arrray_parameter_0[y11/2, y12/3], arrray_parameter_1[y21/4, y22/5], arrray_parameter_2[y31/6, y32/7]]
	 * 
	 *  
	 *  
	 * @param parameters
	 * @return
	 */
	private int setListOfParamValues(ServiceParameters parameters, ListTermImpl p) {
		int pIndexI = 0, pIndexj;
		for(ServiceParam param : parameters) //for each ServiceParameter
			if(param.getParamValue() instanceof ServiceParameters) {
				if((p.get(pIndexI) instanceof ListTermImpl) && ((ListTermImpl)p.get(pIndexI)).size()==((ServiceParameters)param.getParamValue()).size() ) {
					pIndexj=0;
					for(ServiceParam nestedParam : (ServiceParameters)param.getParamValue()) //for each nested param
						nestedParam.setParamValue(((ListTermImpl)p.get(pIndexI)).get(pIndexj++));
				}
				pIndexI++;
			}


		return 0;
	}

	@Override
	public ServiceParameters clone() {
		ServiceParameters parameters = new ServiceParameters();
		for(ServiceParam p:this) {
			ServiceParam newP;
			if(p instanceof ServiceArrayParam)
				newP = new ServiceArrayParam(p.getParamName(), null);
			else
				newP = new ServiceParam(p.getParamName(), null, p.isChangeable());
			if(p.getParamValue() instanceof ServiceParameters)
				newP.setParamValue(((ServiceParameters)p.getParamValue()).clone());	
			else
				if(!p.isChangeable()) { //if it is a default parameters
					newP.setChangeable(true);
					newP.setParamValue(p.getParamValue());
					newP.setChangeable(false);
				}

			parameters.add(newP);

		}
		return parameters;
	}



}
