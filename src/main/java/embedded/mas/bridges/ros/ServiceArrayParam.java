/**
 * A ServiceArrayParam is a ServiceParam whose (i) value is an array of values (ii) the value is written as an array in the corresponding JSON
 */

package embedded.mas.bridges.ros;

import java.util.ArrayList;
import java.util.Arrays;

public class ServiceArrayParam extends ServiceParam {

	public ServiceArrayParam(String paramName, Object[] paramValue) {		
		super(paramName, paramValue);

	}

	public int size() {
		return ((Object[])this.paramValue).length;
	}
	

	@Override
	public void setParamValue(Object paramValue) {
		if((paramValue instanceof Object[])) {
			this.paramValue = cloneArray((Object[])paramValue);
		}

	}

	@Override
	protected ServiceParam clone() {
		Object[] clonedValue = null;
		if(this.paramValue instanceof Object[])
			clonedValue = cloneArray((Object[])this.paramValue);
		return new ServiceArrayParam(this.paramName, clonedValue);
	}

	
	/**	 
	 * @param value
	 */
	public void add(Object value) {
		Object[] newArray;
		if(this.paramValue==null)
			newArray = new Object[1];
		else
		   newArray = Arrays.copyOf(((Object[])this.paramValue), ((Object[])this.paramValue).length+1);
		newArray[ newArray.length-1] = value;
		this.paramValue = newArray;
	}



	@Override
	public String toJsonValue() {
		String pValue = "";
		if(paramValue==null) return "\""+ paramName + "\":" +"[" + "null" + "]" ; 		
		for(Object o : (Object[])this.getParamValue()) {
			if(o instanceof ServiceParam) {
				if(((ServiceParam)o).getParamValue() instanceof ServiceParameters)					
				   pValue = pValue + ((ServiceParameters)((ServiceParam)o).getParamValue()).toJson() + ",";
			}
			else
				pValue = pValue + o + ",";
		}

		pValue = pValue.replaceAll(",$", "");
		return  "\""+ paramName + "\":" +"[" + pValue + "]" ; 
	}

}
