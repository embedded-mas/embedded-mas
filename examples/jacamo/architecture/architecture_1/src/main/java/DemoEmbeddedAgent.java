import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.DemoSensor;

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
		DemoSensor sensor = new DemoSensor(new Atom("my_sensor"));
		this.addSensor(sensor);
		
		
		
	}

}
