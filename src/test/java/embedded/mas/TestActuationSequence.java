package embedded.mas;

import static org.junit.Assert.*;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestActuationSequence {

	@Test
	public void testaddFirst() {
		
		DemoDevice d1 = new DemoDevice(createAtom("myDevice1"));		
		
		ActuationDevice a11 = new ActuationDevice(d1, new Actuator(createAtom("a11")), new Actuation(createAtom("actuation11")));
		ActuationDevice a12 = new ActuationDevice(d1, new Actuator(createAtom("a12")), new Actuation(createAtom("actuation12")));
		
		ActuationDevice a21 = new ActuationDevice(d1, new Actuator(createAtom("a21")), new Actuation(createAtom("actuation21")));
		ActuationDevice a22 = new ActuationDevice(d1, new Actuator(createAtom("a22")), new Actuation(createAtom("actuation22")));
		
		ActuationSet aset1 = new ActuationSet();
		aset1.add(a11);
		aset1.add(a21);
		System.out.println(aset1.toString());
		
		ActuationSet aset2 = new ActuationSet();
		aset2.add(a12);
		aset2.add(a22);
		System.out.println(aset2.toString());
		
		
		
		ActuationSequence aseq = new ActuationSequence();
		assertTrue(aseq.size()==0);
		
		aseq.addLast(aset1);
		assertTrue(aseq.size()==1);
		assertTrue(aseq.get(aseq.size()-1).size()==2);
		assertTrue(aseq.get(aseq.size()-1).contains(a11));
		assertTrue(aseq.get(aseq.size()-1).contains(a21));
		
		aseq.addLast(aset2);
		assertTrue(aseq.size()==2);
		assertTrue(aseq.get(aseq.size()-1).contains(a12));
		assertTrue(aseq.get(aseq.size()-1).contains(a22));
		
	
		
	}

}
