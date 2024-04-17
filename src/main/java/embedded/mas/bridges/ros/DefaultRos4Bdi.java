package embedded.mas.bridges.ros;

import java.util.ArrayList;

public class DefaultRos4BdiAgent extends DefaultRos4EmbeddedMas {

	public DefaultRos4BdiAgent(String connectionStr, ArrayList<String> topics, ArrayList<String> types,
			ArrayList<String> beliefNames) {
		super(connectionStr, topics, types, beliefNames);
		// TODO Auto-generated constructor stub
	}

	public DefaultRos4BdiAgent(String connectionStr, ArrayList<String> topics, ArrayList<String> types) {
		super(connectionStr, topics, types);
		// TODO Auto-generated constructor stub
	}

}
