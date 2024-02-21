package embedded.mas.bridges.jacamo.actuation;

import embedded.mas.bridges.jacamo.DefaultDevice;
import jason.asSyntax.Atom;

public class ActuationDevice {

	private DefaultDevice device;
	private Atom actuationId;
	
	public ActuationDevice(DefaultDevice device, Atom actuationId) {
		super();
		this.device = device;
		this.actuationId = actuationId;
	}

	/*public DefaultDevice getDevice() {
		return device;
	}

	public void setDevice(DefaultDevice device) {
		this.device = device;
	}*/

	public Atom getActuationId() {
		return actuationId;
	}

	public void setActuationId(Atom actuationId) {
		this.actuationId = actuationId;
	}

	@Override
	public String toString() {
		return "(" + device.getId() + "," + actuationId + ")";
	}
	
	
	
}
