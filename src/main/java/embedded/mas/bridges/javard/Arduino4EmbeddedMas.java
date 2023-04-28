/**
 * This class is specialization of the Arduino classe from the Javard package.
 * While the superclasse reads and writes in an Arduino device, this class decodes the read messages according to a predefined protocol that includes 
 * the preamble of the message, as well as the symbols of starting and ending of the message. 
 * 
 */

package embedded.mas.bridges.javard;

import arduino.Arduino;
import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IPhysicalInterface;

import com.fazecast.jSerialComm.*;

public class Arduino4EmbeddedMas extends Arduino implements IPhysicalInterface{

	private String preamble = "==";
	private String startMessage = "::";
	private String endMessage = "--";
	private boolean connected = false;


	public Arduino4EmbeddedMas(String portDescription, int baud_rate) {
		super(portDescription, baud_rate);
		this.openConnection();
	}
	

	@SuppressWarnings("unchecked")
	public String read() {      
		String msg = this.serialRead();
		return msg;
	}
	
	
	@Override
	public boolean write(String s) {
		try {
			System.out.println("[Arduino4EmbeddedMas] writing " + s);
			serialWrite(s);
			return true;
		}catch (Exception e) {
			return false;
		}		
	}
	
	@Override
	public void closeConnection() {
		super.closeConnection();
		this.connected = false;
	}

	@Override
	public boolean openConnection() {
		if(!connected)
		   this.connected  =  super.openConnection();
		return this.connected;
	}

	/**
	 * While the superclasse reads and writes in an Arduino device, this class decodes the read messages according to a predefined protocol that includes 
	 * the preamble of the message, as well as the symbols of starting and ending of the message. 
	 * 
	 * The result of the reading is the payload of the whole message gotten from the Arduino.	 
	 */
	@Override
	public String serialRead() {
		if(comPort.bytesAvailable()==0 ) return "";
		String s = "";
		String start = "";
		String end = "";

		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		byte[] b = new byte[1];

		comPort.readBytes(b, 1);
		while (!start.equals(preamble)) {
			if((char)b[0] == preamble.charAt(start.length())) {
				start = start + (char)b[0];
			}else {
				start = "";
			}
			comPort.readBytes(b, 1);
		}

		while (!end.equals(endMessage)) {
			if((char)b[0] == endMessage.charAt(end.length())) {
				end = end + (char)b[0];
			}else {
				s = s + end + (char)b[0];
				end = "";
			}
			comPort.readBytes(b, 1);
		}

		String[] strings = s.split(startMessage);
		int number = Integer.parseInt(strings[0]);
		String message = strings[1];

		if(number == message.length()) {
			return message;
		}
		else {
			return "Message conversation error";
		}
		
	}


	@Override
	public void execEmbeddedAction(EmbeddedAction action) {
		System.err.println("Method execEmbeddedAction not implemented in " + this.getClass().getName());
		
	}


}
