import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.DemoArduinoDevice;
import embedded.mas.bridges.jacamo.DemoDevice;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;




public class DemoEmbeddedAgent extends EmbeddedAgent {
	
	
	
	@Override
	public void initAg() {
		
		super.initAg();
		System.out.println("starting EmbeddedAgent...");		
	}

	@Override
	protected void setupSensors() {
		DemoArduinoDevice device = new DemoArduinoDevice(new Atom("Arduino1"),"COM4",9600);
		//DemoDevice device = new DemoDevice(new Atom("Arduino1"));
		this.addSensor(device);
		
		
		
	}

}
