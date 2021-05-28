package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.List;

import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;

/*
 * An extension of the default Jason agent that gets percepts from devices
 */

public abstract class EmbeddedAgent extends Agent {

	protected final List<DefaultDevice> devices = new ArrayList<DefaultDevice>();



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
	 * Set up the devices of the agent
	 */
	protected abstract void setupSensors();



	public void addSensor(DefaultDevice device) {
		devices.add(device);
	}

	public void removeSensor(DefaultDevice device) {
		devices.remove(device);
	}

	public List<DefaultDevice> getDevices(){
		return this.devices;
	}

	class checkSensor extends Thread{

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			while(true) {
				
				
				if(getTS().getUserAgArch() instanceof DefaultEmbeddedAgArch) 
					/* The architecture requres a list of devices to handle the perceptions. 
						   In some point after the agent creation, an architecture other than DefaultEmbeddedAgArch is set and the list of sensor is lost.
						   This method update the list of devices if it is null.
						   TODO: improve this */
					synchronized (ts) {
						if(((DefaultEmbeddedAgArch) getTS().getUserAgArch()).getDevices()==null) {
							((DefaultEmbeddedAgArch) getTS().getUserAgArch()).setDevices(devices);
						}
					}
			}

		}
	}
}
