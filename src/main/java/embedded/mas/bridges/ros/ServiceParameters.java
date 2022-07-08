package embedded.mas.bridges.ros;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

}
