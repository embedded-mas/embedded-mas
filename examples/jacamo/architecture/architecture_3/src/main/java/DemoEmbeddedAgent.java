import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.JSONDeviceByRequest;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;

import jason.asSyntax.Atom;




public class DemoEmbeddedAgent extends EmbeddedAgent {
	
	
	
	@Override
	public void initAg() {
		super.initAg();
	}

	@Override
	protected void setupSensors() {

		Arduino4EmbeddedMas arduino = new Arduino4EmbeddedMas("COM4",9600);
		arduino.openConnection();
		
		JSONDeviceByRequest device = new JSONDeviceByRequest(new Atom("Arduino1"), arduino);
		this.addSensor(device);
		
		
		
	}

}
