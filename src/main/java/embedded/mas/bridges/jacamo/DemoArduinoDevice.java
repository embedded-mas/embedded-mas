package embedded.mas.bridges.jacamo;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import jason.asSyntax.Atom;
import sun.tools.jstat.Literal;


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
		String json = arduino.serialRead(); //lê o arduino
		ArrayList<Literal> percepts = new ArrayList<Literal>(); //adicionar os valores lidos arduino na lista percepts (dúvidas - olhar DemoDevice)
		
		JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
    	JsonObject jsonObject = reader.readObject(); //transformar a string em um "objeto JSON"
    	
    	for(String key: jsonObject.keySet()) { //iterar sobre todos os elementos do JsonObject - a variável "key" armazena cada chave do objeto json    		
    		Object value = jsonObject.get(key); //obtém o valor associado à chave "key"
    		String belief = key +"(";
    		if(!(value instanceof JsonArray)) //se o valor não for um vetor (ou seja, se for uma belief com apenas um valor)
    		   belief = belief + value;
    		else { //se for um vetor [v1,v2,...,vn], monta uma belief key(v1,v2,...,vn)    			
    				belief = belief + value.toString().replace("[","").replace("]", "");	 	
    		}
    		belief = belief + ")";
    		
    		//System.out.println(belief);
    		percepts.add(Literal.parseLiteral(belief));
    	}
		
		return percepts;
	}
	
	
}
