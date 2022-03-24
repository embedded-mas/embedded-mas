// roscore
// roslaunch rosbridge_server rosbridge_websocket.launch
// rostopic pub ros_to_java std_msgs/String "Ligar"
// rostopic pub ros_to_java std_msgs/String "Desligar"

package comunicaArduino.InterfaceJavaROS;

import com.fasterxml.jackson.databind.JsonNode;

import ros.Publisher;
import ros.RosBridge;
//import ros.RosBridge.RosBridgeSubscriber;
import ros.RosListenDelegate;
//import ros.SubscriptionRequestMsg;
import ros.msgs.std_msgs.PrimitiveMsg;
import ros.tools.MessageUnpacker;
//import ros.tools.PeriodicPublisher;
import java.util.*;
//import comunicaArduino.Arduino_Java;

/**
 * Example of connecting to rosbridge with publish/subscribe messages. Takes one argument:
 * the rosbridge websocket URI; for example: ws://localhost:9090.
 * @author James MacGlashan.
 */
public class Ros4EmbeddedMas implements IPhysicalInterface{

	private RosListenDelegate listener = null;
	private ArrayList<String> mensagens = new ArrayList<String>();

    public Ros4EmbeddedMas() {
		RosBridge bridge = new RosBridge();
        bridge.connect("ws://localhost:9090", true);

		listener = new RosListenDelegate() {
            public void receive(JsonNode data, String stringRep) {
                MessageUnpacker<PrimitiveMsg<String>> unpacker = new MessageUnpacker<PrimitiveMsg<String>>(PrimitiveMsg.class);
                PrimitiveMsg<String> msg = unpacker.unpackRosMessage(data);
				synchronized(mensagens){
					mensagens.add(msg.data.toString());
				}
				//System.out.println("msg.data: "+msg.data.toString());
				//System.out.println("size: "+mensagens.size());
                //m = msg.data.toString();
            }
        };

		bridge.subscribe("/chatter", "std_msgs/String", listener, 1, 1);
	}
	
	@Override
	public String read() {       
		String mensagem = rosRead();
		return mensagem;
	}
	
	@Override
	public boolean write(String s) {
		try {
			rosWrite(s);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

    public void rosWrite(String s){
        RosBridge bridge = new RosBridge();
        bridge.connect("ws://localhost:9090", true);

        Publisher pub = new Publisher("/javaToROS", "std_msgs/String", bridge);
        pub.publish(new PrimitiveMsg<String>(s));
    }

    public String rosRead(){

		String m;

		synchronized(mensagens){
			if(mensagens.size()==0){
				//System.out.println("Vetor mensagens vazio!");
				return null;
			}

			m = mensagens.get(0);

			mensagens.remove(0);
		}

        return m;
    }
}