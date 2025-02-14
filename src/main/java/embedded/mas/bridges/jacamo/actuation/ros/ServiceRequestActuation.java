package embedded.mas.bridges.jacamo.actuation.ros;

import java.util.List;

import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import jason.asSyntax.Atom;

public class ServiceRequestActuation extends DefaultActuation {

	private String serviceName;
	

	public ServiceRequestActuation(Atom id, List<Atom> parameters) {
		super(id, parameters);
	}


	public ServiceRequestActuation(Atom id, String serviceName, List<Atom> parameters) {
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
	
	

}
