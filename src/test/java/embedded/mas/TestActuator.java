package embedded.mas;

import static org.junit.Assert.*;

import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.Actuator;

public class TestActuator {

	@Test
	public void testAddAndRemoveActuation() {
		Actuator actuator = new Actuator(createAtom("myActuator"));		
		assertTrue(actuator.getActuations().size()==0);
		
		Actuation act1 = new Actuation(createAtom("actuation1"));
		Actuation act2 = new Actuation(createAtom("actuation1"));
		
		actuator.addActuation(act1);
		assertTrue(actuator.getActuations().size()==1);
		
		actuator.addActuation(act2);
		assertTrue(actuator.getActuations().size()==2);
		
		actuator.removeActuation(act1);
		assertTrue(actuator.getActuations().size()==1);
		
		actuator.removeActuation(act2);
		assertTrue(actuator.getActuations().size()==0);
	}
	
	@Test 
	public void testHasActuation() {
		
		Actuator act = new Actuator(createAtom("myActuator"));

		
		assertFalse(act.hasActuation(createAtom("act1"))); //test if actuator set is empty
		
		Actuation actuation1 = new Actuation(createAtom("act1"));
		act.addActuation(actuation1);
		assertTrue(act.hasActuation(createAtom("act1")));
		
		assertFalse(act.hasActuation(createAtom("act2")));
		
		Actuation actuation2 = new Actuation(createAtom("act2"));
		act.addActuation(actuation2);
		assertTrue(act.hasActuation(createAtom("act2")));
		
	}
	
	@Test
	public void testGetActuationById() {		
		Actuator act = new Actuator(createAtom("myActuator"));
		
		assertNull(act.getActuationById(createAtom("act1")));
		
		Actuation actuation1 = new Actuation(createAtom("act1"));
		act.addActuation(actuation1);
		
		assertNotNull(act.getActuationById(createAtom("act1")));
		
	
		
	}

}
