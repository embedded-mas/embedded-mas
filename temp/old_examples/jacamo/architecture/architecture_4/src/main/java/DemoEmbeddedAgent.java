import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.JSONDevice;
import embedded.mas.bridges.jacamo.RxTxDevice;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
//import embedded.mas.bridges.RxTxSerial;
import embedded.mas.bridges.RxTxSerial.SerialRxTx;
import jason.asSyntax.Atom;




public class DemoEmbeddedAgent extends EmbeddedAgent {
	
	
	
	@Override
	public void initAg() {
		super.initAg();
	}

	@Override
	protected void setupSensors() {

		//Arduino4EmbeddedMas arduino = new Arduino4EmbeddedMas("COM4",9600);
		//arduino.openConnection();
		SerialRxTx arduino = new SerialRxTx("COM4", 9600);
		if(arduino.iniciaSerial()) {
			RxTxDevice device = new RxTxDevice(new Atom("Arduino1"), arduino);
			this.addSensor(device);
		}
		//JSONDevice device = new JSONDevice(new Atom("Arduino1"), arduino);
		else {
			System.out.println("falha durante a conexão");
		}
		
		
		
		
	}

}
