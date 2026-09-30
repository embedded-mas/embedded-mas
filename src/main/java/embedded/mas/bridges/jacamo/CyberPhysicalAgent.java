/**
 * 
 * CyberPhysicalAgent is an extension of Jason agent that is composed of physical devices composed of sensors and actuators. 
 * The reading of sensors is included in the perception process.  The actions enabled by the actuators become internal actions.
 * The setup of devices must be implemented in extending classes.
 */


package embedded.mas.bridges.jacamo;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import embedded.mas.bridges.jacamo.config.DefaultConfig;
import embedded.mas.exception.InvalidActuationException;
import embedded.mas.exception.InvalidActuatorException;
import embedded.mas.exception.InvalidDeviceException;
import jason.asSyntax.Literal;
import jason.asSyntax.Rule;


public class CyberPhysicalAgent extends EmbeddedAgent {



	protected void setupDevices() {
		if(new File( Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml").exists()) {
			DefaultConfig conf = new DefaultConfig();
			List<DefaultDevice>  d =  conf.loadFromYaml(Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml");
			this.getDevices().addAll(d);			
			try {
				this.actionMap = conf.getActions(d,(Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml"));
				Collection<Rule> perceptionRules = conf.getPerceptionRules((Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml"));
				checkArch c = new checkArch();
				c.setRules(perceptionRules);
				if(perceptionRules!=null)
					for(Literal s: perceptionRules)
						this.getBB().add(s);
			} catch (InvalidDeviceException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				e.printStackTrace();
			} catch (InvalidActuationException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				e.printStackTrace();
			} catch (InvalidActuatorException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				e.printStackTrace();
			}		
		}


	};

	/**
	 * Background worker that waits for the embedded architecture to become
	 * available, then installs the configured perception rules on it.
	 */
	class checkArch extends Thread{
		private Collection<Rule> perceptionRules;

		/** Stores the perception rules and starts this worker thread. */
		public void setRules(Collection<Rule> perceptionRules) {
			this.perceptionRules = perceptionRules;
			System.out.println();
			this.start();
		}

		/**
		 * Polls until the embedded architecture is available, pausing 10 ms
		 * between checks, then assigns the stored perception rules to it.
		 */
		@Override
		public void run() {		
			DefaultEmbeddedAgArch arch = null;	
			while(arch==null) {  
				arch = getEmbeddedArch();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			arch.setPerceptionRules(this.perceptionRules);
			return;
		}
	}

}
