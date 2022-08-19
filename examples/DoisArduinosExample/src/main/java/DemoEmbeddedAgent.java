package src.main.java;

import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.SampleDevice;
import embedded.mas.bridges.javard.SerialReader;

import jason.asSyntax.Atom;


public class DemoEmbeddedAgent extends EmbeddedAgent {
	
	private SerialReader arduinoSensor = new SerialReader("/dev/ttyUSB0",9600);
	private SerialReader arduinoAtuador = new SerialReader("/dev/ttyUSB1",9600);
	
	@Override
	public void initAg() {
		super.initAg();
	}

	@Override
	protected void setupSensors() {
		SampleDevice deviceSensor = new SampleDevice(new Atom("arduinoSensor"), arduinoSensor);
		SampleDevice deviceAtuador = new SampleDevice(new Atom("arduinoAtuador"),arduinoAtuador);
		this.addSensor(deviceSensor);
		this.addSensor(deviceAtuador);
	}
	
	
	// #Acoes que o agente eh capaz de realizar
	public boolean ligaLed() {
		//System.out.println("ligando led...");
		SampleDevice device1 =(SampleDevice)this.getDevices().get(1);
		device1.doLightOn();
		return true;
	}
	public boolean desligaLed() {
		//System.out.println("desligando led...");
		SampleDevice device1 =(SampleDevice)this.getDevices().get(1);
		device1.doLightOff();
		return true;
	}
}
