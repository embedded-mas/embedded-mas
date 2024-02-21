package embedded.mas;

import static org.junit.Assert.*;

import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.Actuator;

public class TestActuator {

	@Test
	public void testAddAndRemoveActuator() {
		Actuator actuator = new Actuator(createAtom("myActuator"));
		
		assertTrue(actuator.getActuations().size()==0);
	}

}
