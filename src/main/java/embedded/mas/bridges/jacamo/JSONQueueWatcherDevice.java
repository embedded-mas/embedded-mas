/**
 * This class represents an interface with a microcontroller whose sensor data follow the JSON format. 
 JSONWatcherDevice only receive data/beliefs when it sends a request to the microcontroller*/

package embedded.mas.bridges.jacamo;
import embedded.mas.bridges.javard.MicrocontrollerMonitor;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import embedded.mas.exception.PerceivingException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class JSONQueueWatcherDevice extends SerialDevice implements IDevice {
	
	List<Collection<Literal>> listOfBeliefs = Collections.synchronizedList(new ArrayList<Collection<Literal>>());
	
	public JSONQueueWatcherDevice(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);	
		MicrocontrollerMonitor microcontrollerMonitor = new MicrocontrollerMonitor(listOfBeliefs,this.getMicrocontroller());
		microcontrollerMonitor.start();
	}
	
	@Override
	public Collection<Literal> getPercepts() throws PerceivingException{
		
		Collection<Literal> percepts = new ArrayList<Literal>();
		
		if(listOfBeliefs.size()>0) {
			 percepts = listOfBeliefs.remove(0);
		}
		try {Thread.sleep((long)(Math.random() * 1000)); } catch (InterruptedException e) { } //espera um tempo aleatório antes de continuar
	return percepts;
	}

	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPhysicalInterface getMicrocontroller() {
		return (IPhysicalInterface) this.microcontroller;
	}
	
	
}
