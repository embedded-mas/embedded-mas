package embedded.mas;

import static org.junit.Assert.*;

import org.junit.Test;

import embedded.mas.bridges.ros.ServiceArrayParam;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class TestServiceParameters {

	@Test
	public void testToJson() {
		ServiceParameters p = new ServiceParameters();		
		//integer param
		p.add(new ServiceParam("p1", 1));

		//string param
		p.add(new ServiceParam("p2", "test"));
		
		//array param
		p.addParameter("arrayOfFloatParameter", new Float[]{Float.parseFloat("3.14"), Float.parseFloat("1.99")} );


		//nested params
		ServiceParameters nestingParam = new ServiceParameters();
		ServiceParam nestedParam1 = new ServiceParam("nested1", 888);
		ServiceParam nestedParam2 = new ServiceParam("nested2", 999);
		nestingParam.add(nestedParam1);
		nestingParam.add(nestedParam2);
		p.add(new ServiceParam("nestedP", nestingParam)); //TODO: check: the service typing is useless here


		assertTrue("Fail to convert param to json", p.toJson().toString().equals("{\"p1\":1,\"p2\":\"test\",\"arrayOfFloatParameter\":[3.14,1.99],\"nestedP\":{\"nested1\":888,\"nested2\":999}}"));






	}

	@Test
	public void testParamCount() {
		ServiceParameters pLinear = new ServiceParameters();
		ServiceParam xLinear = new ServiceParam("x", null);
		ServiceParam yLinear = new ServiceParam("y", null);
		ServiceParam zLinear = new ServiceParam("z", null);
		pLinear.add(xLinear); 
		pLinear.add(yLinear);
		pLinear.add(zLinear);

		assertEquals(pLinear.paramCount(), 3);
	}

	@Test
	public void testParamCount_nested() {
		ServiceParameters params = new ServiceParameters();
		ServiceParam p1 = new ServiceParam("linear", null);
		ServiceParameters pLinear = new ServiceParameters();
		ServiceParam xLinear = new ServiceParam("x", null);
		ServiceParam yLinear = new ServiceParam("y", null);
		ServiceParam zLinear = new ServiceParam("z", null);
		pLinear.add(xLinear); 
		pLinear.add(yLinear);
		pLinear.add(zLinear);
		p1.setParamValue(pLinear);
		ServiceParam p2 = new ServiceParam("angular", null);
		ServiceParameters pAngular = new ServiceParameters();
		ServiceParam xAngular = new ServiceParam("x", null);
		ServiceParam yAngular = new ServiceParam("y", null);
		ServiceParam zAngular = new ServiceParam("z", null);
		pAngular.add(xAngular);
		pAngular.add(yAngular);
		pAngular.add(zAngular);
		p2.setParamValue(pAngular);
		params.add(p1);
		params.add(p2);

		assertEquals(params.paramCount(), 6);
	}



	@Test
	public void testSetValues() {
		//create the following list of parameters: [[1,2,3],123,[11,22,33]]
		//parameters linear.y and angular.z have default values (not changeable)
		ListTermImpl list = new ListTermImpl();
		ListTermImpl nestedList1 = new ListTermImpl();
		nestedList1.add(new NumberTermImpl(1));
		nestedList1.add(new NumberTermImpl(2));
		nestedList1.add(new NumberTermImpl(3));		
		ListTermImpl nestedList2 = new ListTermImpl();
		nestedList2.add(new NumberTermImpl(11));
		nestedList2.add(new NumberTermImpl(22));
		nestedList2.add(new NumberTermImpl(33));
		
		
		ServiceParameters params = new ServiceParameters();
		ServiceParam p1 = new ServiceParam("linear", null);
		ServiceParameters pLinear = new ServiceParameters();
		ServiceParam xLinear = new ServiceParam("x", null);
		ServiceParam yLinear = new ServiceParam("y", 0.4,false);
		ServiceParam zLinear = new ServiceParam("z", null);
		pLinear.add(xLinear); 
		pLinear.add(yLinear);
		pLinear.add(zLinear);
		p1.setParamValue(pLinear);
		ServiceParam p2 = new ServiceParam("angular", null);
		ServiceParameters pAngular = new ServiceParameters();
		ServiceParam xAngular = new ServiceParam("x", null);
		ServiceParam yAngular = new ServiceParam("y", null);
		ServiceParam zAngular = new ServiceParam("z", 0.22, false);
		pAngular.add(xAngular);
		pAngular.add(yAngular);
		pAngular.add(zAngular);
		p2.setParamValue(pAngular);
	
		params.add(p1);
		
		params.add(new ServiceParam("test",null));
		assertFalse("if the value is a list, the corresponding param must be a list of parameters",params.setValues(list.toArray())); 
		
		
		params.add(new ServiceParam("test", null));
		
		params.remove(params.size()-1);
		params.add(p2);
		


		assertTrue(params.toJson().toString().equals("{\"linear\":{\"x\":null,\"y\":0.4,\"z\":null},\"test\":null,\"angular\":{\"x\":null,\"y\":null,\"z\":0.22}}"));	





		assertFalse("It should not accept array of params with different size of the list of service params",params.setValues(list.toArray())); 

		list.add(nestedList1);
		list.add(new NumberTermImpl(123));
		list.add(nestedList2);

		assertTrue("It must accept array of params with same size of the list of service params",params.setValues(list.toArray())); 		
		assertTrue(params.toJson().toString().equals("{\"linear\":{\"x\":1,\"y\":0.4,\"z\":3},\"test\":123,\"angular\":{\"x\":11,\"y\":22,\"z\":0.22}}"));

	}
	
	@Test
	public void testSetValuesWithDefaultValues() {
		//create the following list of parameters: [[1,2,3],123,[11,22,33]]
		ListTermImpl list = new ListTermImpl();
		ListTermImpl nestedList1 = new ListTermImpl();
		nestedList1.add(new NumberTermImpl(1));
		nestedList1.add(new NumberTermImpl(2));
		nestedList1.add(new NumberTermImpl(3));		
		ListTermImpl nestedList2 = new ListTermImpl();
		nestedList2.add(new NumberTermImpl(11));
		nestedList2.add(new NumberTermImpl(22));
		nestedList2.add(new NumberTermImpl(33));
		
		
		ServiceParameters params = new ServiceParameters();
		ServiceParam p1 = new ServiceParam("linear", null);
		ServiceParameters pLinear = new ServiceParameters();
		ServiceParam xLinear = new ServiceParam("x", null);
		ServiceParam yLinear = new ServiceParam("y", null);
		ServiceParam zLinear = new ServiceParam("z", null);
		pLinear.add(xLinear); 
		pLinear.add(yLinear);
		pLinear.add(zLinear);
		p1.setParamValue(pLinear);
		ServiceParam p2 = new ServiceParam("angular", null);
		ServiceParameters pAngular = new ServiceParameters();
		ServiceParam xAngular = new ServiceParam("x", null);
		ServiceParam yAngular = new ServiceParam("y", null);
		ServiceParam zAngular = new ServiceParam("z", null);
		pAngular.add(xAngular);
		pAngular.add(yAngular);
		pAngular.add(zAngular);
		p2.setParamValue(pAngular);
	
		params.add(p1);
		
		params.add(new ServiceParam("test",null));
		assertFalse("if the value is a list, the corresponding param must be a list of parameters",params.setValues(list.toArray())); 
		
		
		params.add(new ServiceParam("test", null));
		
		params.remove(params.size()-1);
		params.add(p2);
		

		assertTrue(params.toJson().toString().equals("{\"linear\":{\"x\":null,\"y\":null,\"z\":null},\"test\":null,\"angular\":{\"x\":null,\"y\":null,\"z\":null}}"));	





		assertFalse("It should not accept array of params with different size of the list of service params",params.setValues(list.toArray())); 

		list.add(nestedList1);
		list.add(new NumberTermImpl(123));
		list.add(nestedList2);

		assertTrue("It must accept array of params with same size of the list of service params",params.setValues(list.toArray())); 		
		assertTrue(params.toJson().toString().equals("{\"linear\":{\"x\":1,\"y\":2,\"z\":3},\"test\":123,\"angular\":{\"x\":11,\"y\":22,\"z\":33}}"));

	}

	@Test
	public void testsetValuesFromArray() {
		
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

		Term[] t = new Term[serviceParameters.paramCount()];
		t[0] = new NumberTermImpl(0.2);
		t[1] = new NumberTermImpl(0.3);
		t[2] = new NumberTermImpl(0.4);
		t[3] = new NumberTermImpl(11.0);
		t[4] = new NumberTermImpl(22.0);
		t[5] = new NumberTermImpl(111.0);
		t[6] = new NumberTermImpl(222.0);
		t[7] = new NumberTermImpl(0.5);
		t[8] = new NumberTermImpl(0.6);
		t[9] = new NumberTermImpl(0.7);
		
		serviceParameters.setValuesFromArray(t);
		
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(0).getParamValue().toString(), "0.2"); //linear.x
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(1).getParamValue().toString(), "0.3"); //linear.y
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(2).getParamValue().toString(), "0.4"); //linear.z
		
		assertEquals(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(0).getParamValue().toString(), "11"); //linear.w.w1
		assertEquals(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(1).getParamValue().toString(), "22"); //linear.w.w2
		
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(0).getParamValue().toString(), "111"); //linear.w.w3.a1
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(1).getParamValue().toString(), "222"); //linear.w.w3.a2

		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(0).getParamValue().toString(), "0.5"); //angular.x
		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(1).getParamValue().toString(), "0.6"); //angular.y
		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(2).getParamValue().toString(), "0.7"); //angular.z
		
	
		}
	
	
	@Test
	public void testsetValuesFromArrayWithDefaultValues() {
		
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 0.2
               y: 0.3
               z: 0.04 -> default (not changeable)
               w:
                 w1: 11
                 w2: 22
                 w3:
                   a1: 1111  -> default (not changeable)
                   a2: 222
             angular:
               x: 0.5
               y: 0.6 
               z: 0.7
               
             array of parameters: [0.2, 0.3, 11, 22, 222, 0.5, 0.6, 0.7]
		 */
		
		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", 1111, false); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", 0.04, false); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);
		


		
		Term[] t = new Term[serviceParameters.paramCount()];
		t[0] = new NumberTermImpl(0.2); //linear.x
		t[1] = new NumberTermImpl(0.3); //linear.y
		t[2] = new NumberTermImpl(11.0); //linear.w.w1
		t[3] = new NumberTermImpl(22.0); //linear.w.w2
		t[4] = new NumberTermImpl(222.0); //linear.w.w3.a2
		t[5] = new NumberTermImpl(0.5); //angular.x
		t[6] = new NumberTermImpl(0.6); //angular.y
		t[7] = new NumberTermImpl(0.7); //angular.z
		
		serviceParameters.setValuesFromArray(t);
		
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(0).getParamValue().toString(), "0.2"); //linear.x
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(1).getParamValue().toString(), "0.3"); //linear.y
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(2).getParamValue().toString(), "0.04"); //linear.z
		
		assertEquals(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(0).getParamValue().toString(), "11"); //linear.w.w1
		assertEquals(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(1).getParamValue().toString(), "22"); //linear.w.w2
		
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(0).getParamValue().toString(), "1111"); //linear.w.w3.a1
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(1).getParamValue().toString(), "222"); //linear.w.w3.a2

		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(0).getParamValue().toString(), "0.5"); //angular.x
		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(1).getParamValue().toString(), "0.6"); //angular.y
		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(2).getParamValue().toString(), "0.7"); //angular.z
		
	
		}
	
	@Test
	public void test_getServiceParamByName() {
		ServiceParameters params = new ServiceParameters();
		ServiceParam x = new ServiceParam("x", null);
		ServiceParam y = new ServiceParam("y", null);
		params.add(x);
		params.add(y);
		assertNotNull(params.getServiceParamByName("x"));
		assertNull(params.getServiceParamByName("z"));
	}
	
	@Test
	public void test_setToDefaultState() {
		
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 0.2
               y: 0.3
               z: 0.04 -> default (not changeable)
               w:
                 w1: 11
                 w2: 22
                 w3:
                   a1: 1111  -> default (not changeable)
                   a2: 222
             angular:
               x: 0.5
               y: 0.6 
               z: 0.7
		 */
		
		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", 1111, false); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", 0.04, false); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);
		


		
		Term[] t = new Term[serviceParameters.paramCount()];
		t[0] = new NumberTermImpl(0.2); //linear.x
		t[1] = new NumberTermImpl(0.3); //linear.y
		t[2] = new NumberTermImpl(11.0); //linear.w.w1
		t[3] = new NumberTermImpl(22.0); //linear.w.w2
		t[4] = new NumberTermImpl(222.0); //linear.w.w3.a2
		t[5] = new NumberTermImpl(0.5); //angular.x
		t[6] = new NumberTermImpl(0.6); //angular.y
		t[7] = new NumberTermImpl(0.7); //angular.z
		
		serviceParameters.setValuesFromArray(t);
		
		//test whether all the values have been set
		
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(0).getParamValue().toString(), "0.2"); //linear.x
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(1).getParamValue().toString(), "0.3"); //linear.y
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(2).getParamValue().toString(), "0.04"); //linear.z
		
		assertEquals(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(0).getParamValue().toString(), "11"); //linear.w.w1
		assertEquals(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(1).getParamValue().toString(), "22"); //linear.w.w2
		
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(0).getParamValue().toString(), "1111"); //linear.w.w3.a1
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(1).getParamValue().toString(), "222"); //linear.w.w3.a2

		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(0).getParamValue().toString(), "0.5"); //angular.x
		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(1).getParamValue().toString(), "0.6"); //angular.y
		assertEquals(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(2).getParamValue().toString(), "0.7"); //angular.z
				
		
		//test whether all the values back to the default state
		serviceParameters.setToDefaultState();
		assertNull(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(0).getParamValue()); //linear.x
		assertNull(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(1).getParamValue()); //linear.y
		assertEquals(((ServiceParameters)serviceParameters.get(0).getParamValue()).get(2).getParamValue().toString(), "0.04"); //linear.z
		
		assertNull(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(0).getParamValue()); //linear.w.w1
		assertNull(((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(1).getParamValue()); //linear.w.w2
		
		assertEquals(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(0).getParamValue().toString(), "1111"); //linear.w.w3.a1
		assertNull(((ServiceParameters)((ServiceParameters)((ServiceParameters)serviceParameters.get(0).getParamValue()).get(3).getParamValue()).get(2).getParamValue()).get(1).getParamValue()); //linear.w.w3.a2

		assertNull(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(0).getParamValue()); //angular.x
		assertNull(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(1).getParamValue()); //angular.y
		assertNull(((ServiceParameters)serviceParameters.get(1).getParamValue()).get(2).getParamValue()); //angular.z
				
		
	}
	
	
	@Test
	public void test_setParamValues() {
		
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 
               y: 
               z: 0.04 -> default (not changeable)
               w:
                 w1: 
                 w2: 
                 w3:
                   a1: 1111  -> default (not changeable)
                   a2: 
             angular:
               x: 
               y:  
               z: 
               
               
            array of parameters: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
            initial position: 3
            expected result: 10 (i.e., the considered portion of the vector is in positions [3..10])
            expected parameter state:
              default_param_values: 
                linear:
                  x: 4
                  y: 5
                  z: 0.04 -> default (not changeable)
                  w: ---
                    w1: 6 
                    w2: 7
                    w3: ---
                      a1: 1111  -> default (not changeable)
                      a2: 8 
                angular:
                  x: 9
                  y: 10 
                  z: 11
                  
                
		 */
		
		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", 1111, false); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", 0.04, false); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);
		


		
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
		
		assertEquals(serviceParameters.setParamValues(t, 3),10);
		
		//test whether all the values have been set
		
	
		assertEquals( ((ServiceParameters) serviceParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("x").getParamValue().toString(),"4"); //linear.x
		assertEquals( ((ServiceParameters) serviceParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("y").getParamValue().toString(),"5"); //linear.y
		assertEquals( ((ServiceParameters) serviceParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("z").getParamValue().toString(),"0.04"); //linear.z
		
		assertEquals( ((ServiceParameters)((ServiceParameters) serviceParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w1").getParamValue().toString(),"6"); //linear.w.w1
		assertEquals( ((ServiceParameters)((ServiceParameters) serviceParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w2").getParamValue().toString(),"7"); //linear.w.w2
		
		assertEquals( ((ServiceParameters)((ServiceParameters)((ServiceParameters) serviceParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w3").getParamValue()).getServiceParamByName("a1").getParamValue().toString(),"1111"); //linear.w.w3.a1
		assertEquals( ((ServiceParameters)((ServiceParameters)((ServiceParameters) serviceParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w3").getParamValue()).getServiceParamByName("a2").getParamValue().toString(),"8"); //linear.w.w3.a2
				
		
		assertEquals( ((ServiceParameters) serviceParameters.getServiceParamByName("angular").getParamValue()).getServiceParamByName("x").getParamValue().toString(),"9"); //angular.x
		assertEquals( ((ServiceParameters) serviceParameters.getServiceParamByName("angular").getParamValue()).getServiceParamByName("y").getParamValue().toString(),"10"); //angular.y
		assertEquals( ((ServiceParameters) serviceParameters.getServiceParamByName("angular").getParamValue()).getServiceParamByName("z").getParamValue().toString(),"11"); //angular.z										
		
	}
	
	@Test
	public void test_Clone() {
		/**
		 * Simulated params: 
		 * default_param_values: 
             linear:
               x: 0.2
               y: 0.3
               z: 0.04 -> default (not changeable)
               w:
                 w1: 11
                 w2: 22
                 w3:
                   a1: 1111  -> default (not changeable)
                   a2: 222
             angular:
               x: 0.5
               y: 0.6 
               z: 0.7
		 */
		
		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", 1111, false); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", 0.04, false); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);
		


		
		Term[] t = new Term[serviceParameters.paramCount()];
		t[0] = new NumberTermImpl(0.2); //linear.x
		t[1] = new NumberTermImpl(0.3); //linear.y
		t[2] = new NumberTermImpl(11.0); //linear.w.w1
		t[3] = new NumberTermImpl(22.0); //linear.w.w2
		t[4] = new NumberTermImpl(222.0); //linear.w.w3.a2
		t[5] = new NumberTermImpl(0.5); //angular.x
		t[6] = new NumberTermImpl(0.6); //angular.y
		t[7] = new NumberTermImpl(0.7); //angular.z
		
		serviceParameters.setValuesFromArray(t);
		
		ServiceParameters newParameters = serviceParameters.clone();
		
		serviceParameters.setValuesFromArray(t);
		
		
		assertNull( ((ServiceParameters) newParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("x").getParamValue()); //linear.x
		assertNull( ((ServiceParameters) newParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("y").getParamValue()); //linear.y
		assertNotNull( ((ServiceParameters) newParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("z").getParamValue()); //linear.z
		
		assertNull( ((ServiceParameters)((ServiceParameters) newParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w1").getParamValue()); //linear.w.w1
		assertNull( ((ServiceParameters)((ServiceParameters) newParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w2").getParamValue()); //linear.w.w2
		
		assertNotNull( ((ServiceParameters)((ServiceParameters)((ServiceParameters) newParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w3").getParamValue()).getServiceParamByName("a1").getParamValue()); //linear.w.w3.a1
		assertNull( ((ServiceParameters)((ServiceParameters)((ServiceParameters) newParameters.getServiceParamByName("linear").getParamValue()).getServiceParamByName("w").getParamValue()).getServiceParamByName("w3").getParamValue()).getServiceParamByName("a2").getParamValue()); //linear.w.w3.a2
				
		
		assertNull( ((ServiceParameters) newParameters.getServiceParamByName("angular").getParamValue()).getServiceParamByName("x").getParamValue()); //angular.x
		assertNull( ((ServiceParameters) newParameters.getServiceParamByName("angular").getParamValue()).getServiceParamByName("y").getParamValue()); //angular.y
		assertNull( ((ServiceParameters) newParameters.getServiceParamByName("angular").getParamValue()).getServiceParamByName("z").getParamValue()); //angular.z
		
	}
	
	@Test
	public void testSetValuesArrayParam() {
		////create the following list of parameters: [0.1, [[1,2,3],[4,5,6]],0.2]
		ListTermImpl list = new ListTermImpl();
		
		ListTermImpl arrayParam = new ListTermImpl();
				
		ListTermImpl nestedList1 = new ListTermImpl();
		nestedList1.add(new NumberTermImpl(1));
		nestedList1.add(new NumberTermImpl(2));
		nestedList1.add(new NumberTermImpl(3));		
		ListTermImpl nestedList2 = new ListTermImpl();
		nestedList2.add(new NumberTermImpl(4));
		nestedList2.add(new NumberTermImpl(5));
		nestedList2.add(new NumberTermImpl(6));
		
		arrayParam.add(nestedList1);
		arrayParam.add(nestedList2);
		
		list.add(new NumberTermImpl(0.1));
		list.add(arrayParam);
		list.add(new NumberTermImpl(0.2));
		
		System.out.println("LIST:  " + list);
		
		ServiceParam y11 = new ServiceParam("y11", 1);
		ServiceParam y12 = new ServiceParam("y12", 2);
		ServiceParam y13 = new ServiceParam("y13", 3);
		ServiceParameters y1 = new ServiceParameters();
		y1.add(y11); y1.add(y12); y1.add(y13);
		
		ServiceParam y21 = new ServiceParam("y21", 1);
		ServiceParam y22 = new ServiceParam("y22", 2);
		ServiceParam y23 = new ServiceParam("y23", 3);
		ServiceParameters y2 = new ServiceParameters();
		y2.add(y21); y2.add(y22); y2.add(y23);
		
		ServiceParameters parametersY = new ServiceParameters();
		parametersY.add(new ServiceParam("par1", y1));
		parametersY.add(new ServiceParam("par2", y2));
		
		ServiceArrayParam y = new ServiceArrayParam("y", parametersY);
		
		ServiceParameters parameters = new ServiceParameters();
		parameters.add(new ServiceParam("x", 0.1));
		parameters.add(y);
		parameters.add(new ServiceParam("z", 0.2));
		
		System.out.println(">>>" + parameters.toJson());
		
		parameters.setValues(list.toArray());
		
		System.out.println("+++" + parameters.toJson());
		
		
	}
}
