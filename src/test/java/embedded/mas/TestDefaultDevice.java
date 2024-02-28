package embedded.mas;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import static jason.asSyntax.ASSyntax.createAtom;

import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.EmbeddedAtomAction;
import embedded.mas.bridges.jacamo.actuation.Actuator;
import jason.asSemantics.Unifier;

public class TestDefaultDevice {

	@Test
	public void testAddEmbeddedAction() {
		DemoDevice device = new DemoDevice(createAtom("testDevice"));
		assertNull(device.getEmbeddedAction(createAtom("actionTest")));
		EmbeddedAtomAction action = new EmbeddedAtomAction(createAtom("actionTest"), createAtom("actionTest"));
		device.addEmbeddedAction(action);
		assertNotNull(device.getEmbeddedAction(createAtom("actionTest")));
	}

	@Test
	public void testRemoveEmbeddedAction() {
		DemoDevice device = new DemoDevice(createAtom("testDevice"));
		EmbeddedAtomAction action = new EmbeddedAtomAction(createAtom("actionTest"), createAtom("actionTest"));
		device.addEmbeddedAction(action);
	
		assertNotNull(device.getEmbeddedAction(createAtom("actionTest")));
		device.removeEmbeddedAction(action);
		assertNull(device.getEmbeddedAction(createAtom("actionTest")));
	}
	
	@Test
	public void testGetEmbeddedAction() {
		DemoDevice device = new DemoDevice(createAtom("testDevice"));
		assertNull(device.getEmbeddedAction(createAtom("actionTest")));
		EmbeddedAtomAction action = new EmbeddedAtomAction(createAtom("actionTest"), createAtom("actionTest"));
		device.addEmbeddedAction(action);
		assertNotNull(device.getEmbeddedAction(createAtom("actionTest")));
	}


	
	@Test
	public void testExecEmbeddedAction() {
		DemoDevice device = new DemoDevice(createAtom("testDevice"));
		EmbeddedAtomAction action = new EmbeddedAtomAction(createAtom("print"), createAtom("doPrint"));
		device.addEmbeddedAction(action);
		
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(out));
		
		device.execEmbeddedAction(createAtom("print"), new Object[] {Integer.valueOf(12345), "abcde"}, new Unifier());
				
		assertEquals("[doPrint]12345abcde", out.toString().trim());
		
		System.setOut(originalOut);
		
	}
	
	@Test
	public void testAddAndRemoveActuator() {
		DemoDevice device = new DemoDevice(createAtom("testDevice"));
		Actuator actuator1 = new Actuator(createAtom("act1"));
		Actuator actuator2 = new Actuator(createAtom("act2"));
		
		assertTrue(device.getActuators().size()==0);
		
		device.addActuator(actuator1);
		assertTrue(device.getActuators().size()==1);
		
		
		
		device.addActuator(actuator2);
		assertTrue(device.getActuators().size()==2);
		
		
		device.removeActuator(actuator1);
		assertTrue(device.getActuators().size()==1);
		
		device.removeActuator(actuator2);
		assertTrue(device.getActuators().size()==0);
	}

	
	@Test 
	public void testHasActuator() {
		DemoDevice device = new DemoDevice(createAtom("testDevice"));
		
		assertFalse(device.hasActuator(createAtom("act1"))); //test if actuator set is empty
		
		Actuator actuator1 = new Actuator(createAtom("act1"));
		device.addActuator(actuator1);
		assertTrue(device.hasActuator(createAtom("act1")));
		
		assertFalse(device.hasActuator(createAtom("act2")));
		
		Actuator actuator2 = new Actuator(createAtom("act2"));
		device.addActuator(actuator2);
		assertTrue(device.hasActuator(createAtom("act2")));
		
	}
	@Test
	public void testGetActuatorById() {		
		DemoDevice device = new DemoDevice(createAtom("testDevice"));
		
		assertNull(device.getActuatorById(createAtom("act1")));
		
		Actuator actuator1 = new Actuator(createAtom("act1"));
		device.addActuator(actuator1);
		
		assertNotNull(device.getActuatorById(createAtom("act1")));
		
	
		
	}
}
