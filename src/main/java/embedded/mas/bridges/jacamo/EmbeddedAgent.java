package embedded.mas.bridges.jacamo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import embedded.mas.bridges.jacamo.config.DefaultConfig;
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
		setupDevices();
		super.initAg();
		if(new File( Paths.get("").toAbsolutePath()+"/src/main/java/"+getTS().getAgArch().getAgName() + ".yaml").exists()) {
			DefaultConfig conf = new DefaultConfig();
			List<DefaultDevice>  d =  conf.loadFromYaml(Paths.get("").toAbsolutePath()+"/src/main/java/"+getTS().getAgArch().getAgName() + ".yaml");
			devices.addAll(d);
			//for(int i=0;i<d.size();i++)
			//	System.out.println("----" + d.get(i).getId() + " - " + d.get(i).getClass().getName());
		}
		

		checkSensor c = new checkSensor();
		c.start();

		/*File f = new File( Paths.get("").toAbsolutePath()+"/src/main/java/"+getTS().getAgArch().getAgName() + ".yaml");
		try {
			System.out.println("**** arquivo " + f.getName() + " - " + f.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		

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

		@Override
		public void run() {			
			while(arch==null)  
				arch = getEmbeddedArch();			
			arch.setDevices(devices);
			return;
		}
	}
}
