package exemplo_gradle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialRxTx implements SerialPortEventListener {

	SerialPort serialPort = null;
	
	private Protocolo protocolo = new Protocolo(); // Objeto de gestao de protocolo
	private String appName; // Nome da aplica��o
	
	private BufferedReader input; // Objeto para leitura na serial
	private OutputStream output; // Objeto para a escrita na serial
	
	private static final int TIME_OUT = 1000; //tempo de espera para comunica��o serial
	private static int DATA_RATE = 9600; //define a velocidade da comunica��o serial
	
	private String serialPortName = "/dev/ttyUSB0";
	
	public SerialRxTx() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean iniciaSerial() {
		boolean status = false;
		try {
			//Obtem portas seriais do sistema
			CommPortIdentifier portId = null;
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
			
			while(portId == null && portEnum.hasMoreElements()) {
				CommPortIdentifier currentPortId = (CommPortIdentifier) portEnum.nextElement();
				
				if(currentPortId.getName().equals(serialPortName) || currentPortId.getName().startsWith(serialPortName)) {
						serialPort = (SerialPort) currentPortId.open(appName, TIME_OUT);
						portId = currentPortId;
						System.out.println("Conectado em " + currentPortId.getName());
						break;
				}
			}
			
			if( portId == null || serialPort == null) {
				return false;
			}
			
			serialPort.setSerialPortParams(DATA_RATE, 
								SerialPort.DATABITS_8,
								SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			status = true;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}

	// metodo que envia dado pela serial
	public void sendData(String data) {
		try {
			output = serialPort.getOutputStream(); //aloca o fluxo de saida da porta serial para o objeto output
			output.write(data.getBytes());
		} catch (Exception e) {
			System.err.println(e.toString());
			// Podemos exibir uma mensagem aqui;
		}
	}
	
	//metodo que fecha a porta serial
	public synchronized void close() {
		if(serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	
	//Metodo que lida com a chagada de dados pela serial ao computador.
	//Sempre que chegar algo na serial o metodo � disparado.
	@Override
	public void serialEvent(SerialPortEvent spe) {
		try {
			switch(spe.getEventType()) {
				case SerialPortEvent.DATA_AVAILABLE:
					if(input == null) {
						input = new BufferedReader(
								new InputStreamReader(
										serialPort.getInputStream())); 
					}
					if(input.ready()) {
						protocolo.setLeituraComando(input.readLine()); // le ate uma quebra de linha
						//System.out.println("Chegou: " + protocolo.getLeituraComando());
					}
					
					break;
					
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Protocolo getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}

	public static int getDATA_RATE() {
		return DATA_RATE;
	}

	public static void setDATA_RATE(int dATA_RATE) {
		DATA_RATE = dATA_RATE;
	}

	public String getSerialPortName() {
		return serialPortName;
	}

	public void setSerialPortName(String serialPortName) {
		this.serialPortName = serialPortName;
	}

	
}
