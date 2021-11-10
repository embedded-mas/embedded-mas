import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;

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
		
		DefaultDevice device = new MyDemoDevice(new Atom("arduino1"), arduino);
		this.addSensor(device);
		
		//#Acao1 : ligar atuador 1 do arduino1 e ligar atuador 3 do arduino2
		
		//#Acao2 : desligar atuador 1 do arduino1 e ligar atuador 2 do arduino2
		
		//cada ação possivel do agente teria a sua respsctiva classe dentro da biblioteca de açoes
	}

}
