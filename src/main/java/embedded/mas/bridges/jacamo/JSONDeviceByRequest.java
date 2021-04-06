/**
 * This class represents an interface with a microcontroller whose sensor data follow the JSON format. 
 JSONDeviceByRequest only receive data/beliefs when it sends a request to the microcontroller*/

package embedded.mas.bridges.jacamo;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class JSONDeviceByRequest extends DefaultDevice implements IDevice {
	
	public JSONDeviceByRequest(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);	
	}

	@Override
	public Collection<Literal> getPercepts() throws PerceivingException {
		
		
		microcontroller.write("b");
		System.out.println("sending b...");
		
		String json = microcontroller.read();

		if(json.equals("Message conversation error")) //if the message is not propealy read
			throw new PerceivingException();
		else
		{
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
	
}
