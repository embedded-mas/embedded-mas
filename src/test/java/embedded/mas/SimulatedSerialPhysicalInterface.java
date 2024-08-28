/*
 * This class simulates a physical interface (e.g. Arduino4EmbeddedMas).
 * It produces "perceptions" in the form {"x": vx, "y":vy} s.t.
 *   (i) vx increments each simulated reading
 *  (ii) vy = 2*vx  
 * 
 */
package embedded.mas;

import javax.json.Json;
import javax.json.JsonObject;

import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IPhysicalInterface;

public class SimulatedSerialPhysicalInterface implements IPhysicalInterface {
	
	private int x=0; 

	@Override
	public void execEmbeddedAction(EmbeddedAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String read() {
		 JsonObject jsonObject = Json.createObjectBuilder()
		            .add("x", x)
		            .add("y", x++*2)
		            .build();
		 
		        
		return jsonObject.toString();
	}

	@Override
	public boolean write(String s) {
		// TODO Auto-generated method stub
		return false;
	}

}
