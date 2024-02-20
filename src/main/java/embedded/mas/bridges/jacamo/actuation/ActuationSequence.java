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
	
	
	

}
