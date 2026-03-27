package embedded.mas;

import static org.junit.Assert.*;

import org.junit.Test;

import embedded.mas.bridges.ros.ServiceArrayMsgParam;
import embedded.mas.bridges.ros.ServiceArrayParam;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;

import static embedded.mas.bridges.ros.ServiceParam.createServiceParam;

public class TestServiceParameters {

	@Test
	public void testToJson() {
		ServiceParameters p = new ServiceParameters();		
		//integer param
		p.add(createServiceParam("p1", 1));

		//string param
		p.add(createServiceParam("p2", "test"));
		
		//array param
		p.add(createServiceParam("arrayOfFloatParameter", new Float[]{Float.parseFloat("3.14"), Float.parseFloat("1.99")} ));


		//nested params
		ServiceParameters nestingParam = new ServiceParameters();
		ServiceParam nestedParam1 = createServiceParam("nested1", 888);
		ServiceParam nestedParam2 = createServiceParam("nested2", 999);
		nestingParam.add(nestedParam1);
		nestingParam.add(nestedParam2);
		p.add(createServiceParam("nestedP", nestingParam));


		assertTrue("Fail to convert param to json", p.toJson().toString().equals("{\"p1\":1,\"p2\":\"test\",\"arrayOfFloatParameter\":[3.14,1.99],\"nestedP\":{\"nested1\":888,\"nested2\":999}}"));






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
		ServiceParam p1 = createServiceParam("linear", null);
		ServiceParameters pLinear = new ServiceParameters();
		ServiceParam xLinear = createServiceParam("x", null);
		ServiceParam yLinear = createServiceParam("y", null);
		ServiceParam zLinear = createServiceParam("z", null);
		pLinear.add(xLinear); 
		pLinear.add(yLinear);
		pLinear.add(zLinear);
		p1.setParamValue(pLinear);
		ServiceParam p2 = createServiceParam("angular", null);
		ServiceParameters pAngular = new ServiceParameters();
		ServiceParam xAngular = createServiceParam("x", null);
		ServiceParam yAngular = createServiceParam("y", null);
		ServiceParam zAngular = createServiceParam("z", null);
		pAngular.add(xAngular);
		pAngular.add(yAngular);
		pAngular.add(zAngular);
		p2.setParamValue(pAngular);
	
		params.add(p1);
		
		params.add(createServiceParam("test",null));
		assertFalse("if the value is a list, the corresponding param must be a list of parameters",params.setValues(list.toArray())); 
		
		
		params.add(createServiceParam("test", null));
		
		params.remove(params.size()-1);
		params.add(p2);
		
		
		assertTrue(params.toJson().toString().equals("{\"linear\":{\"x\":null,\"y\":null,\"z\":null},\"test\":null,\"angular\":{\"x\":null,\"y\":null,\"z\":null}}"));	
		
		
		
		

		assertFalse("It should not accept array of params with different size of the list of service params",params.setValues(list.toArray())); 
		
		list.add(nestedList1);
		list.add(new NumberTermImpl(123));
		list.add(nestedList2);
		
		assertTrue("It must accept array of params with same size of the list of service params",params.setValues(list.toArray())); 		
		assertTrue("Expected: " + "{\"linear\":{\"x\":1,\"y\":2,\"z\":3},\"test\":123,\"angular\":{\"x\":11,\"y\":22,\"z\":33}}" + 
		           "Obtained: " + params.toJson().toString(),  
				params.toJson().toString().equals("{\"linear\":{\"x\":1,\"y\":2,\"z\":3},\"test\":123,\"angular\":{\"x\":11,\"y\":22,\"z\":33}}"));
		
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
		
		
		ServiceParam y11 = createServiceParam("y11", 1);
		ServiceParam y12 = createServiceParam("y12", 2);
		ServiceParam y13 = createServiceParam("y13", 3);
		ServiceParameters y1 = new ServiceParameters();
		y1.add(y11); y1.add(y12); y1.add(y13);
		
		ServiceParam y21 = createServiceParam("y21", 1);
		ServiceParam y22 = createServiceParam("y22", 2);
		ServiceParam y23 = createServiceParam("y23", 3);
		ServiceParameters y2 = new ServiceParameters();
		y2.add(y21); y2.add(y22); y2.add(y23);
		
		
		ServiceArrayParam y = new ServiceArrayParam("y", null);
		
		ServiceParameters parameters = new ServiceParameters();
		parameters.add(createServiceParam("x", 0.1));
		parameters.add(y);
		parameters.add(createServiceParam("z", 0.2));
		
		
		parameters.setValues(list.toArray());
		
		
		assertEquals(parameters.toJson().toString(), "{\"x\":0.1,\"y\":[[1,2,3],[4,5,6]],\"z\":0.2}");
		
		
	}
	
	
	/**
	 * Input parameters: [0.1, [[1,2,3],[4,5,6]],0.2]
	 * Expected output: {"x":0.1, "y":[{"a":1, "b":2, "c": 3},{"a":4, "b":5, "c": 6}],"z":0.2}
	 */
	@Test
	public void testSetValuesMsgArrayParam() {
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
		
		
		
		ServiceParam y11 = createServiceParam("a", null);
		ServiceParam y12 = createServiceParam("b", null);
		ServiceParam y13 = createServiceParam("c", null);
		ServiceParameters ytemp = new ServiceParameters();
		ytemp.add(y11); ytemp.add(y12); ytemp.add(y13);
		
		ServiceArrayMsgParam y = new ServiceArrayMsgParam("y", null, ytemp);
		
		

		
		ServiceParameters parameters = new ServiceParameters();
		parameters.add(createServiceParam("x", 111));
		parameters.add(y);
		parameters.add(createServiceParam("z", 222));
		
		
		parameters.setValues(list.toArray());
		
		
		assertTrue(parameters.toJson().toString().equals("{\"x\":0.1,\"y\":[{\"a\":1,\"b\":2,\"c\":3},{\"a\":4,\"b\":5,\"c\":6}],\"z\":0.2}"));
	
		
		
	}
}
