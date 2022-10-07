package embedded.mas.bridges.javard;

import embedded.mas.bridges.javard.SerialBuffer;
import arduino.Arduino;
import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IPhysicalInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fazecast.jSerialComm.*;

public class ArduinoReceiver extends Arduino implements IPhysicalInterface{

	private String preamble = "==";
	private String startMessage = "::";
	private String endMessage = "--";
	
	List<byte[]> buffer =  Collections.synchronizedList(new ArrayList<byte[]>());  

	public ArduinoReceiver(String portDescription, int baud_rate) {
		super(portDescription, baud_rate);
		SerialBuffer serialBuffer = new SerialBuffer(buffer, portDescription, baud_rate);
		serialBuffer.start();
	}
	
	@Override
	public String read() {       
		return this.serialRead();
	}
	
	@Override
	public boolean write(String s) {
		try {
			serialWrite(s);
			return true;
		}catch (Exception e) {
			return false;
		}		
	}
	
	@Override
	public String serialRead() {
		String s = "";
		String start = "";
		String end = "";
		byte[] b = new byte[1];
		
		synchronized (buffer) {
			while(buffer.size()!=0)
			{
			b = this.readByte();
			s = s + (char)b[0];
			}
			/*b = this.readByte();
			while (!start.equals(preamble)) {
				if((char)b[0] == preamble.charAt(start.length())) {
					start = start + (char)b[0];
				}else {
					start = "";
				}
				b = this.readByte();
			}
	
			while (!end.equals(endMessage)) {
				if((char)b[0] == endMessage.charAt(end.length())) {
					end = end + (char)b[0];
				}else {
					s = s + end + (char)b[0];
					end = "";
				}
				b = this.readByte();
			}*/
		}
		try {Thread.sleep((long)(Math.random() * 1000)); } catch (InterruptedException e) { } //espera um tempo aleatório antes de continuar
		
		System.out.println("string: "+s);
		return s;
		/*
		String[] strings = s.split(startMessage);
		int number = Integer.parseInt(strings[0]);
		String message = strings[1];
		if(number == message.length()) {
			return message;
		}
		else {
			return "Message conversation error";
		}*/
}
	
	//le e remove o ultimo elemento do buffer
	private byte[] readByte() {
		byte[] b = new byte[1];
		if(buffer.size()>0) {
			b = buffer.get(buffer.size()-1);
			//buffer.remove(buffer.size()-1);	
		}
		
	return b;
	}

	@Override
	public void execEmbeddedAction(EmbeddedAction action) {
		// TODO implementar
		System.err.println("Method execEmbeddedAction not implemented in " + this.getClass().getName());
		
	}


}
