import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.DemoDevice;
import jason.asSyntax.Atom;




public class DemoEmbeddedAgent extends EmbeddedAgent {
	

	
	@Override
	public void initAg() {		
		super.initAg();
	}

	@Override
	protected void setupSensors() {
		DemoDevice microcontroller1 = new DemoDevice(new Atom("microcontroller1"));			
		this.addSensor(microcontroller1);		
		
		DemoDevice microcontroller2 = new DemoDevice(new Atom("microcontroller2"));
		this.addSensor(microcontroller2);						
		
	}

}
