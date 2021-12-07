package src.main.java;

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
		Arduino4EmbeddedMas arduino = new Arduino4EmbeddedMas("COM3",9600);
		arduino.openConnection();
		
		MyDemoDevice device = new MyDemoDevice(new Atom("arduino1"), arduino);
		this.addSensor(device);	
	}
	
	
	// #Acoes que o agente é capaz de realizar
	public boolean lightOn() {
		System.out.println("Acendendo Led...");
		MyDemoDevice device = (MyDemoDevice)this.devices.get(0);
		device.doLightOn();
		return true;
	}
	public boolean lightOff() {
		System.out.println("Apagando Led...");
		MyDemoDevice device =(MyDemoDevice)this.devices.get(0);
		device.doLightOff();
		return true;
	}
	//#
}
