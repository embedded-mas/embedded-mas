package embedded.mas.bridges.javard;

import java.util.ArrayList;
import java.util.List;
import com.fazecast.jSerialComm.*;

public class SerialBuffer extends Thread {

	private SerialPort comPort;
	private String portDescription;
	private int baud_rate;
	
	private List<byte[]> buffer;

	public SerialBuffer(List<byte[]> buffer, String portDescription, int baud_rate) {
		super();
		this.buffer = buffer;
		this.portDescription = portDescription;
		comPort = SerialPort.getCommPort(this.portDescription);
		this.baud_rate = baud_rate;
		comPort.setBaudRate(this.baud_rate);
	}

	@Override
	public void run() {
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		byte[] b = new byte[1];
		comPort.readBytes(b, 1);
		
		while(true) {
			buffer.add(b);
			comPort.readBytes(b, 1);
			System.out.println("b= " + (char)b[0]);
			
			byte[] c = new byte[1];
			for(int i=0; i<buffer.size(); i++) {
				c = buffer.get(i);
				System.out.print((char)c[0]);
			}
			System.out.println();
		try {
			Thread.sleep((long)(Math.random() * 1000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		}
	}


}
