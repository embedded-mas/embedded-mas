package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import embedded.mas.bridges.ros.ServiceArrayParam;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;

public class TestServiceArrayParam {

	
	@Test
	public void testToJsonValue_11() {
		Object[] p1 = {1,2,3};		
		ServiceArrayParam param = new ServiceArrayParam("param", p1);		
		assertTrue(param.toJsonValue().toString().equals("\"param\":[1,2,3]"));
	}
	
	
	@Test
	public void testAdd() {
		Object[] p1 = {1,2,3};		
		ServiceArrayParam param = new ServiceArrayParam("param", p1);		
		assertTrue(param.toJsonValue().toString().equals("\"param\":[1,2,3]"));
		param.add(4);
		assertTrue("Expected: " + "\"param\":[1,2,3,4]. Obtained: " + param.toJsonValue().toString(),param.toJsonValue().toString().equals("\"param\":[1,2,3,4]"));
	}
}
