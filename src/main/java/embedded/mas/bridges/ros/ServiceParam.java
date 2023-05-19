package embedded.mas.bridges.ros;

import java.util.Arrays;


public class ServiceParam {
	
	private String paramName;
	private Object paramValue;



	public ServiceParam(String paramName, Object paramValue){
		super();
		this.paramName = paramName;
		this.paramValue = paramValue;				
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
				if(paramValue instanceof String) {					
					if(paramValue.equals("on"))  //on/off values must be translated to 1/0
						pValue = "1";
					else
						if(paramValue.equals("off"))  
							pValue = "0";
						else 								
							pValue = pValue + "\"" + paramValue.toString() + "\"" ;
				}
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
		return "ServiceParam [paramName=" + paramName + ", paramValue=" + paramValue + "]";
	}





}
