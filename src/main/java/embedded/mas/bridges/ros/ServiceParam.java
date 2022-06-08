package embedded.mas.bridges.ros;

public class ServiceParam {
	
	private String paramName;
	private Object paramValue;
	
	public ServiceParam(String paramName, Object paramValue) {
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
		return "\""+ paramName + "\":" + paramValue.toString(); 
	}
	
	

}
