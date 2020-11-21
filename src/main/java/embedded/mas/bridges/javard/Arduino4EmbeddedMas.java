package embedded.mas.bridges.javard;

import com.fazecast.jSerialComm.SerialPort;

import arduino.Arduino;

public class Arduino4EmbeddedMas extends Arduino {
	
	private String preamble = "==";
	private String startMessage = "::";
	private String endMessage = "--";	

	@Override
	public String serialRead() {
		
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		byte[] b = new byte[1];

		String s = "";
		String start = "";
		String end = "";

		comPort.readBytes(b, 1);

		while (!start.equals(preamble)) {
			if((char)b[0] == preamble.charAt(start.length())) {
				start = start + b;
			}else {
				start = "";
			}
			comPort.readBytes(b, 1);
		}

		while (!end.equals(endMessage)) {
			if((char)b[0] == endMessage.charAt(end.length())) {
				end = end + b;
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


}
