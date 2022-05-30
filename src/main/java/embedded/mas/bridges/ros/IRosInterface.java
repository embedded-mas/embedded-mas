package embedded.mas.bridges.ros;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import embedded.mas.bridges.jacamo.ILiteralListInterface;
import jason.asSyntax.Literal;

public interface IRosInterface extends ILiteralListInterface {
	
	/* Read a string from the microcontroller */
	public List<Literal> read();
	
	/* Publish in a ros topic.*/
	public boolean write(String topic, String type, String s);
	
	/*Service request*/
	public boolean serviceRequest(String serviceName, JsonNode serviceArguments);

}
