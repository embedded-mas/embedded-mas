/**
 * 
 * An actuationSequence is a sequence of [sets of] actuations, that are supposed to be triggered in sequence.
 * 
 * The actuations within a set may be triggered in parallel, but a set of actuations is only triggered when the 
 * actuations if the precedent set have been all triggered.
 * 
 */

package embedded.mas.bridges.jacamo.actuation;

import java.util.ArrayList;

import jason.asSyntax.Term;

public class ActuationSequence {

	private ArrayList<ActuationSet> actuations = new ArrayList<ActuationSet>();

	public ArrayList<ActuationSet> getActuations() {
		return actuations;
	}

	public void addLast(ActuationSet actuationSet) {
		this.actuations.add(actuationSet);
	}


	public ActuationSet get(int position) {
		return this.actuations.get(position);
	}

	public int size() {
		return actuations.size();
	}



	@Override
	public String toString() {
		String s = "[";
		for(int i=0;i<this.getActuations().size();i++) {
			s = s + this.getActuations().get(i);
			if(i<this.getActuations().size()-1)
				s = s + ",";
		}
		s = s + "]";	

		return s;
	}


	/***
	 * Set the parameter of the actuations in the sequence.
	 * 
	 * TODO: raise exception when the parameter size is different from the expected
	 */
	public void setParameters(Term[] params){
		int index = 0;
		for(ActuationSet set : this.getActuations()) //for each actuation set in the sequence 
			for(ActuationDevice actuation : set) { //for each actuation in the set
				index = actuation.getActuation().setParamValues(params, index)+1;
			}

	}





}
