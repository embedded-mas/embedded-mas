package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;

import jason.RevisionFailedException;
import jason.architecture.AgArch;
import jason.asSemantics.Agent;
import jason.asSemantics.Event;
import jason.asSemantics.Intention;
import jason.asSyntax.Literal;
import jason.asSyntax.Trigger;
import jason.asSyntax.Trigger.TEOperator;
import jason.asSyntax.Trigger.TEType;
import jason.stdlib.sublist;

public abstract class DefaultEmbeddedAgArch extends AgArch{
	
		
	protected Collection <IDevice> devices = null;


	public DefaultEmbeddedAgArch() {		
		super();

	}

	@Override
	public Collection<Literal> perceive() {
		Collection<Literal> p = super.perceive(); //get the default perceptions
		Collection<Literal> sensorData = updateSensor(); //get the sensor data
		if(p!=null&&sensorData!=null) //attach the sensor data in the default perception list
		   p.addAll(sensorData);
		else
			if(sensorData!=null)
				p = sensorData;
		return p;

	}


	public void setDevices(Collection<IDevice> devices) {
		this.devices = devices;
	}


	public Collection<IDevice> getDevices(){
		return this.devices;

	}

	
	private final Collection<Literal> updateSensor() {
		ArrayList<Literal> percepts = new ArrayList<Literal>();
		for(IDevice s:this.devices) { //for each sensor
			percepts.addAll(s.getPercepts()); //get all the sensor data
		}
		if(percepts.size()==0) return null;
		return percepts;
	}

}
