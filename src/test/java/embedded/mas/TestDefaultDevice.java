package embedded.mas;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import static jason.asSyntax.ASSyntax.createAtom;

import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.EmbeddedAtomAction;
import embedded.mas.bridges.jacamo.LiteralDevice;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;

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
}
