/**
 * A ServiceArrayParam is a ServiceParam whose (i) value is a ServiceParameters and (ii) the value is written as an array in the corresponding JSON
 */

package embedded.mas.bridges.ros;

public class ServiceArrayParam extends ServiceParam {

	public ServiceArrayParam(String paramName, ServiceParameters paramValue) {
		super(paramName, paramValue);
	}

	@Override
	public String toJsonValue() {
		String pValue = "";
		if(paramValue==null) return super.toJsonValue();

		ServiceParameters parameters = (ServiceParameters)this.getParamValue();
		for(ServiceParam param : parameters)
			if(param.getParamValue() instanceof ServiceParameters)
				pValue = pValue + ((ServiceParameters)param.getParamValue()).toJson() + ",";
			else
				if(param.getParamValue()==null)
					pValue = pValue + "null" + ",";
				else
					pValue = pValue + param.getParamValue().toString() + ",";
		pValue = pValue.replaceAll(",$", "");
		return  "\""+ paramName + "\":" +"[" + pValue + "]" ; 
	}

}
