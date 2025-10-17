/**
 * This class continuously reads the sensor values of a microcontroller (defined in the constructor). 
 * The expected read values are string representing JSON data. 
 * 
 * 
 * 
 * Example of consumming program:
 * 
 * public class Teste {

	public static void main(String[] args) {
		Arduino4EmbeddedMas arduino = new Arduino4EmbeddedMas("/dev/ttyUSB0", 9600);
		List<JsonObject> list = Collections.synchronizedList(new ArrayList<JsonObject>());
		MicrocontrollerSensorString2JsonMonitor monitor = new MicrocontrollerSensorString2JsonMonitor(list, arduino);
		monitor.start();
		while(true) {
			while(list.size()%10!=0) {};
			synchronized (list) {
				for(JsonObject o:list)
					System.out.println(o);
			}
		}
	}

}
 * 
 * @author maiquel
 *
 */

package embedded.mas.bridges.jacamo;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Literal;

public class MicrocontrollerSensorString2JsonMonitor extends MicrocontrollerSensorMonitor<JsonObject> {

	public MicrocontrollerSensorString2JsonMonitor(Collection<JsonObject> list, IExternalInterface microcontroller) {
		super(list, microcontroller);
	}

	@Override
	public void run() {
		while(true) {
			try {
				JsonObject obj = this.decode();
	            if (obj != null) { 
	                this.perceptList.add(obj);
	            }
			} catch (PerceivingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep((long)(100));
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}

		}
	}
	
	
	private JsonObject decode() throws PerceivingException {
		String json = microcontroller.read();
		System.out.println("DECODE " + json);
		if(json!=null && !json.equals("")) { //if reads an non empty string from the microcontroller
			if(json.equals("Message conversation error")) { //if the message is not propealy read
				throw new PerceivingException();		
			}
			else{
				ArrayList<Literal> percepts = new ArrayList<Literal>(); //adicionar os valores lidos arduino na lista percepts (dúvidas - olhar DemoDevice)
				JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));			
				JsonObject jsonObject;
				try {
					jsonObject = reader.readObject();
//					System.out.println("[MicrocontrollerSensorString2JsonMonitor] decoding " + jsonObject);
					return jsonObject;
				} catch (JsonParsingException e) {
					throw new PerceivingException("Invalid JSON: " + json);	
				}
				
			}
		}
		return null;
	}
	

}
