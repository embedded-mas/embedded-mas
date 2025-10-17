package embedded.mas.bridges.jacamo;

import java.util.Collection;

import embedded.mas.bridges.jacamo.IDevice;
import embedded.mas.bridges.jacamo.IExternalInterface;
import embedded.mas.bridges.jacamo.IPhysicalInterface;
import jason.asSyntax.Literal;

public abstract class SensorValueCollector<T> {

	private IExternalInterface microcontroller;
		
	
	public SensorValueCollector(IExternalInterface microcontroller) {
		super();
		this.microcontroller = microcontroller;
	}


	
	
	public IExternalInterface getMicrocontroller() {
		return microcontroller;
	}




	public abstract Collection<T> collect(); //collect from the microcontroller
	
	
	
}
