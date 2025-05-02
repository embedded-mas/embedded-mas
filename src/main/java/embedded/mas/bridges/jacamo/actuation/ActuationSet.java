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

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.IDevice;



public class ActuationSet extends ArrayList<ActuationDevice>{

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
	 * The result is a hashtable where the key is the device and the value is a set of actuations
	 */
	public HashMap<IDevice, HashSet<ActuationDevice>> toActuationSetsByDevice(){
		HashMap<IDevice, HashSet<ActuationDevice>> result = new HashMap<IDevice, HashSet<ActuationDevice>>(); 
		for(ActuationDevice a:this) {
			if(result.get(a.getDevice())==null) result.put(a.getDevice(), new HashSet<ActuationDevice>());
			result.get(a.getDevice()).add(a);						
		}		
		return result;
	}

	
	
	
}
