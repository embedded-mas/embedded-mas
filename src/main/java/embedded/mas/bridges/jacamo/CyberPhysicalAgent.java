/**
 * 
 * CyberPhysicalAgent is an extension of Jason agent that is composed of physical devices composed of sensors and actuators. 
 * The reading of sensors is included in the perception process.  The actions enabled by the actuators become internal actions.
 * The setup of devices must be implemented in extending classes.
 */


package embedded.mas.bridges.jacamo;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.config.DefaultConfig;
import jason.asSyntax.Atom;


public class CyberPhysicalAgent extends EmbeddedAgent {



	protected void setupDevices() {
		System.out.println("[CyberPhysicalAgent] Starting...");
		if(new File( Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml").exists()) {
			DefaultConfig conf = new DefaultConfig();
			List<DefaultDevice>  d =  conf.loadFromYaml(Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml");
			this.getDevices().addAll(d);
			
			this.actionMap = conf.getActions(d,(Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml"));
			for(Map.Entry<Atom, ActuationSequence> entry: actionMap.entrySet())
				System.out.println(entry.getKey() + " +++  " + entry.getValue());
		}


	};

}