package embedded.mas.bridges.ros;

import java.util.Arrays;

import embedded.mas.exception.InvalidRosServiceParameterTypeException;

public class ServiceParam {
	
	private String paramName;
	private Object paramValue;
	private String paramType;


	private String[] paramTypes = {"float32","uint8","string"};

	public ServiceParam(String paramName, Object paramValue, String paramType)// throws InvalidRosServiceParameterTypeException 
	{
		super();
		//if(Arrays.stream(this.paramTypes).anyMatch(paramType::equals)){			
			this.paramName = paramName;
			this.paramValue = paramValue;				
			this.setParamType(paramType);
		//}else
		//	throw new InvalidRosServiceParameterTypeException("Invalid ros service parameter type: " + paramType);
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Object getParamValue() {
		return paramValue;
	}

	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType)// throws InvalidRosServiceParameterTypeException 
	{
		//if(Arrays.asList(this.paramTypes).contains(paramType))
			this.paramType = paramType;
		//else
		//	throw new InvalidRosServiceParameterTypeException("Invalid ros service parameter type: " + paramType);
	}

	/**
	 * Return a string in the key-value json format
	 * @return
	 */
	public String toJsonValue() {
		String pValue = "";;
		if(paramValue.getClass().isArray()) 
			pValue = pValue + arrayToJsonValue((Object[])paramValue);
		else
			if(paramValue instanceof ServiceParameters) {
				pValue = ((ServiceParameters)paramValue).toJson().toString();
			}
			else
				if(paramValue instanceof String) 
					pValue = pValue + "\"" + paramValue.toString() + "\"" ;
				else
					pValue = paramValue.toString();
		return "\""+ paramName + "\":" + pValue ; 
	}

	private String arrayToJsonValue(Object[] paramValue) {
		String pValue = "[";
		for(Object p: (Object[])paramValue) {
			if(p instanceof String) 
				pValue = pValue + "\"" + p.toString() + "\"," ;
			else
				if(p instanceof ServiceParam) 
					pValue = pValue + ((ServiceParam)p).toJsonValue() + ",";
				else
					if(p instanceof ServiceParameters)
						pValue = pValue + ((ServiceParameters)p).toJson() + ",";
					else
						pValue = pValue.concat(p.toString()).concat(",");
		}
		pValue = pValue.substring(0,pValue.length()-1)+"]";
		return pValue;
	}

	@Override
	public String toString() {
		return "ServiceParam [paramName=" + paramName + ", paramValue=" + paramValue + ", paramType=" + paramType + "]";
	}





}
