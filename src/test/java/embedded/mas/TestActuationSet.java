package embedded.mas;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.IDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;
import embedded.mas.bridges.jacamo.actuation.Actuation;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestActuationSet {

	@Test
	public void testToActuationSets() {
		ActuationSet set = new ActuationSet();
		
		DefaultDevice d1 = new DemoDevice(createAtom("d1"));
		Actuator a1 = new Actuator(createAtom("a1"));
		Actuation act1 = new Actuation(createAtom("act1"));
		ActuationDevice ad1 = new ActuationDevice(d1, a1, act1);
		
		DefaultDevice d2 = new DemoDevice(createAtom("d2"));
		Actuator a2 = new Actuator(createAtom("a2"));
		Actuation act2 = new Actuation(createAtom("act2"));
		ActuationDevice ad2 = new ActuationDevice(d2, a2, act2);
		
		Actuator a3 = new Actuator(createAtom("a3"));
		Actuation act3 = new Actuation(createAtom("act3"));
		ActuationDevice ad3 = new ActuationDevice(d2, a3, act3);
		
		set.add(ad1);
		set.add(ad2);
		set.add(ad3);
		
		
		HashMap<IDevice, HashSet<ActuationDevice>> subsets = set.toActuationSetsByDevice(); 
		//System.out.println(subsets);
		
		assertEquals(subsets.get(d1).size(),1);
		assertEquals(subsets.get(d2).size(),2);
		assertTrue(subsets.get(d1).contains(ad1));
		assertTrue(subsets.get(d2).contains(ad2));
		assertTrue(subsets.get(d2).contains(ad3));
		
	}

}
