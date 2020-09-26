package embedded.mas;

import arduino.Arduino;

public class DefaultJava2Serial implements IJava2Serial {
	
	
	private Arduino connection;
	
	public DefaultJava2Serial(String port, int baudrate){
		connection = new Arduino(port, baudrate);
		connection.openConnection();
	}
	
	@Override
	public void send(String message) {
		//System.out.println("sending:: " + message);
		//connection.serialWrite(message, 2000);
		connection.serialWrite(message);

	}
	
	public void send(char c) {
		connection.serialWrite(c, 2000);
	}

	@Override
	public String receive() {
		// TODO Auto-generated method stub
		return "receiving";
	}

}
