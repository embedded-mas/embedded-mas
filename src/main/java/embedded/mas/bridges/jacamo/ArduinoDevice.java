package embedded.mas.bridges.jacamo;


import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import jason.asSyntax.Atom;

public abstract class ArduinoDevice extends DefaultDevice implements IDevice {
	
	protected Arduino4EmbeddedMas arduino;
		
	public ArduinoDevice(Atom id, String portDescription, int baud_rate) {
		super(id);	
		arduino = new Arduino4EmbeddedMas(portDescription, baud_rate);		
		arduino.openConnection();
	}
	
	


}
