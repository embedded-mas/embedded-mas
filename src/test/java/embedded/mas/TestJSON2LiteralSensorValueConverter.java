package embedded.mas;

import static org.junit.Assert.*;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Test;

import embedded.mas.bridges.jacamo.JsonObject2LiteralSensorValueConverter;

public class TestJSON2LiteralSensorValueConverter {

	@Test
	public void testConvert() {
		JsonObject obj1 = Json.createObjectBuilder()
                .add("object1", 1)
                .build();
		
		
		JsonObject obj2 = Json.createObjectBuilder()
                .add("object2", Json.createArrayBuilder()
                        .add(1)
                        .add(2)
                        .add(3))
                .build();

		
		JsonObject obj3 = Json.createObjectBuilder()
                .add("object3.1", 1)
                .add("object3.2", Json.createArrayBuilder()
                        .add(1).add(2))
                .build();
		
		System.out.println(obj1);
		System.out.println(obj2);
		System.out.println(obj3);
		
		JsonObject2LiteralSensorValueConverter converter = new JsonObject2LiteralSensorValueConverter();
		
		assertEquals(converter.convert(obj1).toString(), "object1(1)");
		assertEquals(converter.convert(obj2).toString(), "object2(1,2,3)");	
		
		System.out.println(converter.convert(obj3));
		
	}
	
	

}
