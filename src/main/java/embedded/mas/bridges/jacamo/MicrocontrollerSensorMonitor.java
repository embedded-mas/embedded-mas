/**
 * This class continously reads the sensor values of a microcotroller (defined in the constructor) and records it in a collection
 * 
 * 
 * 
 * @author maiquel
 *
 */

package embedded.mas.bridges.jacamo;

import java.util.Collection;
import java.util.List;

import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Literal;



public class MicrocontrollerSensorMonitor<T> extends Thread{

	protected Collection<T> perceptList;
	protected IExternalInterface microcontroller;

	public MicrocontrollerSensorMonitor(Collection<T> perceptList, IExternalInterface microcontroller) {
		super();
		this.perceptList = perceptList;
		this.microcontroller = microcontroller;
	}
	
	

	public Collection<T> getPerceptList() {
		return perceptList;
	}



	@Override
	public void run(){
		while(true) {
			perceptList.add(this.microcontroller.read());
			try {
				Thread.sleep((long)(100));
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}

		}
	}


}
