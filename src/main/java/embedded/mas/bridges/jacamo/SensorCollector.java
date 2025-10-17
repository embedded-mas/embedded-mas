/**
 * Classes extending this abstract one access a microcontroller (defined in the constructor) and collect a set of values
 * from all the sensors available in the microcontroller. 
 * 
 */
package embedded.mas.bridges.jacamo;

import java.util.Collection;

public abstract class SensorCollector<T> {
	protected IExternalInterface microcontroller;

	public SensorCollector(IExternalInterface microcontroller) {
		super();
		this.microcontroller = microcontroller;
	}



	public IExternalInterface getMicrocontroller() {
		return microcontroller;
	}


	/**
	 * Collects the available perceptions in the sensors controlled by this.microcontroller.
	 * 
	 * @return  a collection of perceptions
	 */
	public abstract Collection<T> collect();
}
