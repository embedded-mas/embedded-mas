/*
 * 
 * 
 * 
 * 
--- # Devices
- id: device1
  className:  embedded.mas.bridges.jacamo.JSONDevice
  microcontroller: 
      id: arduino1
      className: Arduino4EmbeddedMas
      serial: "/dev/ttyUSB0"
      baudRate: 9600

- id: device2
  className:  embedded.mas.bridges.jacamo.JSONDevice
  microcontroller: 
      id: arduino1
      className: Arduino4EmbeddedMas
      serial: "/dev/ttyUSB0"
      baudRate: 9600  



 */

package embedded.mas.bridges.jacamo.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IExternalInterface;
import embedded.mas.bridges.jacamo.SerialEmbeddedAction;
import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import embedded.mas.bridges.javard.NRJ4EmbeddedMas;
import embedded.mas.bridges.ros.DefaultRos4Bdi;
import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;
import embedded.mas.bridges.ros.ServiceArrayMsgParam;
import embedded.mas.bridges.ros.ServiceArrayParam;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import embedded.mas.bridges.ros.ServiceRequestAction;
import embedded.mas.bridges.ros.TopicWritingAction;
import embedded.mas.exception.InvalidActuationException;
import embedded.mas.exception.InvalidActuatorException;
import embedded.mas.exception.InvalidDeviceException;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.Rule;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;

import static jason.asSyntax.ASSyntax.createAtom;
import static jason.asSyntax.ASSyntax.parseRule;

import static embedded.mas.bridges.ros.ServiceParam.createServiceParam;

public class DefaultConfig {
	private enum CLASS_NAME_KEY {className, class_name};  //possible keys for the class name
	private enum ROS_CONNECTION_STRING_KEY {connectionString, connection_string};  //possible keys for ROS connection string
	private enum ROS_SENSOR_KEY {perception, perceptionTopics, perception_topics, sensors};  //possible keys for ROS pereception configuration in the YAML file	
	private enum PERCEPTION_ID_KEY {beliefName, belief_name, percept_name};  //possible keys for ROS pereception configuration in the YAML file
	private enum TOPIC_NAME_KEY {topicName, topic_name};  //possible keys for ROS topic name in the YAML file
	private enum TOPIC_TYPE_KEY {topicType, topic_type};  //possible keys for ROS topic type in the YAML file
	private enum TOPIC_ACTION_KEY {topicWritingActions, topic_writing_actions};  //possible keys for ROS topic actions in the YAML file
	private enum SERVICE_ACTION_KEY {serviceRequestActions, service_request_actions};  //possible keys for ROS serivce actions in the YAML file
	private enum ACTION_NAME_KEY {actionName, action_name};  //possible keys for ROS action name in the YAML file
	private enum SERVICE_NAME_KEY {serviceName, service_name};  //possible keys for ROS service name in the YAML file
	private enum IGNORE_VALUES_KEY {ignoreValues, ignore_values};  //possible keys for ingore values in the YAML file

	private List<DefaultDevice> devices = new ArrayList<DefaultDevice>();


	private Arduino4EmbeddedMas createArduino4EmbeddedMas(String serialPort, int baudRate) {
		Arduino4EmbeddedMas a = new Arduino4EmbeddedMas(serialPort, baudRate);
		return a;
	}

	private NRJ4EmbeddedMas createNRJ4EmbeddedMas(String serialPort, int baudRate) {
		NRJ4EmbeddedMas a = null;
		try {
			a = new NRJ4EmbeddedMas(serialPort, baudRate);
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	private DefaultRos4EmbeddedMas createRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String> beliefNames, HashMap<String, ArrayList<String>> paramsToIgnore) {
		return new DefaultRos4EmbeddedMas(connectionStr, topics, types, beliefNames, paramsToIgnore);

	}

	private DefaultRos4Bdi createRos4Bdi(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String> beliefNames, HashMap<String, ArrayList<String>> paramsToIgnore) {
		return new DefaultRos4Bdi(connectionStr, topics, types, beliefNames, paramsToIgnore);

	}


	private DemoDevice createDemoDevice(String id) {
		return new DemoDevice(createAtom(id));

	}

	public <T> boolean isExternalInterface(Class<T> className){
		Class[] classes = className.getInterfaces();
		if(classes.length == 0) return false;		
		for(int i=0;i<classes.length;i++) { 
			if(classes[i] == IExternalInterface.class) return true;
			return isExternalInterface(classes[0]);
		}
		return false;
	}

	public <T> Class  getIExternalDevice(Class<T> className){
		Class[] classes = className.getInterfaces();
		if(classes.length>0) {
			/*//example access 1
			System.out.println("????" + className.getName() + " - " + className.getInterfaces().length + " - " + className.getInterfaces()[0].getClass().toString());
			System.out.println(
					"Interfaces of myClass: "
							+ Arrays.toString(
									className.getInterfaces()));

			Class[] classes = className.getInterfaces();
			System.out.println( "Interfaces of myClass::: "  + Arrays.toString( classes));
			System.out.println( "Interfaces of myClass:::::: "  + classes[0].getName() );

			getInterface(classes[0]);
			 */

			for(int i=0;i<classes.length;i++) 
				if(isExternalInterface(classes[i]))
					return classes[i];			
		}
		return null;

	}

	/*private Class getInterface(Class c) {
		System.out.println("[getInterface] " + c.getClass().getName());
		Class[] classes = c.getInterfaces();
		for(int i=0;i<classes.length;i++) {
			System.out.println(classes[i].getClass().getName());
		}
		return null;
	}
	 */

	public HashMap<Atom, ActuationSequence> getActions(List<DefaultDevice> devices, String filename) throws InvalidDeviceException, InvalidActuationException, InvalidActuatorException{
		HashMap<Atom, ActuationSequence> actionsMap = new HashMap<Atom, ActuationSequence>();
		Yaml yaml = new Yaml();
		Iterable<Object> itr;
		try {
			itr = yaml.loadAll(new FileInputStream(filename));
			for (Object o : itr) { 
				ArrayList l = (ArrayList) o; //"l" is a list of JSON where each element is a single device configuration
				for(int i=0;i<l.size();i++) 
					if(((LinkedHashMap) l.get(i)).containsKey("actions")) {
						LinkedHashMap actions = (LinkedHashMap) l.get(i);
						if((actions.get("actions") instanceof ArrayList)) { //if there are some actions 
							ArrayList actionList = (ArrayList) actions.get("actions"); 
							for(int i1=0;i1<actionList.size();i1++) { //for each action...
								LinkedHashMap actionItem = (LinkedHashMap) actionList.get(i1);
								Iterator it = actionItem.keySet().iterator();
								if(it.hasNext()) {
									String actionName = it.next().toString(); //save the current action name
									ArrayList actuationSequence = (ArrayList) actionItem.get(actionName); //save the actuation sequence, which is a sequence of actuation sets
									ActuationSequence currentActuationSequence = new ActuationSequence(); //start a new actuation sequence
									String regex = "([^.]+)\\.([^.]+)\\.([^.]+)";
									Pattern pattern;
									Matcher matcher;
									for(int k=0;k<actuationSequence.size();k++) { //for each actuation set
										ArrayList actuationSet = (ArrayList) actuationSequence.get(k);
										ActuationSet currentActuationSet = new ActuationSet(); //start a new actuation set
										for(int n=0;n<actuationSet.size();n++){// for each element in the actuation set
											pattern = Pattern.compile(regex);
											matcher = pattern.matcher(actuationSet.get(n).toString());
											while (matcher.find()) {
												//find the device
												DefaultDevice currentDevice = null;
												for(DefaultDevice d:devices)
													if(d.getId().toString().equals(matcher.group(1)))
														currentDevice = d;			
												if(currentDevice==null) throw new InvalidDeviceException("Device " + matcher.group(1) + " not found.");

												if(currentDevice!=null) {
													boolean actuatorFound = false;
													Iterator<Actuator> actuatorIt = currentDevice.getActuators().iterator();
													while(actuatorIt.hasNext()) {
														Actuator currentActuator = actuatorIt.next();														
														if(currentActuator.getId().toString().equals(matcher.group(2))) { //check whether the device has an actuator that matches with the specified in the action
															actuatorFound = true;
															//check whether the actuator includes the actuation specified
															Iterator<Actuation> actuationIt = currentActuator.getActuations().iterator();
															boolean actuationFound = false;
															while(actuationIt.hasNext()) {
																Actuation currentActuation = actuationIt.next();
																if(currentActuation.getId().toString().equals(matcher.group(3))){
																	actuationFound = true;
																	ActuationDevice act = new ActuationDevice(currentDevice, currentActuator,currentActuation);
																	currentActuationSet.add(act);
																}
															}
															if(!actuationFound) throw new InvalidActuationException("Actuator " + matcher.group(1)+"."+matcher.group(2)+"."+ matcher.group(3) + " not found.");



														}
													}
													if(!actuatorFound) throw new InvalidActuatorException("Actuator " + matcher.group(1)+"."+matcher.group(2) + " not found.");
												}

											}												

										}
										currentActuationSequence.addLast(currentActuationSet);										
									}
									actionsMap.put(createAtom(actionName), currentActuationSequence);
								}
							}
						}
					}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	


		return actionsMap;
	}


	public List<Rule> getPerceptionRules(String fileName) {
		Yaml yaml = new Yaml();
		ArrayList<Rule> rules = new ArrayList<>();
		try {
			Iterable<Object> itr = yaml.loadAll(new FileInputStream(fileName));
			for (Object o : itr) { 
				ArrayList l = (ArrayList) o; //"l" is a list of JSON where each element is a single device configuration
				for(int i=0;i<l.size();i++) 
					if(((LinkedHashMap) l.get(i)).containsKey("perception_rules")) {
						ArrayList<String> sRules = (ArrayList) ((LinkedHashMap) l.get(i)).get("perception_rules");
						for(String s:sRules) {
							Rule rule = parseRule(s);
							rules.add(rule);
						}
					}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TokenMgrError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rules;

	}

	public List<Actuator> processActuators(List actuatorYaml){		
		ArrayList<Actuator> result = new ArrayList<Actuator>();		
		if(actuatorYaml!=null) {

			for(int i=0;i<actuatorYaml.size();i++){
				LinkedHashMap currentActuator = (LinkedHashMap)actuatorYaml.get(i);
				ArrayList actuationsList = (ArrayList) currentActuator.get("actuations");
				Actuator actuator = new Actuator(createAtom(currentActuator.get("actuator_id").toString()));
				for(int j = 0;j<actuationsList.size();j++) {
					LinkedHashMap currentActuation =  (LinkedHashMap) actuationsList.get(j);
					Actuation actuation = new Actuation(createAtom(currentActuation.get("actuation_id").toString()));
					if(currentActuation.get("parameters")!=null) {
						ArrayList parametersList = (ArrayList)currentActuation.get("parameters");
						for(int k=0;k<parametersList.size();k++)
							actuation.addParameter(createAtom(parametersList.get(k).toString()));
					}
					actuator.addActuation(actuation);					   
				}
				result.add(actuator);

			}
		}
		return result;
	}

	public List<DefaultDevice> loadFromYaml(String filename) {

		ArrayList<DefaultDevice> devices = new ArrayList<DefaultDevice>();

		IExternalInterface microcontroller = null;
		ArrayList<EmbeddedAction> embeddedActionList = new ArrayList<EmbeddedAction>();

		Yaml yaml = new Yaml();

		try {
			Iterable<Object> itr = yaml.loadAll(new FileInputStream(filename));	
			for (Object o : itr) { 
				ArrayList l = (ArrayList) o; //"l" is a list of JSON where each element is a single device configuration



				for(int i=0;i<l.size();i++) {
					if(((LinkedHashMap) l.get(i)).get("device_id")!=null) {
						LinkedHashMap item = (LinkedHashMap) l.get(i);
						String microcontrollerClassName = "";
						for(CLASS_NAME_KEY k: CLASS_NAME_KEY.values()) //iterate over all the possible keys that gather the class name
							if(((LinkedHashMap)item.get("microcontroller")).get(k.toString())!=null) {  
								microcontrollerClassName = ((LinkedHashMap)item.get("microcontroller")).get(k.toString()).toString();
								break;
							}


						//if the current device is a serial device
						if(microcontrollerClassName.equals("Arduino4EmbeddedMas")|
								microcontrollerClassName.equals("SerialReader")) {
							microcontroller= createArduino4EmbeddedMas(((LinkedHashMap)item.get("microcontroller")).get("serial").toString(),
									Integer.parseInt(((LinkedHashMap)item.get("microcontroller")).get("baudRate").toString()));
							ArrayList actionsArray = (ArrayList) item.get("serialActions");
							for(int j=0;j<actionsArray.size();j++) {
								String actionNameStr = "";
								for(ACTION_NAME_KEY k: ACTION_NAME_KEY.values()) //iterate over all the possible keys that refer to action name
									if(((LinkedHashMap)actionsArray.get(j)).get(k.toString())!=null) {  
										actionNameStr = ((LinkedHashMap)actionsArray.get(j)).get(k.toString()).toString();
										break;
									}

								SerialEmbeddedAction action  = new SerialEmbeddedAction(createAtom(actionNameStr), 
										createAtom(((LinkedHashMap)actionsArray.get(j)).get("actuationName").toString()));
								embeddedActionList.add(action);


							}
						}
						else
							if(microcontrollerClassName.equals("NRJ4EmbeddedMas")) {
								microcontroller= createNRJ4EmbeddedMas(((LinkedHashMap)item.get("microcontroller")).get("serial").toString(),
										Integer.parseInt(((LinkedHashMap)item.get("microcontroller")).get("baudRate").toString()));
								ArrayList actionsArray = (ArrayList) item.get("serialActions");
								for(int j=0;j<actionsArray.size();j++) {
									String actionNameStr = "";
								    for(ACTION_NAME_KEY k: ACTION_NAME_KEY.values()) //iterate over all the possible keys that refer to action name
									   if(((LinkedHashMap)actionsArray.get(j)).get(k.toString())!=null) {  
										  actionNameStr = ((LinkedHashMap)actionsArray.get(j)).get(k.toString()).toString();
										   break;
									   }

									SerialEmbeddedAction action  = new SerialEmbeddedAction(createAtom(actionNameStr), 
											createAtom(((LinkedHashMap)actionsArray.get(j)).get("actuationName").toString()));
									embeddedActionList.add(action);


								}
							}
							else
								//if the current device is a ros node
								if(microcontrollerClassName.equals("DefaultRos4EmbeddedMas")|
										microcontrollerClassName.equals("DefaultRos4Bdi")) { //DefaultRos4Bdi is just an alias class for the names to make more sense in Jason-ROS applications
									//ArrayList perceptionTopics = (ArrayList) ((LinkedHashMap)item.get("microcontroller")).get("perceptionTopics");
									//ArrayList perceptionTopics = (ArrayList) item.get("perceptionTopics");
									ArrayList perceptionTopics = null;
									for(ROS_SENSOR_KEY k: ROS_SENSOR_KEY.values()) //iterate over all the possible keys that gather perception topics
										if(item.get(k.toString())!=null) {  
											perceptionTopics = (ArrayList) item.get(k.toString());
											break;
										}
									
									
									ArrayList<String> topics = new ArrayList<String>();
									ArrayList<String> types = new ArrayList<String>();
									ArrayList<String> beliefNames = new ArrayList<String>();
									HashMap<String, ArrayList<String>> ignoreParams = new HashMap<>();
									if(perceptionTopics!=null)
										for(int j=0;j<perceptionTopics.size();j++) {
											String topicName = "";	
											for(TOPIC_NAME_KEY k: TOPIC_NAME_KEY.values()) //iterate over all the possible keys that gather perception topic names
												if(((LinkedHashMap)perceptionTopics.get(j)).get(k.toString())!=null) {  
													topicName = ((LinkedHashMap)perceptionTopics.get(j)).get(k.toString()).toString();
													break;
												}

											topics.add(topicName);

											String topicType = "";										
											for(TOPIC_TYPE_KEY k: TOPIC_TYPE_KEY.values()) //iterate over all the possible keys that gather perception topic types
												if(((LinkedHashMap)perceptionTopics.get(j)).get(k.toString())!=null) {  
													topicType = ((LinkedHashMap)perceptionTopics.get(j)).get(k.toString()).toString();
													break;
												}
											types.add(topicType	);
											// if(((LinkedHashMap)perceptionTopics.get(j)).get("beliefName")==null)
											// 	beliefNames.add(((LinkedHashMap)perceptionTopics.get(j)).get("topicName").toString());
											// else
											// 	beliefNames.add(((LinkedHashMap)perceptionTopics.get(j)).get("beliefName").toString());	

											for(PERCEPTION_ID_KEY k: PERCEPTION_ID_KEY.values()) //iterate over all the possible keys that gather perception belief names
												if(((LinkedHashMap)perceptionTopics.get(j)).get(k.toString())!=null) {  
													beliefNames.add(((LinkedHashMap)perceptionTopics.get(j)).get(k.toString()).toString());
													break;
												}
											if(beliefNames.size()<j+1) //if no belief name was found, use the topic name as the belief name
												beliefNames.add(topicName);
											ArrayList tempParams =  null;
											for(IGNORE_VALUES_KEY k: IGNORE_VALUES_KEY.values()) //iterate over all the possible keys that gather perception belief names
												if(((LinkedHashMap)perceptionTopics.get(j)).get(k.toString())!=null) {  
													tempParams =  (ArrayList) ((LinkedHashMap)perceptionTopics.get(j)).get(k.toString());
													break;
												}
											ignoreParams.put(topicName, tempParams);
										}

									String connectionStr = "";
									for(ROS_CONNECTION_STRING_KEY k: ROS_CONNECTION_STRING_KEY.values()) //iterate over all the possible keys that gather the connection string for ROS
										if(((LinkedHashMap)item.get("microcontroller")).get(k.toString())!=null) {  
											connectionStr = ((LinkedHashMap)item.get("microcontroller")).get(k.toString()).toString();
											break;
										}

									if(microcontrollerClassName.equals("DefaultRos4EmbeddedMas"))
										microcontroller= createRos4EmbeddedMas(connectionStr,topics,types,beliefNames, ignoreParams);
									else
										if(microcontrollerClassName.equals("DefaultRos4Bdi"))
											microcontroller = createRos4Bdi(connectionStr,topics,types,beliefNames, ignoreParams);


									//handle topic writing actions
									if(item.get("actions")!=null) {
										ArrayList topicWritingActions = null;
										for(TOPIC_ACTION_KEY k: TOPIC_ACTION_KEY.values()) //iterate over all the possible keys that gather topic writing actions
											if(((LinkedHashMap)item.get("actions")).get(k.toString())!=null) {  
												topicWritingActions = (ArrayList) ((LinkedHashMap)item.get("actions")).get(k.toString());
												break;
											}

										//if(((LinkedHashMap)item.get("actions")).get("topicWritingActions")!=null) {
										if(topicWritingActions!=null) {
											//ArrayList topicWritingActions = (ArrayList) ((LinkedHashMap)item.get("actions")).get("topicWritingActions");
											for(int j=0;j<topicWritingActions.size();j++) {
												ServiceParameters params = new ServiceParameters();
												if(((LinkedHashMap)topicWritingActions.get(j)).get("params")!=null)
													params = buildServiceParameters( (ArrayList<Object>) ((LinkedHashMap) topicWritingActions.get(j)).get("params"));

												String topicName = "";	
												for(TOPIC_NAME_KEY k: TOPIC_NAME_KEY.values()) //iterate over all the possible keys that gather perception topic names
													if(((LinkedHashMap)topicWritingActions.get(j)).get(k.toString())!=null) {  
														topicName = ((LinkedHashMap)topicWritingActions.get(j)).get(k.toString()).toString();
													break;
												}
												String topicType = "";										
												for(TOPIC_TYPE_KEY k: TOPIC_TYPE_KEY.values()) //iterate over all the possible keys that gather perception topic types
													if(((LinkedHashMap)topicWritingActions.get(j)).get(k.toString())!=null) {  
														topicType = ((LinkedHashMap)topicWritingActions.get(j)).get(k.toString()).toString();
													break;
												}	

												String actionNameStr = "";
												for(ACTION_NAME_KEY k: ACTION_NAME_KEY.values()) //iterate over all the possible keys that refer to action name
													if(((LinkedHashMap)topicWritingActions.get(j)).get(k.toString())!=null) {  
											            actionNameStr = ((LinkedHashMap)topicWritingActions.get(j)).get(k.toString()).toString();
										                break;
									                }

												embeddedActionList.add(new TopicWritingAction(createAtom(actionNameStr),
														topicName,
														topicType,null,params));
											}
										}

										//handle service request actions
										//if(((LinkedHashMap)((LinkedHashMap)item.get("microcontroller")).get("actions")).get("serviceRequestActions")!=null) {
										ArrayList serviceRequestActions = null;
										for(SERVICE_ACTION_KEY k: SERVICE_ACTION_KEY.values()) //iterate over all the possible keys that gather service request actions
											if(((LinkedHashMap)item.get("actions")).get(k.toString())!=null) {  
												serviceRequestActions = (ArrayList) ((LinkedHashMap)item.get("actions")).get(k.toString());
												break;
											}

										//if(((LinkedHashMap)item.get("actions")).get("serviceRequestActions")!=null) {
										if(serviceRequestActions!=null) {
											//ArrayList serviceRequestActions = (ArrayList) ((LinkedHashMap)item.get("actions")).get("serviceRequestActions");
											for(int j=0;j<serviceRequestActions.size();j++) {
												ServiceParameters params = new ServiceParameters();
												if(((LinkedHashMap)serviceRequestActions.get(j)).get("params")!=null)
													params = buildServiceParameters( (ArrayList<Object>) ((LinkedHashMap) serviceRequestActions.get(j)).get("params"));
												ServiceRequestAction serviceAction = null;
												String actionNameStr = "";
								                for(ACTION_NAME_KEY k: ACTION_NAME_KEY.values()) //iterate over all the possible keys that refer to action name
									               if(((LinkedHashMap)serviceRequestActions.get(j)).get(k.toString())!=null) {  
										              actionNameStr = ((LinkedHashMap)serviceRequestActions.get(j)).get(k.toString()).toString();
										               break;
									               }
												String serviceNameStr = "";
								                for(SERVICE_NAME_KEY k: SERVICE_NAME_KEY.values()) //iterate over all the possible keys that refer to service name
									               if(((LinkedHashMap)serviceRequestActions.get(j)).get(k.toString())!=null) {  
										              serviceNameStr = ((LinkedHashMap)serviceRequestActions.get(j)).get(k.toString()).toString();
										               break;
									               }
												serviceAction = new ServiceRequestAction(createAtom(actionNameStr), 
														serviceNameStr, params);
												embeddedActionList.add(serviceAction);

											}
										}	

									}
									else
										if(microcontrollerClassName.equals("DemoDevice")) {
											//do nothing (so far)
										}		
								}

						DefaultDevice device = null;
						try {
							String deviceClassName = "";
							for(CLASS_NAME_KEY k: CLASS_NAME_KEY.values()) //iterate over all the possible keys that gather the class name
								if(item.get(k.toString())!=null) {
									deviceClassName = item.get(k.toString()).toString();
									break;
								}
							Class c = Class.forName(deviceClassName);
							Object obj = null;
							if(deviceClassName.equals("embedded.mas.bridges.jacamo.DemoDevice")) {
								obj = new DemoDevice(createAtom(item.get("device_id").toString()));
							}
							else {
								Constructor constructor = c.getConstructor(jason.asSyntax.Atom.class,getIExternalDevice(microcontroller.getClass()));
								obj = constructor.newInstance(createAtom(item.get("device_id").toString()),microcontroller);
							}
							for(EmbeddedAction a : embeddedActionList)
								((DefaultDevice) obj).addEmbeddedAction(a);


							List<Actuator> actuators = processActuators((ArrayList) item.get("actuators")); 
							for(Actuator a : actuators)
								((DefaultDevice) obj).addActuator(a);


							devices.add((DefaultDevice) obj);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					/*else
						if(((LinkedHashMap) l.get(i)).containsKey("actions")) {
							LinkedHashMap actions = (LinkedHashMap) l.get(i);
							if((actions.get("actions") instanceof ArrayList)) { //if there are some actions 
								ArrayList actionList = (ArrayList) actions.get("actions"); 
								for(int i1=0;i1<actionList.size();i1++) { //for each action...
									LinkedHashMap actionItem = (LinkedHashMap) actionList.get(i1);
									Iterator it = actionItem.keySet().iterator();
									if(it.hasNext()) {
										String actionName = it.next().toString(); //save the current action name
										ArrayList actuationSequence = (ArrayList) actionItem.get(actionName); //save the actuation sequence, which is a sequence of actuation sets
										ActuationSequence currentActuationSequence = new ActuationSequence(); //start a new actuation sequence
										String regex = "([^.]+)\\.([^.]+)";
										Pattern pattern;
										Matcher matcher;
										for(int k=0;k<actuationSequence.size();k++) { //for each actuation set
											//System.out.println("Actuation Set: " + actuationSequence.get(k));
											ArrayList actuationSet = (ArrayList) actuationSequence.get(k);
											ActuationSet currentActuationSet = new ActuationSet(); //start a new actuation set
											for(int n=0;n<actuationSet.size();n++){// for each element in the actuation set
												pattern = Pattern.compile(regex);
												matcher = pattern.matcher(actuationSet.get(n).toString());
												while (matcher.find()) {
													//find the device
													DefaultDevice currentDevice = null;
													for(DefaultDevice d:devices)
														if(d.getId().toString().equals(matcher.group(1)))
															currentDevice = d;														
													Actuation act = new Actuation(currentDevice, createAtom(matcher.group(2)));
													currentActuationSet.add(act);													
												}												

											}
											currentActuationSequence.addLast(currentActuationSet);
										}
									}
								}
							}
						}*/
					//System.out.println(l.get(0).getClass().getName());
				}
			}
			return devices;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//check whether all the devices are properly set in the actuations (i.e. check for actuations wher the device is null


		return devices;

	}

	public ServiceParameters buildServiceArrayParameters(ArrayList<Object> object) {
		ServiceParameters result = new ServiceParameters();
		int arrayParamCount = 0;
		for(Object o : object) //for each nested array
			if(o instanceof ArrayList) {
				ServiceParam p = createServiceParam("arrray_parameter_" + arrayParamCount++, buildServiceParameters((ArrayList)o));
				result.add(p);
			}

		return result;

	}

	public ServiceParameters buildServiceParameters(ArrayList<Object> object) {
		ServiceParameters result = new ServiceParameters();
		for(Object o:object) { //for each parameter
			if(o instanceof LinkedHashMap) { //if the current parameter has nested parameters
				for (Map.Entry<String, ArrayList> oo : ((LinkedHashMap<String, ArrayList>) o).entrySet()) { //for each nested parameter
					if(oo.getValue() instanceof ArrayList) { //if the nested parameter is an ArrayList (default condition for nested parameters)
						if(((ArrayList)oo.getValue()).size()==0) { //if the paramvalue is an array which accepts any amount of parameters
						    result.add(new ServiceArrayParam(oo.getKey(), null));
						}
						else
						if(((ArrayList)oo.getValue()).get(0) instanceof ArrayList) {  //if the paramvalue is an array whose expected elements are service parameters 
							if (((ArrayList)((ArrayList)oo.getValue()).get(0)).size()>0){ 
								ServiceParameters p = new ServiceParameters();
								for(int i=0;i<((ArrayList)((ArrayList)oo.getValue()).get(0)).size();i++)
									p.add(createServiceParam(((ArrayList)((ArrayList)oo.getValue()).get(0)).get(i).toString(), null));
								result.add(new ServiceArrayMsgParam(oo.getKey(), null,p));
							}							
						}
						else
							result.add( createServiceParam(oo.getKey(), buildServiceParameters((ArrayList<Object>) oo.getValue())));
					}
				}					
			}
			else {
				result.add(createServiceParam(o.toString(), null));
			}

		}
		return result;
	}


}
