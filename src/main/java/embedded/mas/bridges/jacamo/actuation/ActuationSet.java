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

import java.util.HashSet;
import java.util.Iterator;



public class ActuationSet extends HashSet<Actuation>{

	@Override
	public String toString() {
		String s = "{";
		Iterator<Actuation> it = this.iterator();
		while(it.hasNext()) {
			s = s.concat(it.next().toString());
			if(it.hasNext()) s = s.concat(",");
		}
		s = s.concat("}");
		return s;
	}

	
	
	
}
