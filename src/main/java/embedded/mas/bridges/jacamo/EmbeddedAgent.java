/**
 * A CyberPhysical agent with customized device setup
 */

package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import jason.architecture.AgArch;
import jason.asSemantics.Agent;
import jason.asSyntax.Atom;


public abstract class EmbeddedAgent extends Agent {

	private final List<DefaultDevice> devices = new ArrayList<DefaultDevice>();
	private DefaultEmbeddedAgArch arch = null;
	protected HashMap<Atom, ActuationSequence> actionMap = new HashMap<Atom, ActuationSequence>();


	@Override
	public void initAg() {		
		setupDevices();
		super.initAg(); 
		checkSensor c = new checkSensor();
		c.start();

	}

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
	
	
	public HashMap<Atom, ActuationSequence> getActionMap(){
		return this.actionMap;
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
			while(arch==null) {  
				arch = getEmbeddedArch();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			arch.setDevices(devices);
			return;
		}
	}
	
}
