/***
 * An ActuationSet implements a set of actuations (i.e.actions executed by an actuator).
 * All the actuations in a set are supposed to be triggered simultaneously. It it is not possible due to limitations in
 * the hardware, they can be triggered in some sequence. 
 * 
 * As it is a set, duplicated actuations are not allowed. If an actuation is supposed to be triggered twice, 
 * it must be triggered in a sequence.   
 *  
 */

package embedded.mas.bridges.jacamo.actuation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.IDevice;
import jason.asSyntax.Atom;



public class ActuationSet extends ArrayList<ActuationDevice>{


//	private Map<Integer, Atom> parameterMapping = new HashMap<>(); //for a pair (k,v), maps the k-th actuation to an action parameter identified by "v"

	@Override
	public boolean add(ActuationDevice e) {
		if(this.hasActuation(e)!=-1) return false;
		return super.add(e);
	}


	@Override
	public void add(int index, ActuationDevice e) {
		if(this.hasActuation(e)==-1) 
			super.add(index, e);
	}


	private int hasActuation(ActuationDevice e) {
		return this.hasActuation(e.getDevice(), e.getActuator(), e.getActuation());
	}

	private int hasActuation(DefaultDevice device, Actuator actuator, DefaultActuation actuation) {
		//for(ActuationDevice a : this)
		for(int i=0;i<this.size();i++)
			if(this.get(i).getDevice().getId().toString().equals(device.getId().toString()) &&
					this.get(i).getActuator().getId().toString().equals(actuator.getId().toString()) &&
					this.get(i).getActuation().getId().toString().equals(actuation.getId().toString()))
				return i;
		return -1;
	}


	@Override
	public String toString() {
		String s = "{";
		Iterator<ActuationDevice> it = this.iterator();
		while(it.hasNext()) {
			s = s.concat(it.next().toString());
			if(it.hasNext()) s = s.concat(",");
		}
		s = s.concat("}");
		return s;
	}
	
	
	/*
	 * Divide the set in subsets, group by device.
	 * The result is a hashtable where the key is the device and the value is a set of actuations.
	 * 
	 * The set of actuations is recorded in an ArrayList as it is faster than objects implementing the Set interface.
	 * 
	 */
	public HashMap<IDevice, ArrayList<ActuationDevice>> toActuationSetsByDevice(){
		HashMap<IDevice, ArrayList<ActuationDevice>> result = new HashMap<IDevice, ArrayList<ActuationDevice>>(); 
		for(ActuationDevice a:this) {
			if(result.get(a.getDevice())==null) { 
				result.put(a.getDevice(), new ArrayList<ActuationDevice>());				
			}
			result.get(a.getDevice()).add(a);						
		}		
		return result;
	}

	
	/**
	 * Map the *actuationParameter* (which the is a parameter of the *actuation* of the *actuator* available in a *device*) 
	 * to the *actionParameter*, which is the identifier of an action parameter (from the agent's repertory 
	 * 
	 * @param actionParameter
	 * @param device
	 * @param actuator
	 * @param actuation
	 * @param actuationParameter
	 * @return
	 */
	public int setParameterMapping(DefaultDevice device, Actuator actuator, DefaultActuation actuation, Atom actuationParameter,Atom actionParameter){
		int actIndex =  hasActuation(device, actuator, actuation);
		if(actIndex!=-1)
			this.get(actIndex).getActuation().setParamActionMapping(actuationParameter, actionParameter);
		return actIndex;
					
	}

}
