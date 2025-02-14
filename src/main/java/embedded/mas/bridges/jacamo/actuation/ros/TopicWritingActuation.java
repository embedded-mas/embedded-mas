package embedded.mas.bridges.jacamo.actuation.ros;

import java.util.ArrayList;
import java.util.List;

import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import jason.asSyntax.Atom;

public class TopicWritingActuation extends DefaultActuation {

	private String topicName;
	private String topicType;

	public TopicWritingActuation(Atom id, String topicName, String topicType, Atom parameter) {
		super(id);
		this.topicName = topicName;
		this.topicType = topicType;
		if(parameter!=null)
			this.addParameter(parameter);
	}



	@Override
	public void addParameter(Atom parameter) {
		/* topic writing actuations admit a single parameter */
		if(this.getParameters()==null||this.getParameters().size()==0)
			super.addParameter(parameter);
		else
			this.getParameters().set(0, parameter);
	}


	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTopicType() {
		return topicType;
	}

	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}

	@Override
	public String toString() {
		return "TopicWritingActuation [id=" + getId() + ",topicName=" + topicName + ", topicType=" + topicType + ", parameters=" + getParameters() + "]";
	}



}
