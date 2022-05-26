package embedded.mas.bridges.ros;

import java.util.List;

import embedded.mas.bridges.jacamo.ILiteralListInterface;
import jason.asSyntax.Literal;

public interface IRosInterface extends ILiteralListInterface {
	
	/* Read a string from the microcontroller */
	public List<Literal> read();
	
	/* Write a string upon the microcontroller. Returns false if it fails.*/
	public boolean write(String topic, String type, String s);

}
