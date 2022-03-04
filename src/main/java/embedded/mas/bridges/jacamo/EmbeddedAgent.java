package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.List;

import jason.architecture.AgArch;
import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;

/*
 * An extension of the default Jason agent that gets percepts from devices
 */

public abstract class EmbeddedAgent extends Agent {

	private final List<DefaultDevice> devices = new ArrayList<DefaultDevice>();
	private DefaultEmbeddedAgArch arch = null;


	@Override
	public void initAg() {
		setupSensors();
		super.initAg(); 
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
	
	
	private DefaultEmbeddedAgArch getEmbeddedArch() {
        AgArch arch = getTS().getAgArch().getFirstAgArch();
        while (arch != null) {
            if (arch instanceof DefaultEmbeddedAgArch) {
                return (DefaultEmbeddedAgArch)arch;
            }
            arch = arch.getNextAgArch();
        }
        return null;
    }


	class checkSensor extends Thread{

		@Override
		public void run() {			
			while(arch==null)  
				arch = getEmbeddedArch();			
			arch.setDevices(devices);
			return;
		}
	}
}
