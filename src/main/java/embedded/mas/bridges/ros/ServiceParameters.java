package embedded.mas.bridges.ros;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jason.asSyntax.ListTermImpl;

public class ServiceParameters extends ArrayList<ServiceParam> {
	
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
			if(i<this.size()-1) 
				s = s.concat(", ");
		}
		return s;
	}


	/**
	 * Fill the parameter values with values from an plain array of object.
	 * @param array - plain array  (i.e. no nested arrays)  of terms to be assigned to the parameter values
	 * @return true if success, false otherwise
	 */
	public boolean setValuesFromArray(Object[] array) {
		return internal_setValuesFromArray(array, 0)!=-1;
	}
	
	
	public int internal_setValuesFromArray(Object[] array, int index) {
		//TODO: check whether the array size does not match the parameter count
		for(ServiceParam p : this) //for each service param
			if(p.getParamValue() instanceof ServiceParameters) {
				index = ((ServiceParameters)p.getParamValue()).internal_setValuesFromArray(array, index);
				if(index==-1) 
					return -1;									
			}
			else {
				p.setParamValue(array[index++]);
			}

		return index;
	}


}
