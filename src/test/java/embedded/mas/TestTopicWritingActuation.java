package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.ros.TopicWritingActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTermImpl;
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
	
	
	/**
	 * Test case: check whether parameters roll back to the default state (i.e. non default values are null)
	 */
	@Test
	public void testSetParametersToDefaultState() {
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


		
		
		//check correct assignment of default parameters
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
		
		//check correct assignment of non default parameters
		((ServiceParameters)actuation.getParameters().getServiceParamByName("linear").getParamValue()).getServiceParamByName("z").setParamValue(0.4);
		((ServiceParameters)((ServiceParameters)actuation.getParameters().getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w2").setParamValue(22);		
		System.out.println(actuation.toString());		
		t = actuation.getParametersAsArray();				
		assertEquals(t[0].toString(), "0.2");
		assertEquals(t[1].toString(), "0.3");
		assertEquals(t[2].toString(), "0.4");
		assertEquals(t[3].toString(), "11");
		assertEquals(t[4].toString(), "22");
		assertEquals(t[5].toString(), "111");
		assertEquals(t[6].toString(), "222");
		assertEquals(t[7].toString(), "0.5");
		assertNull(t[8]);
		assertEquals(t[9].toString(), "0.7");

		
		//check whether default parameters are correctly restored
		actuation.setParametersToDefaultState();
		t = actuation.getParametersAsArray();	
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
	
	
	@Test
	public void test_SetParaValues() {
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 
               y: 
               z: 0.4 -> default
               w: ---
                 w1: 
                 w2: 22 -> default
                 w3: ---
                   a1: 111 -> default
                   a2: 222 -> default
             angular:
               x: 0.5
               y: 0.6 -> default ->default
               z: 0.7
               
               
                
            array of parameters: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
            initial position: 3
            expected result: 7 (i.e., the considered portion of the vector is in positions [3..7])
            expected parameter state:            
             linear:
                x: 4
                y: 5
                z: 0.4 -> default
                w: ---
                  w1: 6
                  w2: 22 -> default
                  w3: ---
                    a1: 111 -> default
                    a2: 222 -> default
              angular:
                x: 0.5 7
                y: 0.6 -> default ->default
                z: 0.7 8
		 */


		//build the hashmap which represents the default parameters
		HashMap<String, Object> a = new HashMap<>();
		a.put("a1", 111);
		a.put("a2", 222);

		HashMap<String, Object> w = new HashMap<>();
		w.put("w2", 22);
		w.put("w3", a);
		

		HashMap<String, Object> linear = new HashMap<>();
		linear.put("z", 0.4);
		linear.put("w", w);

		HashMap<String, Object> angular = new HashMap<>();
	    angular.put("y", 0.6);

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
		t[9] = new NumberTermImpl(10); 
		t[10] = new NumberTermImpl(11); 
		t[11] = new NumberTermImpl(12); 
		t[12] = new NumberTermImpl(13); 
		t[13] = new NumberTermImpl(14); 
		t[14] = new NumberTermImpl(15); 
		
		
		assertEquals(actuation.setParamValues(t, 3),7);
		Term[] p = actuation.getParametersAsArray();
		
		//check correct assignment of  parameters
		assertEquals(p[0].toString(), "4");
		assertEquals(p[1].toString(), "5");
		assertEquals(p[2].toString(), "0.4");
		assertEquals(p[3].toString(), "6");
		assertEquals(p[4].toString(), "22");
		assertEquals(p[5].toString(), "111");
		assertEquals(p[6].toString(), "222");
		assertEquals(p[7].toString(), "7");
		assertEquals(p[8].toString(), "0.6");
		assertEquals(p[9].toString(), "8");

	}
	
//	AQUI: TESTAR PARAM MAPPING
	
	@Test
	public void test_SetParamMapping() {
		
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 
               y: 
               z: 0.4 -> default
               w: ---
                 w1: 
                 w2: 22 -> default
                 w3: ---
                   a1: 111 -> default
                   a2: 222 -> default
             angular:
               x: 0.5
               y: 0.6 -> default ->default
               z: 0.7
               
               
                
            array of parameters: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
            initial position: 3
            expected result: 7 (i.e., the considered portion of the vector is in positions [3..7])
            expected parameter state:            
             linear:
                x: 4
                y: 5
                z: 0.4 -> default
                w: ---
                  w1: 6
                  w2: 22 -> default
                  w3: ---
                    a1: 111 -> default
                    a2: 222 -> default
              angular:
                x: 0.5 7
                y: 0.6 -> default ->default
                z: 0.7 8
		 */


		//build the hashmap which represents the default parameters
		HashMap<String, Object> a = new HashMap<>();
		a.put("a1", 111);
		a.put("a2", 222);

		HashMap<String, Object> w = new HashMap<>();
		w.put("w2", 22);
		w.put("w3", a);
		

		HashMap<String, Object> linear = new HashMap<>();
		linear.put("z", 0.4);
		linear.put("w", w);

		HashMap<String, Object> angular = new HashMap<>();
	    angular.put("y", 0.6);

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
		serviceParameters.add(new ServiceParam("test", null));
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);

		TopicWritingActuation actuation = new TopicWritingActuation(createAtom("act"), "topicName", "topicType", serviceParameters);
		actuation.setParamActionMapping("linear.w.w3.a1", createAtom("action_param1"));
		actuation.setParamActionMapping("test", createAtom("action_param_test"));
		
		System.out.println(actuation.toString());

		
	}
	
	@Test
	public void test_setParamFromMapping() {
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 
               y: 
               z: 0.4 -> default
               w: ---
                 w1: 
                 w2: 22 -> default
                 w3: ---
                   a1: 111 -> default
                   a2: 222 -> default
             angular:
               x: 0.5
               y: 0.6 -> default ->default
               z: 0.7
               
               
                
            array of parameters: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
            initial position: 3
            expected result: 7 (i.e., the considered portion of the vector is in positions [3..7])
            expected parameter state:            
             linear:
                x: 4
                y: 5
                z: 0.4 -> default
                w: ---
                  w1: 6
                  w2: 22 -> default
                  w3: ---
                    a1: 111 -> default
                    a2: 222 -> default
              angular:
                x: 0.5 7
                y: 0.6 -> default ->default
                z: 0.7 8
		 */


		//build the hashmap which represents the default parameters
		HashMap<String, Object> a = new HashMap<>();
		a.put("a1", 111);
		a.put("a2", 222);

		HashMap<String, Object> w = new HashMap<>();
		w.put("w2", 22);
		w.put("w3", a);
		

		HashMap<String, Object> linear = new HashMap<>();
		linear.put("z", 0.4);
		linear.put("w", w);

		HashMap<String, Object> angular = new HashMap<>();
	    angular.put("y", 0.6);

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
		serviceParameters.add(new ServiceParam("test", null));
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);

		TopicWritingActuation actuation = new TopicWritingActuation(createAtom("act"), "topicName", "topicType", serviceParameters);
		actuation.setParamActionMapping("linear.w.w3.a1", createAtom("action_param1"));
		actuation.setParamActionMapping("test", createAtom("action_param_test"));
		
		Map<Atom, Object> actionParams = new HashMap<Atom, Object>();
		actionParams.put(createAtom("action_param1"), 2222);
		actionParams.put(createAtom("action_param2"), "blabla");
		actionParams.put(createAtom("action_param_test"), 999);
		
		actuation.setParamActionMapping("linear.w.w3.a1", createAtom("action_param1"));
		actuation.setParamActionMapping("angular.x", createAtom("action_param2"));
		actuation.setParamActionMapping("test", createAtom("action_param_test"));
		
		actuation.setParamValuesFromMapping(actionParams);
		
		
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)actuation.getParameters().getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w3").getParamValue()).getServiceParamByName("a1").getParamValue(),2222);
		assertEquals(((ServiceParameters)actuation.getParameters().getServiceParamByName("angular").getParamValue()).getServiceParamByName("x").getParamValue(),"blabla");
		assertEquals(actuation.getParameters().getServiceParamByName("test").getParamValue(),999);
		

	}
}
