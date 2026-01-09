package embedded.mas.bridges.jacamo.actuation.ros;

import java.util.HashMap;
import java.util.Map;

import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;

public class TopicWritingActuation extends ROSActuation {

	private String topicName;
	private String topicType;

	public TopicWritingActuation(Atom id, String topicName, String topicType, ServiceParameters parameter) {
		super(id,parameter);
		this.topicName = topicName;
		this.topicType = topicType;
//		this.setParameters(parameter);
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
		return "TopicWritingActuation [id=" + getId() + ",topicName=" + topicName + ", topicType=" + topicType + ", parameters=" + getParameters() + ", param mapping: " + this.getParamMapping() +"]";
	}


//	@Override
//	public int parameterSize() {
//		return this.getParameters().paramCount();
//	}
//

	@Override
	public DefaultActuation<ServiceParameters> clone() {
		return new TopicWritingActuation(this.getId(), this.topicName, this.topicType, this.getParameters().clone());
	}

	@Override
	public int setParamValues(Object[] p, int initialPosition) {		
		return this.getParameters().setParamValues(p, initialPosition);
	}







}
