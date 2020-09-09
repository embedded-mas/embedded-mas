package embedded.mas;

public interface IJava2Serial {
	public void send(String message);
	public String receive();
}
