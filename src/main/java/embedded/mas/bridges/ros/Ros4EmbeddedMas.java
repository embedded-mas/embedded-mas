// Inicializa o ROS: roscore
// Inicializa a ponte de comunicação entre ROS e JAVA: roslaunch rosbridge_server rosbridge_websocket.launch

package embedded.mas.bridges.ros;

import com.fasterxml.jackson.databind.JsonNode;

import embedded.mas.bridges.jacamo.IPhysicalInterface;
import embedded.mas.bridges.ros.ros.RosBridge;
import embedded.mas.bridges.ros.ros.RosListenDelegate;
import embedded.mas.bridges.ros.ros.Publisher;
import embedded.mas.bridges.ros.ros.msgs.std_msgs.PrimitiveMsg;
import embedded.mas.bridges.ros.ros.tools.MessageUnpacker;

import java.util.*;

/**
 * Example of connecting to rosbridge with publish/subscribe messages. Takes one argument:
 * the rosbridge websocket URI; for example: ws://localhost:9090.
 * @author James MacGlashan.
 */
public class Ros4EmbeddedMas implements IPhysicalInterface{

	private RosListenDelegate listener = null;
	private ArrayList<String> mensagens = new ArrayList<String>();

	private String preamble = "==";
	private String startMessage = "::";
	private String endMessage = "--";

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
	
	public Ros4EmbeddedMas(int port, String node) {
		RosBridge bridge = new RosBridge();
		bridge.connect("ws://localhost:" + port, true);

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

		bridge.subscribe(node, "std_msgs/String", listener, 1, 1);
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
		
		if(m.equals("") ) return "";

		String s = "";
		String start = "";
		String end = "";





		//comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		//byte[] b = new byte[1];

		byte[] mBytes = m.getBytes();
		byte[] b = new byte[1];
		b[0] = mBytes[0];
		int pos = 0;

		while (!start.equals(preamble)) {
			if((char)b[0] == preamble.charAt(start.length())) {
				start = start + (char)b[0];
			}else {
				start = "";
			}
			b[0] = mBytes[++pos];
		}

		while (pos<mBytes.length &&  !end.equals(endMessage)) {
			if((char)b[0] == endMessage.charAt(end.length())) {
				end = end + (char)b[0];
			}else {
				s = s + end + (char)b[0];
				end = "";
			}
			if(pos<mBytes.length-1)
			   b[0] = mBytes[++pos];
		}

		String[] strings = s.split(startMessage);
		int number = Integer.parseInt(strings[0]);
		String message = strings[1];

		if(number == message.length()) {
			return message;
		}
		else {
			return "Message conversation error";
		}



	}

	//return m;
}
