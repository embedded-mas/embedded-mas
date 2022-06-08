package embedded.mas.bridges.ros;

import com.fasterxml.jackson.databind.JsonNode;

import embedded.mas.bridges.jacamo.LiteralDevice;
import jason.asSyntax.Atom;

public class RosMaster extends LiteralDevice {

	public RosMaster(Atom id, DefaultRos4EmbeddedMas microcontroller) {
		super(id, microcontroller);
	}

	protected final boolean serviceRequest(String serviceName, ServiceParameters parameters) {
		return ((DefaultRos4EmbeddedMas) microcontroller).serviceRequest(serviceName, parameters.toJson()); 
	}
	
	protected final boolean blockingServiceRequest(String serviceName, ServiceParameters parameters) {
		JsonNode serviceReply = ((DefaultRos4EmbeddedMas) microcontroller).serviceRequestResponse(serviceName,parameters.toJson());		
		try {
			return serviceReply.get("result").asBoolean(); //return true whether the request is successful
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
