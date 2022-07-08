package src.main.java;

import embedded.mas.bridges.jacamo.EmbeddedAgent;
import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.CarDevice;
import embedded.mas.bridges.javard.SerialReader;

import jason.asSyntax.Atom;


public class DemoEmbeddedAgent extends EmbeddedAgent {
	
	@Override
	public void initAg() {
		super.initAg();
	}

	@Override
	protected void setupSensors() {
		SerialReader arduino = new SerialReader("/dev/ttyUSB0",9600);
		
		CarDevice device = new CarDevice(new Atom("arduino1"), arduino);
		this.addSensor(device);	
	}
	
	
	// #Acoes que o agente eh capaz de realizar

	public boolean frente() {
		//print de monitoramento da ação
		System.out.println("Fazendo o carro andar para frente...");
		//seleciona os devices e suas atuacoes que que serão utilizados na acao
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doMotor1Horario();
		device1.doMotor2Horario();
		return true;
	}
	public boolean tras() {
		System.out.println("Fazendo o carro andar para trás...");
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doMotor1Antihorario();
		device1.doMotor2Antihorario();
		return true;
	}

	//Exemplo de como fazer uma mesma acao com atuacoes diferentes:
	public boolean esquerda() {
		System.out.println("Fazendo o carro andar para esquerda...");
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doMotor1Horario();
		device1.doMotor2Antihorario();
		return true;
	}
	public boolean esquerda(int a) {
		System.out.println("Fazendo o carro andar para esquerda...");
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doMotor1Horario();
		device1.doMotor2Para();
		return true;
	}
	public boolean direita() {
		System.out.println("Fazendo o carro andar para direita...");
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doMotor1Antihorario();
		device1.doMotor2Horario();
		return true;
	}
	public boolean para() {
		System.out.println("Fazendo o carro parar...");
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doMotor1Para();
		device1.doMotor2Para();
		return true;
	}
	public boolean ligaLed() {
		System.out.println("ligando led...");
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doLightOn();
		return true;
	}
	public boolean desligaLed() {
		System.out.println("desligando led...");
		CarDevice device1 =(CarDevice)this.getDevices().get(0);
		device1.doLightOff();
		return true;
	}
}
