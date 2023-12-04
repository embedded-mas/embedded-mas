package embedded.mas;

import static org.junit.Assert.*;

import org.junit.Test;

import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;

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
		
		System.out.println(params.toJson());
		
		assertTrue(params.toJson().toString().equals("{\"linear\":{\"x\":null,\"y\":null,\"z\":null},\"test\":null,\"angular\":{\"x\":null,\"y\":null,\"z\":null}}"));	
		
		
		
		

		assertFalse("It should not accept array of params with different size of the list of service params",params.setValues(list.toArray())); 
		
		list.add(nestedList1);
		list.add(new NumberTermImpl(123));
		list.add(nestedList2);
		System.out.println(list);
		
		assertTrue("It must accept array of params with same size of the list of service params",params.setValues(list.toArray())); 		
		assertTrue(params.toJson().toString().equals("{\"linear\":{\"x\":1,\"y\":2,\"z\":3},\"test\":123,\"angular\":{\"x\":11,\"y\":22,\"z\":33}}"));
		
	}
}
