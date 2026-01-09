package embedded.mas;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.ros.ServiceRequestActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestServiceRequestActuation {

	@Test
	public void test() {
		ServiceParameters parameters = new ServiceParameters();
		ServiceParam p1 = new ServiceParam("p1", null); parameters.add(p1);
		ServiceParam p2 = new ServiceParam("p2", null); parameters.add(p2);
		ServiceParam p3 = new ServiceParam("p3", null); parameters.add(p3);

		ServiceRequestActuation actuation = new ServiceRequestActuation(createAtom("act"), "serviceName", parameters);

		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("p2", 5);
		actuation.setDefaultParameterValues(defaultParams);

		assertNull(actuation.getParametersAsArray()[0]);
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertNull(actuation.getParametersAsArray()[2]);
	}


	/**
	 * Test case: check whether parameters roll back to the default state (i.e. non default values are null)
	 */
	@Test
	public void testSetParametersToDefaultState() {

		ServiceParameters parameters = new ServiceParameters();
		ServiceParam p1 = new ServiceParam("p1", null); parameters.add(p1);
		ServiceParam p2 = new ServiceParam("p2", null); parameters.add(p2);
		ServiceParam p3 = new ServiceParam("p3", null); parameters.add(p3);

		ServiceRequestActuation actuation = new ServiceRequestActuation(createAtom("act"), "serviceName", parameters);

		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("p2", 5);
		actuation.setDefaultParameterValues(defaultParams);


		actuation.getParameters().getServiceParamByName("p1").setParamValue(1);
		actuation.getParameters().getServiceParamByName("p2").setParamValue(2);
		actuation.getParameters().getServiceParamByName("p3").setParamValue(3);

		//test whether param values are corrected assigned
		assertEquals(actuation.getParametersAsArray()[0].toString(), "1");
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertEquals(actuation.getParametersAsArray()[2].toString(), "3");

		//test whether parameter values are correctly rolled back
		actuation.setParametersToDefaultState();
		assertNull(actuation.getParametersAsArray()[0]);
		assertEquals(actuation.getParametersAsArray()[1].toString(), "5");
		assertNull(actuation.getParametersAsArray()[2]);

	}


	/**
	 *       array of parameters: [1,2,3,4,5,6,7,8,9]
	 *       initial position: 4
	 *       expected result: 6 (i.e., the considered portion of the vector is in positions [3..6])
	 *       expected parameter values:
	 *         p1: 5, p2: 99, p3: 6, p4: 7         
	 */
	@Test
	public void test_setParamValues() {

		ServiceParameters parameters = new ServiceParameters();
		ServiceParam p1 = new ServiceParam("p1", null); parameters.add(p1);
		ServiceParam p2 = new ServiceParam("p2", null); parameters.add(p2);
		ServiceParam p3 = new ServiceParam("p3", null); parameters.add(p3);
		ServiceParam p4 = new ServiceParam("p4", null); parameters.add(p4);

		ServiceRequestActuation actuation = new ServiceRequestActuation(createAtom("act"), "serviceName", parameters);

		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("p2", 99);
		actuation.setDefaultParameterValues(defaultParams);



		Term[] t = new Term[15];
		t[0] = new NumberTermImpl(1); 
		t[1] = new NumberTermImpl(2); 
		t[2] = new NumberTermImpl(3); 
		t[3] = new NumberTermImpl(4); 
		t[4] = new NumberTermImpl(5); 
		t[5] = new NumberTermImpl(6); 
		t[6] = new NumberTermImpl(7); 
		t[7] = new NumberTermImpl(8); 
		t[8] = new NumberTermImpl(9); 

		assertEquals(actuation.setParamValues(t, 4),6);
		
		
		Term[] p = actuation.getParametersAsArray();
		//check correct assignment of  parameters
		assertEquals(p[0].toString(), "5");
		assertEquals(p[1].toString(), "99");
		assertEquals(p[2].toString(), "6");
		assertEquals(p[3].toString(), "7");
	}
	
	/**
	 *       array of parameters: [1,2,3,4,5,6,7,8,9]
	 *       initial position: 4
	 *       expected result: 6 (i.e., the considered portion of the vector is in positions [3..6])
	 *       expected parameter values:
	 *         p1: 5, p2: 99, p3: 6, p4: 7         
	 */
	@Test
	public void test_setParamMapping() {

		ServiceParameters parameters = new ServiceParameters();
		ServiceParam p1 = new ServiceParam("p1", null); parameters.add(p1);
		ServiceParam p2 = new ServiceParam("p2", null); parameters.add(p2);
		ServiceParam p3 = new ServiceParam("p3", null); parameters.add(p3);
		ServiceParam p4 = new ServiceParam("p4", null); parameters.add(p4);

		ServiceRequestActuation actuation = new ServiceRequestActuation(createAtom("act"), "serviceName", parameters);
		
		actuation.setParamActionMapping(p1.getParamName(), createAtom("action_param1"));
		actuation.setParamActionMapping(p3, createAtom("action_param3"));
		
		HashMap<Atom, Object> params = new HashMap<>();
		params.put(createAtom("action_param1"), 111);
		params.put(createAtom("action_param2"), 222);
		params.put(createAtom("action_param3"), 333);
		
		actuation.setParamValuesFromMapping(params);

		assertEquals(actuation.getParameters().getServiceParamByName("p1").getParamValue(), 111);
		assertNull(actuation.getParameters().getServiceParamByName("p2").getParamValue());
		assertEquals(actuation.getParameters().getServiceParamByName("p3").getParamValue(), 333);
		assertNull(actuation.getParameters().getServiceParamByName("p4").getParamValue());
		
	}

}


