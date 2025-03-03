package embedded.mas;

import static org.junit.Assert.*;

import org.junit.Test;

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

		System.out.println(p.toJson());
		System.out.println("{\"p1\":1,\"p2\":\"test\",\"arrayOfFloatParameter\":[3.14,1.99],\"nestedP\":{\"nested1\":888,\"nested2\":999}}");

		System.out.println(p.toJson().toString().equals("{\"p1\":1,\"p2\":\"test\",\"arrayOfFloatParameter\":[3.14,1.99],\"nestedP\":{\"nested1\":888,\"nested2\":999}}"));

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
		System.out.println(params);

		assertEquals(params.paramCount(), 6);
	}



	@Test
	public void testSetValues() {
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
}
