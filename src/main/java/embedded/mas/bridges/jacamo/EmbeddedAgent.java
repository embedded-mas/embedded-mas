/**
 * A CyberPhysical agent with customized device setup
 */

package embedded.mas.bridges.jacamo;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import embedded.mas.bridges.jacamo.config.DefaultConfig;

public class EmbeddedAgent extends CyberPhysicalAgent {
	


	/**
	 * Set up the devices of the agent
	 */
	protected void setupDevices() {
		System.out.println("[EmbeddedAgent] iniciou setup defices " );
		if(new File( Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml").exists()) {
			DefaultConfig conf = new DefaultConfig();
			List<DefaultDevice>  d =  conf.loadFromYaml(Paths.get("").toAbsolutePath()+"/src/agt/"+getTS().getAgArch().getAgName() + ".yaml");
			this.getDevices().addAll(d);
			System.out.println("[EmbeddedAgent] encontrou devices  " + d.size() );
		}else System.out.println("[EmbeddedAgent] não encontrou arquivos de configuração " );
						
		for(DefaultDevice d: this.getDevices())
			System.out.println("[EmbeddedAgent] device " + d.getId() + " - " + d.getClass().getName());
		
	};

 
}
