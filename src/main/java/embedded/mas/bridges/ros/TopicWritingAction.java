package embedded.mas.bridges.ros;

import jason.asSyntax.Atom;

public class TopicWritingAction extends RosAction {

	/* The field "value" is used for topics that have a single, simple parameter  (e.g an integer value)
	 * The field "parameters" is used in the case of structured parameters (that are converted to JSON in the rosbridge)
	 * */

	private String topicName;
	private String topicType;
	private Object value;
	private ServiceParameters parameters;


	public TopicWritingAction(Atom actionName, String topicName, String topicType, Object value) {
		super();
		this.setActionName(actionName);
		this.topicName = topicName;
		this.topicType = topicType;
		this.value = value;
		this.setParameters(null);
	}


	public TopicWritingAction(Atom actionName, String topicName, String topicType, Object value, ServiceParameters parameters) {
		this(actionName,topicName,topicType,value);
		this.value = null;		
		if(parameters.size()==0) 
			this.setParameters(null);
		else
			this.setParameters(parameters);
	}


	public String getTopicName() {
		return topicName;
	}


	public String getTopicType() {
		return topicType;
	}


	public void setValue(Object value) {
		if(this.parameters!=null) { //if this action uses the ServiceParameters class
			this.parameters.setValues((Object[])value);
		}
		else
			if(value.getClass().isArray()) //if it is an array of parameters, take the first element (old implementation style)
				this.value = ((Object[])value)[0];
			else
				this.value = value;
	}

	public Object getValue() {
		if(this.parameters!=null) return getParameters().toJson();
		return value;
	}


	public ServiceParameters getParameters() {
		return parameters;
	}


	public void setParameters(ServiceParameters parameters) {
		this.parameters = parameters;
	}


	@Override
	public String toString() {
		if(parameters==null)
			return "TopicWritingAction [topicName=" + topicName + ", topicType=" + topicType + ", value=" + value
					+ ", parameters={}]";
		else
			return "TopicWritingAction [topicName=" + topicName + ", topicType=" + topicType + ", value=" + value
					+ ", parameters=" + parameters.toJson() + "]";
	}





}
