// Inicializa o ROS: roscore
// Inicializa a ponte de comunicação entre ROS e JAVA: roslaunch rosbridge_server rosbridge_websocket.launch

package embedded.mas.bridges.ros;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import embedded.mas.bridges.ros.ros.RosBridge;
import embedded.mas.bridges.ros.ros.RosListenDelegate;
import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.ros.ros.Publisher;
import embedded.mas.bridges.ros.ros.msgs.std_msgs.PrimitiveMsg;
import static embedded.mas.bridges.jacamo.Utils.jsonToPredArguments;

import jason.asSyntax.Literal;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;
import static jason.asSyntax.ASSyntax.parseLiteral;
import static jason.asSyntax.ASSyntax.createAtom;

import java.util.*;


/**
 * Example of connecting to rosbridge with publish/subscribe messages. Takes one argument:
 * the rosbridge websocket URI; for example: ws://localhost:9090.
 * @author James MacGlashan.
 */
public class DefaultRos4EmbeddedMas implements IRosInterface{

	private RosListenDelegate listener = null;
	private ArrayList<Literal> mensagens = new ArrayList<Literal>();
	
	
	/*
	 * beliefName is a map where the key is the name of the topic and the value is the corresponding belief functor
	 */
	private HashMap<String, Literal> beliefName = new HashMap<>();

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
			createDefaultRos4EmbeddedMas(connectionStr, topics, types, null);
	}

	
	//TODO: throw exception when topics and types have different sizes	
	public DefaultRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String>beliefNames) {
		createDefaultRos4EmbeddedMas(connectionStr, topics, types, beliefNames);
	}


	private void createDefaultRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String>beliefNames) {
		this.connection = connectionStr;
		bridge.connect(connectionStr, true);
		
		//TODO: throw exception when topics and belief names have different sizes
		if(beliefNames==null) {
			for(String s:topics)
				this.beliefName.put(s.replaceAll("/", "_"), createAtom(s.replaceAll("/", "_")));
		}else{
			for(int i=0;i<topics.size();i++) {
				this.beliefName.put(topics.get(i).replaceAll("/", "_"), createAtom(beliefNames.get(i)));
			}
		}
		

		listener = new RosListenDelegate() {
			public void receive(JsonNode data, String stringRep) {
				synchronized(mensagens){
					try {						
						Literal p = customizeBelief(data.get("topic").textValue(),data.get("msg"));
						if(p==null) {
							Literal functor = beliefName.get(data.get("topic").textValue().replaceAll("/", "_"));
							String terms;
							if(data.get("msg").size()==1&&data.get("msg").get("data")!=null) //basic case: single data
								terms = jsonToPredArguments(data.get("msg").get("data"));
							else	
								terms = jsonToPredArguments(data.get("msg"));
							p = parseLiteral(functor+"("+terms+")");
						}
						synchronized (topicValues) {
							topicValues.put(data.get("topic").toString().replaceAll("^\"|\"$", ""),p);	
						}						
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

	

	@Override
	public List<Literal> read() {		
		return rosRead();
	}

	public List<Literal> rosRead(){
		ArrayList<Literal> list = new ArrayList<Literal>();
		synchronized (topicValues) { 
			//for(Literal l:topicValues.values())
			//list.add(l);	
			return new ArrayList<Literal>(topicValues.values());
		}
		

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
			if(type.equals("geometry_msgs/Pose")) //TODO: handle application specific message types in application-customized extensions of DefaultRos4EmbeddedMas
				try {
					pub.publish(new ObjectMapper().readTree(s));
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			else
				pub.publish(new PrimitiveMsg<String>(s));
	}

	@Override
	public boolean serviceRequest(String serviceName, JsonNode serviceArguments) {
		return this.bridge.doServiceRequest(serviceName, serviceArguments);
	}


	public JsonNode serviceRequestResponse(String serviceName, JsonNode serviceArguments) {
		JsonNode response = this.bridge.doServiceRequestResponse(serviceName, serviceArguments); 		
		return response;
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

	@Override
	public void execEmbeddedAction(EmbeddedAction action) {
		/*if(action instanceof TopicWritingAction) 
			rosWrite(((TopicWritingAction)action).getTopicName(), ((TopicWritingAction)action).getTopicType(), ((TopicWritingAction)action).getValue().toString());
		else
			if(action instanceof ServiceRequestAction) {
				serviceRequest(((ServiceRequestAction)action).getServiceName(), ((ServiceRequestAction)action).getServiceParameters().toJson());
			}*/
	}

}
