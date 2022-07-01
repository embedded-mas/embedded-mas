// Inicializa o ROS: roscore
// Inicializa a ponte de comunicação entre ROS e JAVA: roslaunch rosbridge_server rosbridge_websocket.launch

package embedded.mas.bridges.ros;

import com.fasterxml.jackson.databind.JsonNode;

import embedded.mas.bridges.ros.ros.RosBridge;
import embedded.mas.bridges.ros.ros.RosListenDelegate;
import embedded.mas.bridges.ros.ros.Publisher;
import embedded.mas.bridges.ros.ros.msgs.std_msgs.PrimitiveMsg;
import embedded.mas.bridges.ros.ros.tools.MessageUnpacker;

import jason.asSyntax.Literal;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;
import static jason.asSyntax.ASSyntax.parseLiteral;

import java.util.*;


/**
 * Example of connecting to rosbridge with publish/subscribe messages. Takes one argument:
 * the rosbridge websocket URI; for example: ws://localhost:9090.
 * @author James MacGlashan.
 */
public class DefaultRos4EmbeddedMas implements IRosInterface{

	private RosListenDelegate listener = null;
	private ArrayList<Literal> mensagens = new ArrayList<Literal>();

	/* topicValues is a hash where the key is the topic name and the value is the topic value.
	 * It stores all the current read node values. When a node value changes, it is added to the hash table and updated 
	 * accordingly.
	 * 
	 * The agent gets a list of all stored values when it gets perceptions.  
	 * 
	 * */
	private HashMap<String, Literal> topicValues = new HashMap<>();

	RosBridge bridge = new RosBridge();
	private String connection=null;



	//TODO: throw exception when topics and types have different sizes	
	public DefaultRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types) {


		//bridge.connect("ws://localhost:" + port, true);
		this.connection = connectionStr;
		bridge.connect(connectionStr, true);

		listener = new RosListenDelegate() {
			public void receive(JsonNode data, String stringRep) {
				synchronized(mensagens){
					try {						
						Literal p = customizeBelief(data.get("topic").textValue(),data.get("msg"));
						if(p==null) {
							Literal functor = parseLiteral(data.get("topic").textValue().replaceAll("/", "_"));
							String terms = jsonToPredArguments(data.get("msg"));
							p = parseLiteral(functor+"("+terms+")");
						}
						topicValues.put(data.get("topic").toString().replaceAll("^\"|\"$", ""),p);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TokenMgrError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		for(int i=0;i<topics.size();i++) {
			bridge.subscribe(topics.get(i), types.get(i), listener, 1, 1);
		}

	}

	/**/
	private static  String jsonToPredArguments(JsonNode node) {
		String s = "", f;
		Iterator<String> fields = node.fieldNames();
		if(node.size()==1) { 
			f = fields.next();
			if(f.equals("data")) 
				s =  node.get(f).toString(); //basic case: single value
			else
				s = f+"("+ node.get(f)+")";
		}else
			while(fields.hasNext()) { //iterate over JSON fields
				f = fields.next(); 
				if((node.get(f).isObject())) { //check whether the current JSON field is a nested JSON
					s = s.concat(f+"(").concat(jsonToPredArguments(node.get(f))).concat(")"); //recursively handling nested JSON
					if(fields.hasNext()) s = s.concat(",");
				}
				else {
					s = s.concat( f + "(" + node.get(f)+")");
					if(fields.hasNext()) s = s.concat(",");
				}

			}
		return s;
	}


	@Override
	public List<Literal> read() {		
		return rosRead();
	}

	public List<Literal> rosRead(){
		return new ArrayList<Literal>(topicValues.values());

	}

	@Override
	public boolean write(String topic, String type, String s) {
		try {
			rosWrite(topic, type, s);
			return true;
		}catch(Exception e) {
			return false;
		}
	}


	public void rosWrite(String topic, String type, String s){
		Publisher pub = new Publisher(topic, type, bridge);
		if(type.equals("std_msgs/Int32"))
			pub.publish(new PrimitiveMsg<Integer>(Integer.parseInt(s)));
		else
			pub.publish(new PrimitiveMsg<String>(s));
	}

	@Override
	public boolean serviceRequest(String serviceName, JsonNode serviceArguments) {
		return this.bridge.doServiceRequest(serviceName, serviceArguments);
	}

	
	public JsonNode serviceRequestResponse(String serviceName, JsonNode serviceArguments) {
		return this.bridge.doServiceRequestResponse(serviceName, serviceArguments);
	}


	/**
	 * Customizes beliefs when should not follow the default format translated from rostopics.
	 * By default, returns null. In this case, the belief follows the default translation from the rostopic.
	 * To return values other than null, override this method in subclasses. In this case, the method is supposed to build and return a literal representing
	 * the belief corresponding to the topic. 
	 * 
	 * @param topicName: name of the topic to be converted to belief.
	 * @param data: JSON containing the topic values. This JSON is produced by the rosbridge, which is an interface ROS-Java. To inspect this JSON, print the value of this parameter. 
	 * @return null to follow the default conversion from topic value to belief; a literal representing the corresponding belief otherwise.
	 */
	protected Literal customizeBelief(String topicName, JsonNode data) {
		return null;
	}

}
