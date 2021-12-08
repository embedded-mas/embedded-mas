
package embedded.mas.bridges.javard;

import embedded.mas.bridges.jacamo.*;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
/**
 * Esta classe ilustra um "produtor" em um exemplo de produtor-consumidor.
 * O produtor, nesse caso, alimenta uma lista com números inteiros em sequência.
 * 
 * 
 * 
 * @author maiquel
 *
 */


public class MicrocontrollerMonitor extends Thread {

	private List<Collection<Literal>> lista;
	private IPhysicalInterface microcontroller;
	
	public MicrocontrollerMonitor(List<Collection<Literal>> lista, IPhysicalInterface microcontroller) {
		super();
		this.lista = lista;
		this.microcontroller = microcontroller;
	}

	@Override
	public void run(){
		while(true) {
			try {
				this.decode();
			} catch (PerceivingException e1) {
				if(e1.getMessage()!=null) System.err.println(e1.getMessage());
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
public void decode() throws PerceivingException {
		String json = microcontroller.read();
		if(json.equals("Message conversation error")) { //if the message is not propealy read
			throw new PerceivingException();		
		}
		else{
			ArrayList<Literal> percepts = new ArrayList<Literal>(); //adicionar os valores lidos arduino na lista percepts (dúvidas - olhar DemoDevice)
			JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));			
			JsonObject jsonObject;
			try {
				jsonObject = reader.readObject();
			} catch (JsonParsingException e) {
				throw new PerceivingException("Invalid JSON: " + json);	
			}
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
			this.lista.add(percepts);
		}
	}


}
