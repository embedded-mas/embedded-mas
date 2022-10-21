package embedded.mas.bridges.ros;

import jason.asSyntax.Atom;

public class ServiceRequestAction extends RosAction {
	
	private String serviceName;
	private ServiceParameters serviceParameters;
	
	public ServiceRequestAction(Atom actionName, String serviceName, ServiceParameters serviceParameters) {
		super();
		this.setActionName(actionName);
		this.serviceName = serviceName;
		this.serviceParameters = serviceParameters;
	}

	public String getServiceName() {
		return serviceName;
	}

	
	public ServiceParameters getServiceParameters() {
		return serviceParameters;
	}

	@Override
	public String toString() {
		return "ServiceRequestAction [serviceName=" + serviceName + ", serviceParameters=" + serviceParameters + "]";
	}

	
	
	
	
	

}
