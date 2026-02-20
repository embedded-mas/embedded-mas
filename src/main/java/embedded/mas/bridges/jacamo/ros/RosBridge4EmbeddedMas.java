package embedded.mas.bridges.jacamo.ros;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import embedded.mas.bridges.ros.ros.RosBridge;
import embedded.mas.log.EMasLogger;

@WebSocket
public class RosBridge4EmbeddedMas extends RosBridge{

   private EMasLogger logger = EMasLogger.getInstance();
   
   @Override
   public void publish(String topic, String type, Object msg){

		if(this.session == null){
			throw new RuntimeException("Rosbridge connection is closed. Cannot publish. Attempted Topic Publish: " + topic);
		}

		this.advertise(topic, type);

		Map<String, Object> jsonMsg = new HashMap<String, java.lang.Object>();
		jsonMsg.put("op", "publish");
		jsonMsg.put("topic", topic);
		jsonMsg.put("type", type);
		jsonMsg.put("msg", msg);

		JsonFactory jsonFactory = new JsonFactory();
		StringWriter writer = new StringWriter();
		JsonGenerator jsonGenerator;
		ObjectMapper objectMapper = new ObjectMapper();

		try {			
			jsonGenerator = jsonFactory.createGenerator(writer);
			objectMapper.writeValue(jsonGenerator, jsonMsg);
		} catch(Exception e){
			System.out.println("Error");
		}

		String jsonMsgString = writer.toString();
		logger.fine("publishing to the ROS topic " + topic + ". Topic type: " + type + ". Message: " + jsonMsgString);
		Future<Void> fut;
		try{
			fut = session.getRemote().sendStringByFuture(jsonMsgString);
			fut.get(2, TimeUnit.SECONDS);
		}catch (Throwable t){
			System.out.println("Error publishing to " + topic + " with message type: " + type);
			t.printStackTrace();
		}

	}
   
   @Override
   public void publishJsonMsg(String topic, String type, String jsonMsg){
      logger.fine("publishing to the ROS topic " + topic + ". Topic type: " + type + ". Message: " + jsonMsg);
      super.publishJsonMsg(topic, type, jsonMsg);
   
   }
   
   @Override
   public void sendRawMessage(String message){
      logger.fine("sending message to ROS: " + message);
      super.sendRawMessage(message);
   }

}
