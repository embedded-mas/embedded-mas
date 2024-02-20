package embedded.mas;

import static org.junit.Assert.*;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestActuationSequence {

	@Test
	public void testaddFirst() {
		
		DemoDevice d1 = new DemoDevice(createAtom("myDevice1"));
		DemoDevice d2 = new DemoDevice(createAtom("myDevice2"));
		
		Actuation a11 = new Actuation(d1, createAtom("a11"));
		Actuation a12 = new Actuation(d1, createAtom("a12"));
		
		Actuation a21 = new Actuation(d1, createAtom("a21"));
		Actuation a22 = new Actuation(d1, createAtom("a22"));
		
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
