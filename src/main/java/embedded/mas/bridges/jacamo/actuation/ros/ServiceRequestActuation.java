package embedded.mas.bridges.jacamo.actuation.ros;

import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class ServiceRequestActuation extends DefaultActuation<ServiceParameters>  {

	private String serviceName;
	

	public ServiceRequestActuation(Atom id, ServiceParameters parameters) {
		super(id, parameters);
	}


	public ServiceRequestActuation(Atom id, String serviceName, ServiceParameters parameters) {
		super(id, parameters);
		this.serviceName = serviceName;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	@Override
	public String toString() {
		return "ServiceRequestActuation [serviceName=" + serviceName + ", getId()=" + getId() + ", getParameters()="
				+ getParameters() + "]";
	}


	@Override
	public int parameterSize() {
		return this.getParameters().size();
	}


	@Override
	public DefaultActuation<ServiceParameters> clone() {
		return new ServiceRequestActuation(this.getId(), this.serviceName, this.getParameters());
	}


	@Override
	public Term[] getParametersAsArray() {
		Term[] params = new Term[this.getParameters().size()];
		int i = 0;
		for(ServiceParam a : getParameters()) {			
			if(getDefaultParameterValues()==null || getDefaultParameterValues().get(a.getParamName().toString())==null) 					
				params[i++] = null;							
			else {
				Object defaultValue = getDefaultParameterValues().get(a.getParamName().toString());
				if (defaultValue instanceof Integer)
					params[i++] = new NumberTermImpl(defaultValue.toString());
				else
				   params[i++] = (Term) defaultValue;
			}
		}
		return params;
	}

}
