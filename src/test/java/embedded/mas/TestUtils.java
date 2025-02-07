package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import embedded.mas.bridges.jacamo.Utils;

import static embedded.mas.bridges.jacamo.Utils.jsonToPredArguments;

public class TestUtils {

	@Test
	public void testjsonToPredArgumentsWithIgnoreParams() {
		String json = "{\"x\":5.544444561004639,\"y\":5.544444561004639,\"theta\":0.0,\"linear_velocity\":0.0,\"angular_velocity\":0.0}";
		ObjectMapper objectMapper = new ObjectMapper();
	    try {	    	
			JsonNode jsonNode = objectMapper.readTree(json);			
			ArrayList<String> l = new ArrayList<>();
			//test with an empty list - no param to ignore
			assertEquals(Utils.jsonToPredArguments(jsonNode, l), "x(5.544444561004639),y(5.544444561004639),theta(0.0),linear_velocity(0.0),angular_velocity(0.0)");
			l.add("theta"); 
			l.add("linear_velocity");
			assertEquals(Utils.jsonToPredArguments(jsonNode, l), "x(5.544444561004639),y(5.544444561004639),angular_velocity(0.0)");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		
		assertTrue(true);
	}
	
	@Test
	public void testjsonToPredArgumentsInterestParams() {
		String json = "{\"x\":5.544444561004639,\"y\":5.544444561004639,\"theta\":0.0,\"linear_velocity\":0.0,\"angular_velocity\":0.0}";
		ObjectMapper objectMapper = new ObjectMapper();
	    try {	    	
			JsonNode jsonNode = objectMapper.readTree(json);			
			ArrayList<String> l = new ArrayList<>();
			//test with an empty list - no param to ignore
			assertEquals(Utils.jsonToPredArgumentsWithParamsToInclude(jsonNode, l).length(), 0);
			l.add("x"); 
			l.add("y");
			jsonNode = objectMapper.readTree(json);
			assertEquals(Utils.jsonToPredArgumentsWithParamsToInclude(jsonNode, l), "x(5.544444561004639),y(5.544444561004639)");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}

}
