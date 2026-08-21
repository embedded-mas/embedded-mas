// Inicializa o ROS: roscore
// Inicializa a ponte de comunicação entre ROS e JAVA: roslaunch rosbridge_server rosbridge_websocket.launch

package embedded.mas.bridges.ros;

import java.util.concurrent.ConcurrentHashMap;
import embedded.mas.bridges.ros.ros.RosActionListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import embedded.mas.bridges.ros.ros.RosBridge;
import embedded.mas.bridges.jacamo.ros.RosBridge4EmbeddedMas;
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

	private static final Set<String> PRIMITIVE_TYPES = new HashSet<>(Set.of(
			"std_msgs/Bool", "std_msgs/msg/Bool",
			"std_msgs/Byte", "std_msgs/msg/UInt8",
			"std_msgs/Char", "std_msgs/msg/Int8",			
			"std_msgs/Float32", "std_msgs/msg/Float32",
			"std_msgs/Float64", "std_msgs/msg/Float64",
			"std_msgs/String", "std_msgs/msg/String",
			"std_msgs/Time", "builtin_interfaces/msg/Time",
			"std_msgs/Duration", "builtin_interfaces/msg/Duration"
			));

	private static final Set<String> INTEGER_TYPES = new HashSet<>(Set.of(
			"std_msgs/Int8", "std_msgs/msg/Int8",
			"std_msgs/UInt8", "std_msgs/msg/UInt8",
			"std_msgs/Int16", "std_msgs/msg/Int16",
			"std_msgs/UInt16", "std_msgs/msg/UInt16",
			"std_msgs/Int32", "std_msgs/msg/Int32",
			"std_msgs/UInt32", "std_msgs/msg/UInt32",
			"std_msgs/Int64", "std_msgs/msg/Int64",
			"std_msgs/UInt64", "std_msgs/msg/UInt64"
			));

	/*
	 * beliefName is a map where the key is the name of the topic and the value is the corresponding belief functor
	 */
	private HashMap<String, Literal> beliefName = new HashMap<>();


	/*
	 * paramsToIgnore is a map where the key is the name of the topic and the value is a list of params to be ignore
	 */
	private HashMap<String, ArrayList<String>> paramsToIgnore = new HashMap<>();

	/* topicValues is a hash where the key is the topic name and the value is the topic value.
	 * It stores all the current read node values. When a node value changes, it is added to the hash table and updated 
	 * accordingly.
	 * 
	 * The agent gets a list of all stored values when it gets perceptions.  
	 * 
	 * */
	private HashMap<String, Literal> topicValues = new HashMap<>();

	RosBridge bridge = new RosBridge4EmbeddedMas();
	private String connection=null;

	private final ConcurrentHashMap<String, RosActionGoal> actionGoals =
			new ConcurrentHashMap<String, RosActionGoal>();

	private final ConcurrentHashMap<String, Literal> actionValues =
			new ConcurrentHashMap<String, Literal>();

	private final RosActionListener actionListener =
			new RosActionListener() {

		@Override
		public void onFeedback(
				String goalId,
				String actionName,
				JsonNode values) {

			RosActionGoal goal;

			goal = actionGoals.get(goalId);

			if (goal == null) {
				return;
			}

			goal.setStatus(
					RosActionGoal.STATUS_EXECUTING
			);

			goal.setLastFeedback(values);

			updateStatusBelief(goal);
			updateFeedbackBelief(goal, values);
		}

		@Override
		public void onResult(
				String goalId,
				String actionName,
				int status,
				boolean success,
				JsonNode values) {

			RosActionGoal goal;

			goal = actionGoals.get(goalId);

			if (goal == null) {
				return;
			}

			goal.setStatus(status);
			goal.setResult(values);

			actionValues.remove(
					"feedback:" + goalId
			);

			updateStatusBelief(goal);
			updateResultBelief(goal, values);

			actionGoals.remove(goalId);
		}
	};

	@Override
	public String sendActionGoal(
			RosActionClientAction action,
			Object[] arguments) {

		String goalId;
		ServiceParameters parameters;
		JsonNode goalArguments;
		RosActionGoal goal;
		boolean sent;

		if (action == null) {
			throw new IllegalArgumentException(
					"The ROS action cannot be null."
			);
		}

		goalId = UUID.randomUUID().toString();

		parameters = action.buildGoalParameters(
				arguments
		);

		goalArguments = parameters.toJson();

		if (goalArguments == null) {
			throw new IllegalStateException(
					"Could not convert the action goal to JSON."
			);
		}

		goal = new RosActionGoal(
				goalId,
				action
		);

		goal.setStatus(
				RosActionGoal.STATUS_ACCEPTED
		);

		this.actionGoals.put(
				goalId,
				goal
		);

		updateStatusBelief(goal);

		sent = this.bridge.sendActionGoal(
				goalId,
				action.getRosActionName(),
				action.getActionType(),
				goalArguments,
				action.isFeedbackEnabled(),
				this.actionListener
		);

		if (sent == false) {
			this.actionGoals.remove(goalId);
			this.actionValues.remove(
					"status:" + goalId
			);

			throw new IllegalStateException(
					"Could not send ROS action goal."
			);
		}

		return goalId;
	}

	@Override
	public boolean cancelActionGoal(
			RosActionCancelAction cancelAction,
			String goalId) {

		RosActionGoal goal;
		RosActionClientAction configuredAction;
		boolean sent;

		if (cancelAction == null) {
			return false;
		}

		if (goalId == null) {
			return false;
		}

		goal = this.actionGoals.get(goalId);

		if (goal == null) {
			return false;
		}

		configuredAction =
				cancelAction.getTargetAction();

		if (configuredAction.getRosActionName().equals(
				goal.getAction().getRosActionName()
		) == false) {

			return false;
		}

		sent = this.bridge.cancelActionGoal(
				goalId,
				configuredAction.getRosActionName()
		);

		if (sent) {
			goal.setStatus(
					RosActionGoal.STATUS_CANCELING
			);

			updateStatusBelief(goal);
		}

		return sent;
	}

	private void updateStatusBelief(
        RosActionGoal goal) {

    String beliefName;
    String literalText;
    Literal literal;

    beliefName =
            goal.getAction().getStatusBelief();

    if (beliefName == null) {
        return;
    }

    if (beliefName.isBlank()) {
        return;
    }

    literalText =
            beliefName
            + "(\""
            + goal.getGoalId()
            + "\","
            + goal.getStatusName()
            + ")";

    try {
        literal = parseLiteral(literalText);

        this.actionValues.put(
                "status:" + goal.getGoalId(),
                literal
        );
    } catch (ParseException exception) {
        exception.printStackTrace();
    } catch (TokenMgrError error) {
        error.printStackTrace();
    }
}

private void updateFeedbackBelief(
			RosActionGoal goal,
			JsonNode values) {

		String beliefName;
		String arguments;
		String literalText;
		Literal literal;

		beliefName =
				goal.getAction().getFeedbackBelief();

		if (beliefName == null) {
			return;
		}

		if (beliefName.isBlank()) {
			return;
		}

		arguments = "\""
				+ goal.getGoalId()
				+ "\"";

		if (values != null) {
			String convertedValues;

			convertedValues =
					jsonToPredArguments(values);

			if (convertedValues != null) {
				if (convertedValues.isBlank() == false) {
					arguments =
							arguments
							+ ","
							+ convertedValues;
				}
			}
		}

		literalText =
				beliefName
				+ "("
				+ arguments
				+ ")";

		try {
			literal = parseLiteral(literalText);

			this.actionValues.put(
					"feedback:" + goal.getGoalId(),
					literal
			);
		} catch (ParseException exception) {
			exception.printStackTrace();
		} catch (TokenMgrError error) {
			error.printStackTrace();
		}
	}

	private void updateResultBelief(
			RosActionGoal goal,
			JsonNode values) {

		String beliefName;
		String arguments;
		String literalText;
		Literal literal;

		beliefName =
				goal.getAction().getResultBelief();

		if (beliefName == null) {
			return;
		}

		if (beliefName.isBlank()) {
			return;
		}

		arguments = "\""
				+ goal.getGoalId()
				+ "\","
				+ goal.getStatusName();

		if (values != null) {
			String convertedValues;

			convertedValues =
					jsonToPredArguments(values);

			if (convertedValues != null) {
				if (convertedValues.isBlank() == false) {
					arguments =
							arguments
							+ ","
							+ convertedValues;
				}
			}
		}

		literalText =
				beliefName
				+ "("
				+ arguments
				+ ")";

		try {
			literal = parseLiteral(literalText);

			this.actionValues.put(
					"result:" + goal.getGoalId(),
					literal
			);
		} catch (ParseException exception) {
			exception.printStackTrace();
		} catch (TokenMgrError error) {
			error.printStackTrace();
		}
	}


	//TODO: throw exception when topics and types have different sizes	
	public DefaultRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types) {
		createDefaultRos4EmbeddedMas(connectionStr, topics, types, null, null);
	}


	//TODO: throw exception when topics and types have different sizes	
	public DefaultRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String>beliefNames) {
		createDefaultRos4EmbeddedMas(connectionStr, topics, types, beliefNames, null);
	}

	public DefaultRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String>beliefNames, HashMap<String, ArrayList<String>> ignoreParams) {
		createDefaultRos4EmbeddedMas(connectionStr, topics, types, beliefNames, ignoreParams);
	}


	private void createDefaultRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String>beliefNames, HashMap<String, ArrayList<String>> ignoreParams ) {
		this.connection = connectionStr;
		bridge.connect(connectionStr, true);

		//TODO: throw exception when topics and belief names have different sizes
		if(beliefNames==null) {
			for(String s:topics)
				this.beliefName.put(s.replaceAll("/", "_"), createAtom(s.replaceAll("/", "_")));
		}else{
			for(int i=0;i<topics.size();i++) {
				this.beliefName.put(topics.get(i).replaceAll("/", "_"), createAtom(beliefNames.get(i).replaceAll("/", "_")));
			}
		}

		this.paramsToIgnore = ignoreParams;

		listener = new RosListenDelegate() {
			public void receive(JsonNode data, String stringRep) {
				synchronized(mensagens){
					try {						
							Literal p = customizeBelief(data.get("topic").textValue(),data.get("msg"));
						if(p==null) {
							Literal functor = beliefName.get(data.get("topic").textValue().replaceAll("/", "_"));
							String terms;
							
							//check interest params
							ArrayList<String> ignoreParameters = ignoreParams.get(data.get("topic").textValue());
							
							if(data.get("msg").size()==1&&data.get("msg").get("data")!=null) //basic case: single data
								terms = jsonToPredArguments(data.get("msg").get("data"));
							else	
								//aqui: incluir interestParams
								terms = jsonToPredArguments(data.get("msg"), ignoreParameters);
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

	public List<Literal> rosRead() {
		ArrayList<Literal> result;

		result = new ArrayList<Literal>();

		synchronized (this.topicValues) {
			result.addAll(
					this.topicValues.values()
			);
		}

		result.addAll(
				this.actionValues.values()
		);

		return result;
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
		if(INTEGER_TYPES.contains(type))
			pub.publish(new PrimitiveMsg<Integer>(Integer.parseInt(s)));
		else
			if(PRIMITIVE_TYPES.contains(type))
				pub.publish(new PrimitiveMsg<String>(s));
			else
				try {
					pub.publish(new ObjectMapper().readTree(s));
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

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
		if(action instanceof TopicWritingAction) 
			rosWrite(((TopicWritingAction)action).getTopicName(), ((TopicWritingAction)action).getTopicType(), ((TopicWritingAction)action).getValue().toString());
		else
			if(action instanceof ServiceRequestAction) {
				serviceRequest(((ServiceRequestAction)action).getServiceName(), ((ServiceRequestAction)action).getServiceParameters().toJson());
			}
	}





}
