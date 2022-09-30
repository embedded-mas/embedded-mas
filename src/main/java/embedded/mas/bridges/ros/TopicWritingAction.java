package embedded.mas.bridges.ros;

import jason.asSyntax.Atom;

public class TopicWritingAction extends RosAction {
	
	private String topicName;
	private String topicType;
	private Object value;
	

	public TopicWritingAction(Atom actionName, String topicName, String topicType, Object value) {
		super();
		this.setActionName(actionName);
		this.topicName = topicName;
		this.topicType = topicType;
		this.value = value;
	}

	

	public String getTopicName() {
		return topicName;
	}


	public String getTopicType() {
		return topicType;
	}


	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}





}
