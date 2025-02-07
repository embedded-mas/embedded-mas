package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import embedded.mas.bridges.jacamo.JSONQueueWatcherDevice;
import embedded.mas.exception.PerceivingException;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestJSONQueueWatcherDevice {
	
	
	@Test
	public void testGetPercepts() {		
		SimulatedSerialPhysicalInterface physicalInterface = new SimulatedSerialPhysicalInterface();
		List<Collection<Literal>> beliefs = new ArrayList<>();
		JSONQueueWatcherDevice device = new JSONQueueWatcherDevice(createAtom("deviceTest"), physicalInterface);
		Collection<Literal> percepts = null;
		
		//check whether the device gets the first percept		
		int attemtps=0;
		do {
			try {
				percepts = device.getPercepts();
			} catch (PerceivingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(attemtps++<1000 & (percepts==null || percepts.size()==0));
		assertEquals(percepts.toString(), "[x(0), y(0)]");
		
		
		//check whether the device gets the second percept
		attemtps=0;
		do {
			try {
				percepts = device.getPercepts();
			} catch (PerceivingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(attemtps++<1000 & (percepts==null || percepts.size()==0));				
		assertEquals(percepts.toString(), "[x(1), y(2)]");
		
	}

}
