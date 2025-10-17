/**
 * An abstract class that collects perceptions represented as JSON objects.
 * 
 * 
 */

package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

public abstract class JSONSensorValueCollector extends SensorValueCollector<Map.Entry<String, JsonValue>> {

	private Collection<JsonObject> perceptList;
	private MicrocontrollerSensorString2JsonMonitor monitor;
	
	public JSONSensorValueCollector(IExternalInterface microcontroller) {
		super(microcontroller);
		this.perceptList = new ArrayList<JsonObject>();
		this.monitor = new MicrocontrollerSensorString2JsonMonitor(perceptList, microcontroller); 
		this.monitor.start();
	}

	
	public JSONSensorValueCollector(IExternalInterface microcontroller, Collection<JsonObject> perceptList) {
		super(microcontroller);
		this.perceptList = perceptList;
		this.monitor = new MicrocontrollerSensorString2JsonMonitor(perceptList, microcontroller); 
		this.monitor.start();
	}
	
	protected final Collection<JsonObject> getPercptList() {
		return perceptList;
	}
	
	

	protected final void setPerceptList(Collection<JsonObject> perceptList) {
		this.perceptList = perceptList;
	}

	protected final MicrocontrollerSensorString2JsonMonitor getMonitor() {
		return monitor;
	}

	
	
}
