package embedded.mas;

import static jason.asSyntax.ASSyntax.createAtom;
import static org.junit.Assert.*;

import org.junit.Test;

import embedded.mas.bridges.ros.RosMaster;

public class TestRosMaster {



	@Test
	public void testExecEmbeddedActionAtomObjectArrayUnifier_checkArrayConsistency() {
		String s = "test";
		Object[] v = new Object[] {Integer.valueOf(3),Integer.valueOf(4),Integer.valueOf(5),Integer.valueOf(70), s};
		Object[] o = new Object[] {v};
		RosMaster master = new RosMaster(createAtom("ros1"), null);
		assertFalse(master.execEmbeddedAction(createAtom("test"), o, null));
	}

	@Test
	public void testExecEmbeddedActionStringObjectArrayTermUnifier_checkArrayConsistency() {
		String s = "test";
		Object[] v = new Object[] {Integer.valueOf(3),Integer.valueOf(4),Integer.valueOf(5),Integer.valueOf(70), s};
		Object[] o = new Object[] {v};
	
		RosMaster master = new RosMaster(createAtom("ros1"), null);
		
		
		try {
			master.execEmbeddedAction("test", o, null, null);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Array arguments require all elements of the same type.");
		}
	}

}
