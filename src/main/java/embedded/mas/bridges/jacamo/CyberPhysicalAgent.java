/**
 * 
 * CyberPhysicalAgent is an extension of Jason agent that is composed of physical devices composed of sensors and actuators. 
 * The reading of sensors is included in the perception process.  The actions enabled by the actuators become internal actions.
 * The setup of devices must be implemented in extending classes.
 */


package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.List;

import jason.architecture.AgArch;
import jason.asSemantics.Agent;

public abstract class CyberPhysicalAgent extends Agent {

	private final List<DefaultDevice> devices = new ArrayList<DefaultDevice>();
	private DefaultEmbeddedAgArch arch = null;


	@Override
	public void initAg() {		
		setupDevices();
		super.initAg(); 
		checkSensor c = new checkSensor();
		c.start();

	}

	/* remove a belief according to the functor and source of the parameter*/
	/*public void abolishByFunctorAndSource(Literal bel, Agent ag) {
		bel = (Literal) bel.clone();
		bel.makeTermsAnnon(); //change all the terms anonimous variables ("_")
		Literal belFound = ag.findBel(bel, new Unifier()); //find the belief by the functor		
		if(belFound!=null) {
			belFound = (Literal) belFound.clone(); //clone to 
			belFound.delSources(); //del all the sources (the same belief may be many sources) to add the only interesting source (below)
			belFound.addSource(bel.getSources().get(0));
			ag.getBB().remove(belFound);
		}
	}*/

	protected abstract void setupDevices();
	
    @Deprecated
	public void addSensor(DefaultDevice device) {
		this.addDevice(device);
	}

    @Deprecated
	public void removeSensor(DefaultDevice device) {
		this.removeDevice(device);
	}

	
	public void addDevice(DefaultDevice device) {
		devices.add(device);
	}

	public void removeDevice(DefaultDevice device) {
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
		/*
		 * Wait until the architecture is set up to put the devices there. The architecture uses these artifacts to get the perceptions. 
		 */
		
		@Override
		public void run() {			
			while(arch==null)  
				arch = getEmbeddedArch();			
			arch.setDevices(devices);
			return;
		}
	}
	
}
