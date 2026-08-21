/***
 * This class implements an array of parameters where each element is a ROS message
 * 
 *  For instance, consider a message with the fields x, y, and z; 
 *  
 *  A param p of these messages would be:
 *  {"p":[{"x":1, "y":2, "z":3},{"x":9, "y":8, "z":7},]}
 */

package embedded.mas.bridges.ros;

import java.lang.reflect.Array;
import java.util.List;

public class ServiceArrayMsgParam extends ServiceArrayParam {

	private ServiceParameters serviceParameters = null;;

	public ServiceArrayMsgParam(String paramName, Object[] paramValue) {
		super(paramName, null);
		this.setParamValue(paramValue);
	}




	public ServiceArrayMsgParam(String paramName, Object[] paramValue, ServiceParameters serviceParameters) {
		super(paramName, paramValue);
		this.serviceParameters = serviceParameters;
		if(paramValue==null)
			this.paramValue = null;
		else
			this.setParamValue(paramValue);
	}





	@Override
	public void add(Object value) {		
		if(value instanceof Object[]) {
			ServiceParameters p = (ServiceParameters) this.serviceParameters.clone();
			for(int i=0;i<p.size();i++)
				p.get(i).setParamValue(((Object[])value)[i]);
			super.add(p);
		}
		else
		if(value instanceof List) {						
			ServiceParameters p = (ServiceParameters) this.serviceParameters.clone();
			for(int i=0;i<p.size();i++)
				p.get(i).setParamValue(((List)value).get(i));
			super.add(p);
		}
	}




	public void setParamValue(Object[] paramValue) {	
		if(this.paramValue==null) this.paramValue = new Object[1];
		Object[] param = new Object[paramValue.length];
		int pIndex = 0;;
		for(Object o: paramValue) { //for each element in the received array of parameters
			if(o.getClass().isArray()) { //each element of paramValue must be an array
				if(Array.getLength(o) ==this.serviceParameters.size()) { //each parameter size must match with the exepected parameters
					ServiceParameters p = (ServiceParameters) this.serviceParameters.clone();
					for(int i=0;i<p.size();i++)
						p.get(i).setParamValue(Array.get(o, i));
					System.out.println("=== " + p.toJson());
					param[pIndex++] = p;
				}
			}
		}

		this.paramValue = param;

	}


	@Override
	public String toJsonValue() {
		String pValue = "";
		if(paramValue==null) return "\""+ paramName + "\":" +"["+ this.serviceParameters.toJson() +"]" ;
		for(Object o : (Object[])this.getParamValue()) {
			System.out.println("[ServideArrayMsgParam] generating json 1 " + pValue);
			if(o instanceof ServiceParameters) {
				pValue = pValue + ((ServiceParameters)o).toJson()+",";
			}
		}
		
		pValue = pValue.replaceAll(",$", "");
		return  "\""+ paramName + "\":" +"[" + pValue + "]" ; 
	}

	@Override
	protected ServiceParam clone() {
		ServiceParameters clonedTemplate = null;
		if(this.serviceParameters != null)
			clonedTemplate = this.serviceParameters.clone();

		ServiceArrayMsgParam clonedParam = new ServiceArrayMsgParam(
				this.paramName, null, clonedTemplate);
		if(this.paramValue instanceof Object[])
			clonedParam.paramValue = cloneArray((Object[])this.paramValue);
		return clonedParam;
	}

}
