package embedded.mas.bridges.ros;

import java.util.ArrayList;
import java.util.List;

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
			if((values[i] instanceof List)|(values[i] instanceof Object[])) {	//if the i-th parameter is a list	


				if(this.get(i) instanceof ServiceArrayParam) {
					if((values[i] instanceof Object[])) {
						for(int j=0;j<((Object[])values[i]).length;j++) {
							((ServiceArrayParam)this.get(i)).add(((Object[])values[i])[j]);
						}
					}
					if(values[i] instanceof List) {
						for(Object t : (List)values[i]) {	   
							((ServiceArrayParam)this.get(i)).add(t);
						}
					}
				} 
				else
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

	@Override
	public String toString() {
		String s = " ";
		for(int i=0;i<this.size();i++) {
			s = s.concat(this.get(i).getParamName()).concat(" ");
			if(this.get(i).getParamValue() instanceof ServiceParameters)
				s = s.concat("[").concat(((ServiceParameters)this.get(i).getParamValue() ).toString().concat("]"));
		}
		return s;
	}


	@Override
	public ServiceParameters clone() {
		ServiceParameters s = new ServiceParameters();
		for (ServiceParam obj : this) {
			s.add(obj.clone()); 
		}
		return s;
	}


}
