package embedded.mas.bridges.ros;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import embedded.mas.exception.InvalidRosServiceParameterTypeException;

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

	public boolean addParameter(String parameterName, Object ParameterValue, String parameterType) throws InvalidRosServiceParameterTypeException {
		this.add(new ServiceParam(parameterName, ParameterValue, parameterType));
		return true;		
	}

	@Override
	public String toString() {
		String s = "ServiceParameters ";
		for(int i=0;i<this.size();i++)
			s = s.concat(this.get(i).getParamName());
		return s;
	}
	
	
	
	

}
