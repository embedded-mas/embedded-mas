package embedded.mas;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.ros.ServiceRequestActuation;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;

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

}
