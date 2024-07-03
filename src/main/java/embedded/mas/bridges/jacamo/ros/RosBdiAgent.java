package embedded.mas.bridges.jacamo.ros;

import embedded.mas.bridges.jacamo.CyberPhysicalAgent;
import embedded.mas.bridges.ros.RosMaster;

public class RosBdiAgent extends CyberPhysicalAgent {
	
	

	public RosBdiAgent() {
		super();
		System.out.println("**** ROS BDI AGENT ***");
	}

	public void addRosMaster(RosMaster rosMaster) {
		//this.getDevices().clear(); 
		this.addDevice(rosMaster);
	}
	
}
