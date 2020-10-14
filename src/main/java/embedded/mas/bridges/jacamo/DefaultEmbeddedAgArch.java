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

public abstract class DefaultEmbeddedAgArch extends AgArch{
	
		
	protected Collection <ISensor> sensors = null;


	public DefaultEmbeddedAgArch() {		
		super();

	}

	@Override
	public Collection<Literal> perceive() {
		if(this.sensors!=null)
			updateSensor();

		return super.perceive();

	}


	public void setSensors(Collection<ISensor> sensors) {
		this.sensors = sensors;
	}


	public Collection<ISensor> getSensors(){
		return this.sensors;

	}

	private final void updateSensor() {
		for(ISensor s:this.sensors) { //for each sensor
			Collection<Literal> percepts = s.getPercepts(); //get all the percepts from the current sensor
			if(percepts!=null) {
				for(Literal l:percepts) { //for each percept..
					try {					
						((EmbeddedAgent)getTS().getAg()).abolishByFunctorAndSource(l, getTS().getAg()); //remove the corresponding current belief
						if(getTS().getAg().addBel(l)) {
							Trigger te = new Trigger(TEOperator.add, TEType.belief, l.copy());
							getTS().updateEvents(new Event(te, Intention.EmptyInt));
						}

					} catch (RevisionFailedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
