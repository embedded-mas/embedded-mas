package embedded.mas;

public class DefaultJava2Serial implements IJava2Serial {

	@Override
	public void send(String message) {
		System.out.println("sending " + message);

	}

	@Override
	public String receive() {
		// TODO Auto-generated method stub
		return "receiving";
	}

}
