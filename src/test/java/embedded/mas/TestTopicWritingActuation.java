package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.ros.TopicWritingActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.Term;

public class TestTopicWritingActuation {


	/**
	 * Test case: the default parameters hashmap is either null or empty 
	 */
	@Test
	public void testGetNestedParametersAsArray() {
		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", null); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", null); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);

		TopicWritingActuation actuation = new TopicWritingActuation(createAtom("act"), "topicName", "topicType", serviceParameters);

		Term[] t = actuation.getParametersAsArray();

		for(int j=0;j<serviceParameters.paramCount();j++)
			assertNull(t[j]);
		
		
		//add an empty set of default parameter values
		actuation.setDefaultParameterValues(new HashMap<String, Object>());
		for(int j=0;j<serviceParameters.paramCount();j++)
			assertNull(t[j]);
	}





	/**
	 * Test case: some parameters have a corresponding default value, others don't have
	 */
	@Test
	public void testGetNestedParametersAsArray_2() {
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 0.2
               y: 0.3
               z: 0.4 -> null
               w:
                 w1: 11
                 w2: 22 -> null
                 w3:
                   a1: 111
                   a2: 222
             angular:
               x: 0.5
               y: 0.6 -> null
               z: 0.7
		 */


		//build the hashmap which represents the default parameters
		HashMap<String, Object> a = new HashMap<>();
		a.put("a1", 111);
		a.put("a2", 222);

		HashMap<String, Object> w = new HashMap<>();
		w.put("w1", 11);
		//w.put("w2", 22);
		w.put("w3", a);

		HashMap<String, Object> linear = new HashMap<>();
		linear.put("x", 0.2);
		linear.put("y", 0.3);
		//linear.put("z", 0.4);
		linear.put("w", w);

		HashMap<String, Object> angular = new HashMap<>();
		angular.put("x", 0.5);
		//angular.put("y", 0.6);
		angular.put("z", 0.7);

		HashMap<String, Object> params = new HashMap<>();
		params.put("linear", linear);
		params.put("angular", angular);


		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", null); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", null); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);

		TopicWritingActuation actuation = new TopicWritingActuation(createAtom("act"), "topicName", "topicType", serviceParameters);

		actuation.setDefaultParameterValues(params);

		Term[] t = actuation.getParametersAsArray();


		assertEquals(t[0].toString(), "0.2");
		assertEquals(t[1].toString(), "0.3");
		assertNull(t[2]);
		assertEquals(t[3].toString(), "11");
		assertNull(t[4]);
		assertEquals(t[5].toString(), "111");
		assertEquals(t[6].toString(), "222");
		assertEquals(t[7].toString(), "0.5");
		assertNull(t[8]);
		assertEquals(t[9].toString(), "0.7");


	}

	/**
	 * Test case: all the parameters have a corresponding default value
	 */
	@Test
	public void testGetNestedParametersAsArray_3() {
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 0.2
               y: 0.3
               z: 0.4
               w:
                 w1: 11
                 w2: 22
                 w3:
                   a1: 111
                   a2: 222
             angular:
               x: 0.5
               y: 0.6
               z: 0.7
		 */


		//build the hashmap which represents the default parameters
		HashMap<String, Object> a = new HashMap<>();
		a.put("a1", 111);
		a.put("a2", 222);

		HashMap<String, Object> w = new HashMap<>();
		w.put("w1", 11);
		w.put("w2", 22);
		w.put("w3", a);

		HashMap<String, Object> linear = new HashMap<>();
		linear.put("x", 0.2);
		linear.put("y", 0.3);
		linear.put("z", 0.4);
		linear.put("w", w);

		HashMap<String, Object> angular = new HashMap<>();
		angular.put("x", 0.5);
		angular.put("y", 0.6);
		angular.put("z", 0.7);

		HashMap<String, Object> params = new HashMap<>();
		params.put("linear", linear);
		params.put("angular", angular);


		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", null); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", null); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);


		TopicWritingActuation actuation = new TopicWritingActuation(createAtom("act"), "topicName", "topicType", serviceParameters);
		actuation.setDefaultParameterValues(params);


		Term[] t = actuation.getParametersAsArray();


		assertEquals(t[0].toString(), "0.2");
		assertEquals(t[1].toString(), "0.3");
		assertEquals(t[2].toString(), "0.4");
		assertEquals(t[3].toString(), "11");
		assertEquals(t[4].toString(), "22");
		assertEquals(t[5].toString(), "111");
		assertEquals(t[6].toString(), "222");
		assertEquals(t[7].toString(), "0.5");
		assertEquals(t[8].toString(), "0.6");
		assertEquals(t[9].toString(), "0.7");


	}
}
