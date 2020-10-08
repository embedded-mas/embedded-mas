package embedded.mas.comm.serial;

public interface IJava2Serial {
	
	public void send(String message);
	public String receive();
	
}
