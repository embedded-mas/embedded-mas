package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
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

		//System.out.println(actuation.toString());
		
		assertNull(actuation.getParametersAsArray()[0]);
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertNull(actuation.getParametersAsArray()[2]);
	}

	
	@Test
	public void testSetParametersToDefaultState() {
		ArrayList<Atom> params = new ArrayList<Atom>();
		params.add(createAtom("p1"));
		params.add(createAtom("p2"));
		params.add(createAtom("p3"));
		params.add(createAtom("p4"));
		Actuation actuation = new Actuation(createAtom("act"), params); 
		
		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("p2", 5);
		actuation.setDefaultParameterValues(defaultParams);		

		
		//test whether the initial values are correct
		assertNull(actuation.getParametersAsArray()[0]);
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertNull(actuation.getParametersAsArray()[2]);
		assertNull(actuation.getParametersAsArray()[3]);
		
		actuation.setParameterValue(createAtom("p1"), createAtom("a"));
		actuation.setParameterValue(createAtom("p2"), createAtom("b")); //this assignment cannot change the defalut value
		actuation.setParameterValue(createAtom("p3"), createAtom("c"));
		
		//test whether the param values are correctly assigned
		assertEquals(actuation.getParametersAsArray()[0].toString(), "a");
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertEquals(actuation.getParametersAsArray()[2].toString(), "c");
		assertNull(actuation.getParametersAsArray()[3]);
		
		
		actuation.setParametersToDefaultState();
		
		
		//test whether the values back to the default values
		assertNull(actuation.getParametersAsArray()[0]);
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertNull(actuation.getParametersAsArray()[2]);
		assertNull(actuation.getParametersAsArray()[3]);
		
	}
	
	@Test
	public void test_setParamValues() {
		/* parameters: [p1, p2/5, p3] (p2 has a default value)
		 * parameter values: [ 11, 22, 33, 44, 55, 66]
		 * initial position: 2
		 * 
		 * Expected result: 
		 *    p1 has the value 33 (position 2 of parameter values)
		 *    p2 has the value 5 (default)
		 *    p3 has the value 44 (position 3 of parameter values)
		 * 
		 *Expected return: 3 
		 */
		
		ArrayList<Atom> params = new ArrayList<Atom>();
		params.add(createAtom("p1"));
		params.add(createAtom("p2"));
		params.add(createAtom("p3"));
		Actuation actuation = new Actuation(createAtom("act"), params); 
		
		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("p2", 5);
		actuation.setDefaultParameterValues(defaultParams);		
		
		Term[] p = new Term[6];
		p[0] = new NumberTermImpl(11);
		p[1] = new NumberTermImpl(22);
		p[2] = new NumberTermImpl(33);
		p[3] = new NumberTermImpl(44);
		p[4] = new NumberTermImpl(55);
		p[5] = new NumberTermImpl(66);
		
		
		
		
		assertEquals(actuation.setParamValues(p, 2),3);
		assertEquals(actuation.getParameterValue(createAtom("p1")).toString(), "33");
		assertEquals(actuation.getParameterValue(createAtom("p2")), 5);
		assertEquals(actuation.getParameterValue(createAtom("p3")).toString(), "44");
		
	}

}
