package embedded.mas;

import static org.junit.Assert.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.junit.Test;

import embedded.mas.bridges.ros.ServiceArrayMsgParam;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;

import static embedded.mas.bridges.ros.ServiceParam.createServiceParam;

public class TestServiceArrayMsgParam {

	@Test
	public void testSetParamValueObjectArray() {
		ServiceParam x = createServiceParam("x", 0);
		ServiceParam y = createServiceParam("y", 0);
		ServiceParam z = createServiceParam("z", 0);
		ServiceParameters p = new ServiceParameters();
		p.add(x); p.add(y); p.add(z);
		
		int[] p1 = {1,2,3};
		int[] p2 = {9,8,7};
		
		Object[] param = {p1,p2};
		
		ServiceArrayMsgParam paramArray = new ServiceArrayMsgParam("paramArray", param, p);
		
		
		assertTrue(paramArray.getParamValue().getClass().isArray()); //the param value must be an array
		
		for(Object o : (Object[])paramArray.getParamValue()) {
			assertTrue(o instanceof ServiceParameters);
		}
		
		
		assertTrue(paramArray.toJsonValue().toString().equals("\"paramArray\":[{\"x\":1,\"y\":2,\"z\":3},{\"x\":9,\"y\":8,\"z\":7}]"));
		
	}

}
