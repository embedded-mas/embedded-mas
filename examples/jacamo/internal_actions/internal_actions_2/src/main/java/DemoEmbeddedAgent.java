
import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import embedded.mas.bridges.jacamo.DemoDevice;
import jason.asSyntax.Atom;




public class DemoEmbeddedAgent extends EmbeddedAgent {
		
	@Override
	public void initAg() {		
		super.initAg();		
	}

	@Override
	protected void setupSensors() {
		
		Arduino4EmbeddedMas arduino = new Arduino4EmbeddedMas("/dev/ttyUSB0",9600);
		arduino.openConnection();
		
		DemoDevice microcontroller1 = new DemoDevice(new Atom("microcontroller1"));			
		this.addSensor(microcontroller1);
				
		DemoDevice microcontroller2= new MyDemoDevice(new Atom("microcontroller2"),arduino);
		this.addSensor(microcontroller2);
		
		
		
		
	}

}
