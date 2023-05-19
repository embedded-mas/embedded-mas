import java.util.Collection;

import embedded.mas.bridges.jacamo.JSONWatcherDevice;
import embedded.mas.bridges.jacamo.IPhysicalInterface;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;


/**
 * This device enables two actions: lightA and lightB, that are meant to turn on different lights in the physical device.
 * 
 * The actions lightA and lightB send to the microcontroller the messages "light_a" and "light_b". The microcontroller is expected to properly handle them,
 * turning on the light. A or B accordingly.
 * 
 * The names of the actions (lightA and lightB) are different from the messages sent to microcontroller ("light_a" and "light_b") to illustrate the 
 * decoupling between the agent level (the actions) and the physical level (the handling of messages). Though actions names and messages could be equal.
 *
 */

public class MyDemoDevice extends JSONWatcherDevice {

	public MyDemoDevice(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);
	}

	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args) {
		if(actionName.equals("lightOn")) 
			return doLightOn();
		else 
                   return false;
	}
	
	public boolean doLightOn() {
		this.getMicrocontroller().write("light_on");
		return true;
	}

	
}
