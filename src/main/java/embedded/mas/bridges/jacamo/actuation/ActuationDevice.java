package embedded.mas.bridges.jacamo.actuation;

import embedded.mas.bridges.jacamo.DefaultDevice;

public class ActuationDevice {

	private DefaultDevice device;
	private Actuator actuator;
	private DefaultActuation actuation;
	
	public ActuationDevice(DefaultDevice device, Actuator actuator, DefaultActuation actuation) {
		super();
		this.device = device;
		this.actuator = actuator;
		this.actuation = actuation;
	}

	public DefaultDevice getDevice() {
		return device;
	}

	public void setDevice(DefaultDevice device) {
		this.device = device;
	}

	public Actuator getActuator() {
		return this.actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}

	
	
	public DefaultActuation getActuation() {
		return actuation;
	}

	public void setActuation(DefaultActuation actuation) {
		this.actuation = actuation;
	}
	
	
	

	@Override
	public String toString() {
		return "(" + device.getId() + "." + actuator.getId() + "." + actuation.toString() + ")";
	}
	
	
	
}
