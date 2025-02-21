package embedded.mas;

import static org.junit.Assert.*;

import java.util.HashMap;

import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.actuation.ros.TopicWritingActuation;

public class TestTopicWritingActuation {

	@Test
	public void testGetParametersAsArray() {
		TopicWritingActuation actuation  = new TopicWritingActuation(createAtom("act"), "topicName", "topicType", createAtom("param"));
		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("param", 5);
		actuation.setDefaultParameterValues(defaultParams);
		assertEquals(actuation.getParametersAsArray()[0].toString(), "5");
	}

}
