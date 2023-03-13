package embedded.mas;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import embedded.mas.bridges.ros.ServiceParam;

public class TestServiceParam {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	//test whether the types are properly written in json
	@Test
	public void testTypeConversionOnJson() {
		//int parameter
		ServiceParam p1 = new ServiceParam("p1", 1);
		assertEquals(p1.getParamName(), "p1");
		assertEquals(p1.getParamValue(), 1);
		assertEquals(p1.toJsonValue(), "\"p1\":1");
		
		//float parameter
		ServiceParam p2 = new ServiceParam("p2", 3.14);
		assertEquals(p2.getParamName(), "p2");
		assertEquals(p2.getParamValue(), 3.14);
		assertEquals(p2.toJsonValue(), "\"p2\":3.14");
			
		//string parameter
		ServiceParam p3 = new ServiceParam("p3", "test");
		assertEquals(p3.getParamName(), "p3");
		assertEquals(p3.getParamValue(), "test");
		assertEquals(p3.toJsonValue(), "\"p3\":\"test\"");
      	
   
		
	}
	
	@Test
	public void testTypeConversionOnJsonOnValue() {
		ServiceParam p3 = new ServiceParam("pOn", "on");
		assertEquals(p3.getParamName(), "pOn");
		assertEquals(p3.getParamValue(), "on");
		assertEquals(p3.toJsonValue(), "\"pOn\":1");
	}
	
	@Test
	public void testTypeConversionOnJsonOffValue() {
		ServiceParam p3 = new ServiceParam("pOff", "off");
		assertEquals(p3.getParamName(), "pOff");
		assertEquals(p3.getParamValue(), "off");
		assertEquals(p3.toJsonValue(), "\"pOff\":0");
	}
	
	


}
