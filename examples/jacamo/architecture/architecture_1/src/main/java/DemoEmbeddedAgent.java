import embedded.mas.bridges.jacamo.EmbeddedAgent;
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
		DemoDevice device = new DemoDevice(new Atom("my_device"));
		this.addSensor(device);
		
		
		
	}

}
