package embedded.mas.comm.serial;

import arduino.Arduino;

public class DefaultJava2Serial implements IJava2Serial {
	
	
	private Arduino connection;
	
	public DefaultJava2Serial(String port, int baudrate){
		connection = new Arduino(port, baudrate);
		connection.openConnection();
	}
	
	@Override
	public void send(String message) {
		connection.serialWrite(message);

	}
	
	public void send(char c) {
		connection.serialWrite(c, 2000);
	}

	@Override
	public String receive() {
		return "receiving";
	}

}
