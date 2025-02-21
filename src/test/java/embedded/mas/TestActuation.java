package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import jason.asSyntax.Atom;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.Actuation;

public class TestActuation {

	@Test
	public void testGetParametersAsArray() {
		ArrayList<Atom> params = new ArrayList<Atom>();
		params.add(createAtom("p1"));
		params.add(createAtom("p2"));
		params.add(createAtom("p3"));
		Actuation actuation = new Actuation(createAtom("act"), params); 
		
		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("p2", 5);
		actuation.setDefaultParameterValues(defaultParams);		

		
		assertNull(actuation.getParametersAsArray()[0]);
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertNull(actuation.getParametersAsArray()[2]);
	}

}
