package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import embedded.mas.bridges.jacamo.SensorCollector;

public class TestSensorCollector {

	@Test
	public void testConvert() {
		ArrayList<String> listOfBeliefs = new ArrayList<>();
		SensorCollector<String> monitor = new SensorCollector<>(listOfBeliefs, new SimulatedSerialPhysicalInterface());
		
		monitor.collect();
		monitor.collect();
		
		assertEquals(monitor.getCollectedValues().size(), 2);
		assertEquals(monitor.getCollectedValues().get(monitor.getCollectedValues().size()-2).toString(), "{\"x\":0,\"y\":0}");
		assertEquals(monitor.getCollectedValues().get(monitor.getCollectedValues().size()-1).toString(), "{\"x\":1,\"y\":2}");
		
	}

}
