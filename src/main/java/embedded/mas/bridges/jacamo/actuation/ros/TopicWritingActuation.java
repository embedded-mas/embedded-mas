package embedded.mas.bridges.jacamo.actuation.ros;

import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class TopicWritingActuation extends DefaultActuation<Atom> {

	private String topicName;
	private String topicType;

	public TopicWritingActuation(Atom id, String topicName, String topicType, Atom parameter) {
		super(id);
		this.topicName = topicName;
		this.topicType = topicType;
		this.setParameters(parameter);
	}

	
	public void setParameter(Atom parameter) {
		this.setParameter(parameter);
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


	@Override
	public int parameterSize() {
		return 1;
	}


	@Override
	public DefaultActuation<Atom> clone() {
		return new TopicWritingActuation(this.getId(), this.topicName, this.topicType, this.getParameters());
	}


	@Override
	public Term[] getParametersAsArray() {	
		Term[] params = new Term[1];
		if(this.getDefaultParameterValues()==null)
			params[0] = null;
		Object defaultValue = this.getDefaultParameterValues().get(this.getParameters().toString());
		if(defaultValue instanceof Integer)
			params[0] = new NumberTermImpl(this.getDefaultParameterValues().get(this.getParameters().toString()).toString());
		else
		params[0] = (Term)this.getDefaultParameterValues().get(this.getParameters().toString());		
		return params;
	}



}
