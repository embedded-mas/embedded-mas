package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.List;

import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;

/*
 * An extension of the default Jason agent that gets percepts from sensors
 */

public abstract class EmbeddedAgent extends Agent {

	protected final List<ISensor> sensors = new ArrayList<ISensor>();

	@Override
	public void initAg() {
		setupSensors();
		super.initAg();
		//TODO: find a better way to set the JavinoAgentArch to the JavinoAgent. It is needed to check when/where the agent architecture is set. 
		checkSensor c = new checkSensor();
		c.start();

	}

	/* remove a belief according to the functor and source of the parameter*/
	public void abolishByFunctorAndSource(Literal bel, Agent ag) {
		bel = (Literal) bel.clone();
		bel.makeTermsAnnon(); //change all the terms anonimous variables ("_")
		Literal belFound = ag.findBel(bel, new Unifier()); //find the belief by the functor		
		if(belFound!=null) {
			belFound = (Literal) belFound.clone(); //clone to 
			belFound.delSources(); //del all the sources (the same belief may be many sources) to add the only interesting source (below)
			belFound.addSource(bel.getSources().get(0));
			ag.getBB().remove(belFound);
		}
	}


	/*
	 * Set up the sensors of the agent
	 */
	protected abstract void setupSensors();



	public void addSensor(ISensor sensor) {
		sensors.add(sensor);
	}

	public void removeSensor(ISensor sensor) {
		sensors.remove(sensor);
	}

	class checkSensor extends Thread{

		@Override
		public void run() {
			while(true) {
				synchronized (ts) {
					if(getTS().getUserAgArch() instanceof DefaultEmbeddedAgArch) 
					/* The architecture requres a list of sensors to handle the perceptions. 
						   In some point after the agent creation, an architecture other than DefaultEmbeddedAgArch is set and the list of sensor is lost.
						   This method update the list of sensors if it is null.
						   TODO: improve this */ 
						if(((DefaultEmbeddedAgArch) getTS().getUserAgArch()).getSensors()==null) {
							((DefaultEmbeddedAgArch) getTS().getUserAgArch()).setSensors(sensors);
						}

					//if(getTS().getUserAgArch() instanceof StatArch) 
						/* The architecture requres a list of sensors to handle the perceptions. 
					   In some point after the agent creation, an architecture other than DefaultEmbeddedAgArch is set and the list of sensor is lost.
					   This method update the list of sensors if it is null.
					   TODO: improve this */ 
					//	if(((StatArch) getTS().getAgArch()).getSensors()==null) {
					//		((StatArch) getTS().getAgArch()).setSensors(sensors);
					//	}

				}
			}

		}
	}
}
