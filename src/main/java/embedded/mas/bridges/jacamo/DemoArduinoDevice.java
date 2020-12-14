package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;

import embedded.mas.bridges.jacamo.ArduinoDevice;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;


/**
 * 
 * Exemplo de implementação de ArduinoDevice
 * 
 * @author maiquel
 *
 */
public class DemoArduinoDevice extends ArduinoDevice {

	public DemoArduinoDevice(Atom id, String portDescription, int baud_rate) {
		super(id, portDescription, baud_rate);
	}

	@Override
	public Collection<Literal> getPercepts() {
		String s = arduino.serialRead(); //lê o arduino
		
		//transformar a string lida do arduino em crença
		ArrayList<Literal> percepts = new ArrayList<Literal>();
		//adicionar os valores lidos arduino na lista percepts (dúvidas - olhar DemoDevice)
		//percepts.add(Literal.parseLiteral(s)); 
		return percepts;
	}

}
