package src.main.java;

import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import embedded.mas.bridges.javard.SerialReader;

import jason.asSyntax.Atom;


public class DemoEmbeddedAgent extends EmbeddedAgent {
	
	@Override
	public void initAg() {
		super.initAg();
	}

	@Override
	protected void setupDevices() {
		//Arduino4EmbeddedMas arduino = new Arduino4EmbeddedMas("/dev/ttyACM0",115200);
		//arduino.openConnection();
		SerialReader arduino = new SerialReader("/dev/ttyACM0",115200);
		
		MyDemoDevice device = new MyDemoDevice(new Atom("arduino1"), arduino);
		this.addSensor(device);	
	}
	
	
	// #Acoes que o agente eh capaz de realizar

	public boolean frente() {
		//print de monitoramento da ação
		System.out.println("Fazendo o carro andar para frente");
		//seleciona os devices e suas atuacoes que que serão utilizados na acao
		MyDemoDevice device1 =(MyDemoDevice)this.getDevices().get(0);
		device1.motorFrontLeftForward();
		device1.motorFrontRightForward();
		device1.motorBackLeftForward();
		device1.motorBackRightForward();
		return true;
	}
	public boolean tras() {
		System.out.println("Fazendo o carro andar para trás");
		MyDemoDevice device1 =(MyDemoDevice)this.getDevices().get(0);
		device1.motorFrontLeftBackward();
		device1.motorFrontRightBackward();
		device1.motorBackLeftBackward();
		device1.motorBackRightBackward();
		return true;
	}

	//Exemplo de como fazer uma mesma acao com atuacoes diferentes:
	public boolean esquerda() {
		System.out.println("Fazendo o carro andar para esquerda");
		MyDemoDevice device1 =(MyDemoDevice)this.getDevices().get(0);
		device1.motorFrontLeftBackward();
		device1.motorFrontRightForward();
		device1.motorBackLeftBackward();
		device1.motorBackRightForward();
		return true;
	}	

	public boolean direita() {
		System.out.println("Fazendo o carro andar para direita");
		MyDemoDevice device1 =(MyDemoDevice)this.getDevices().get(0);
		device1.motorFrontLeftForward();
		device1.motorFrontRightBackward();
		device1.motorBackLeftForward();
		device1.motorBackRightBackward();
		return true;
	}
	public boolean para() {
		System.out.println("Fazendo o carro andar para para");
		MyDemoDevice device1 =(MyDemoDevice)this.getDevices().get(0);
		device1.motorFrontLeftStop();
		device1.motorFrontRightStop();
		device1.motorBackLeftStop();
		device1.motorBackRightStop();
		return true;
	}
}
