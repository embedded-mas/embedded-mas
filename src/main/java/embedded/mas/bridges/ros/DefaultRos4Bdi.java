package embedded.mas.bridges.ros;

import java.util.ArrayList;
import java.util.HashMap;

import embedded.mas.bridges.jacamo.EmbeddedAction;

public class DefaultRos4Bdi extends DefaultRos4EmbeddedMas implements IRosInterface{

	public DefaultRos4Bdi(String connectionStr, ArrayList<String> topics, ArrayList<String> types,
			ArrayList<String> beliefNames) {
		super(connectionStr, topics, types, beliefNames);
		// TODO Auto-generated constructor stub
	}
	
	public DefaultRos4Bdi(String connectionStr, ArrayList<String> topics, ArrayList<String> types,
			ArrayList<String> beliefNames, HashMap<String, ArrayList<String>> paramsToIgnore) {
		super(connectionStr, topics, types, beliefNames, paramsToIgnore);
		// TODO Auto-generated constructor stub
	}
	
	

	public DefaultRos4Bdi(String connectionStr, ArrayList<String> topics, ArrayList<String> types) {
		super(connectionStr, topics, types);
		// TODO Auto-generated constructor stub
	}
	
	public void execRosAction(EmbeddedAction action) {
		super.execEmbeddedAction(action);
	}

}
