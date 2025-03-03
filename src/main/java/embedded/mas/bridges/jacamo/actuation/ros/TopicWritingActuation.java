package embedded.mas.bridges.jacamo.actuation.ros;

import java.util.HashMap;

import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class TopicWritingActuation extends DefaultActuation<ServiceParameters> {

	private String topicName;
	private String topicType;

	public TopicWritingActuation(Atom id, String topicName, String topicType, ServiceParameters parameter) {
		super(id);
		this.topicName = topicName;
		this.topicType = topicType;
		this.setParameters(parameter);
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
		return this.getParameters().paramCount();
	}


	@Override
	public DefaultActuation<ServiceParameters> clone() {
		return new TopicWritingActuation(this.getId(), this.topicName, this.topicType, this.getParameters());
	}


	@Override
	public Term[] getParametersAsArray() {	
		Term[] t = new Term[this.getParameters().paramCount()];
		internal_getNestedParametersAsArray(this.getParameters(), this.getDefaultParameterValues(), t, 0);		
		return t;


	}

	private int internal_getNestedParametersAsArray(ServiceParameters sp, HashMap<String, Object> dp, Term [] array, int index) {
		if(dp==null) { //if there is not default parameters
			for(int j=0;j<array.length;j++) 
				array[index++]=null;
			return index;
		}
		for(ServiceParam p : sp) { //for each parameter
			if(p.getParamValue() instanceof ServiceParameters){//if the current parameter is a nested serviceParameters
				if(dp.get(p.getParamName())!=null && dp.get(p.getParamName()) instanceof HashMap<?, ?>)//if the default parameters hashmap has a key corresponding to the current parameter
					index =  internal_getNestedParametersAsArray((ServiceParameters)p.getParamValue(), (HashMap<String, Object>) dp.get(p.getParamName()), array, index); //recursively checks the nested parameters
			}
			else //default case: it is not a nested parameters case
				if(dp.get(p.getParamName())!=null) {
					if(dp.get(p.getParamName()) instanceof Number)
						array[index++] = new NumberTermImpl(dp.get(p.getParamName()).toString());
					else
						array[index++] = (Term) dp.get(p.getParamName());
				}
				else {
					array[index++] = null;
				}
		}

		return index;

	}





}
