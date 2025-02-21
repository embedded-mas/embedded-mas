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
import embedded.mas.bridges.jacamo.actuation.DefaultActuation;
import embedded.mas.bridges.jacamo.actuation.ros.ServiceRequestActuation;
import embedded.mas.bridges.jacamo.actuation.ros.TopicWritingActuation;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import embedded.mas.bridges.ros.DefaultRos4Bdi;
import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import embedded.mas.bridges.ros.ServiceRequestAction;
import embedded.mas.bridges.ros.TopicWritingAction;
import embedded.mas.exception.InvalidActuationException;
import embedded.mas.exception.InvalidActuatorException;
import embedded.mas.exception.InvalidDeviceException;
import jason.asSyntax.Atom;


import static jason.asSyntax.ASSyntax.createAtom;

public class DefaultConfig {
	private List<DefaultDevice> devices = new ArrayList<DefaultDevice>();


	private Arduino4EmbeddedMas createArduino4EmbeddedMas(String serialPort, int baudRate) {
		Arduino4EmbeddedMas a = new Arduino4EmbeddedMas(serialPort, baudRate);
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
									Matcher matcher = null;
									for(int k=0;k<actuationSequence.size();k++) { //for each actuation set
										ArrayList actuationSet = (ArrayList) actuationSequence.get(k);
										ActuationSet currentActuationSet = new ActuationSet(); //start a new actuation set
										for(int n=0;n<actuationSet.size();n++){// for each element in the actuation set
											pattern = Pattern.compile(regex);
											HashMap<String, Object> def_params = null;
											if(actuationSet.get(n) instanceof LinkedHashMap) {
												matcher = pattern.matcher(((LinkedHashMap)actuationSet.get(n)).get("actuation").toString());
												if(((LinkedHashMap)actuationSet.get(n)).get("default_param_values")!=null)
												    def_params = new HashMap<>();
												    ArrayList<LinkedHashMap<String, Object>> default_parameters = ((ArrayList)(((LinkedHashMap)actuationSet.get(n)).get("default_param_values") ) );
												    for(LinkedHashMap<String, Object> p: default_parameters)
												    	for(Map.Entry<String, Object> e : p.entrySet()) 
												    		def_params.put(e.getKey(), e.getValue());
												    	
												    	
											}
											else
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
															Iterator<DefaultActuation> actuationIt = currentActuator.getActuations().iterator(); 
															boolean actuationFound = false;
															while(actuationIt.hasNext()) {
																DefaultActuation currentActuation = actuationIt.next().clone();
																if(currentActuation.getId().toString().equals(matcher.group(3))){
																	actuationFound = true;
																	ActuationDevice act = new ActuationDevice(currentDevice, currentActuator,currentActuation);
																	act.getActuation().setDefaultParameterValues(def_params);
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
							actuation.getParameters().add(createAtom(parametersList.get(k).toString()));
					}
					actuator.addActuation(actuation);					   
				}
				result.add(actuator);

			}
		}
		return result;
	}


	public List<Actuator> processRosActuators(List actuatorYaml){		
		ArrayList<Actuator> result = new ArrayList<Actuator>();		
		if(actuatorYaml!=null) {
			for(int i=0;i<actuatorYaml.size();i++){ //for each actuator
				LinkedHashMap currentActuator = (LinkedHashMap)actuatorYaml.get(i);
				ArrayList<LinkedHashMap> topicActuationsList = (ArrayList) currentActuator.get("topicWritingActuations");
				ArrayList<LinkedHashMap> serviceActuationsList = (ArrayList) currentActuator.get("serviceRequestActuations");

				Actuator actuator = new Actuator(createAtom(currentActuator.get("actuator_id").toString()));
				
				

				//for each topic actuation
				if(topicActuationsList!=null)
					for(LinkedHashMap topicActuation: topicActuationsList) {
						Atom parameter;
						parameter = createAtom("value"); //topic-writing actuations have always a single parameter (i.e. the value write)
						TopicWritingActuation actuation = new TopicWritingActuation(createAtom(topicActuation.get("actuation_id").toString()),
								topicActuation.get("topicName").toString(),
								topicActuation.get("topicType").toString(),
								parameter
								);
						actuator.addActuation(actuation);									
					}


				//for each service actuation
				if(serviceActuationsList!=null)
					for(LinkedHashMap serviceActuation: serviceActuationsList) {						
						ServiceParameters p = new ServiceParameters();
						if(serviceActuation.get("parameters")!=null)
							for(int j=0;j<((ArrayList)serviceActuation.get("parameters")).size();j++) 
								p.add(new ServiceParam(((ArrayList)serviceActuation.get("parameters")).get(j).toString(), null));



						ServiceRequestActuation actuation = new ServiceRequestActuation(createAtom(serviceActuation.get("actuation_id").toString()), 
								serviceActuation.get("serviceName").toString(), 
								p);
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
						if(((LinkedHashMap)item.get("microcontroller")).get("className").equals("Arduino4EmbeddedMas")|
								((LinkedHashMap)item.get("microcontroller")).get("className").equals("SerialReader")) {
							microcontroller= createArduino4EmbeddedMas(((LinkedHashMap)item.get("microcontroller")).get("serial").toString(),
									Integer.parseInt(((LinkedHashMap)item.get("microcontroller")).get("baudRate").toString()));
							ArrayList actionsArray = (ArrayList) item.get("serialActions");
							for(int j=0;j<actionsArray.size();j++) {
								SerialEmbeddedAction action  = new SerialEmbeddedAction(createAtom(((LinkedHashMap)actionsArray.get(j)).get("actionName").toString() ), 
										createAtom(((LinkedHashMap)actionsArray.get(j)).get("actuationName").toString()));
								embeddedActionList.add(action);


							}
						}		
						else
							if(((LinkedHashMap)item.get("microcontroller")).get("className").equals("DefaultRos4EmbeddedMas")|
									((LinkedHashMap)item.get("microcontroller")).get("className").equals("DefaultRos4Bdi")) { //DefaultRos4Bdi is just an alias class for the names to make more sense in Jason-ROS applications
								//ArrayList perceptionTopics = (ArrayList) ((LinkedHashMap)item.get("microcontroller")).get("perceptionTopics");
								ArrayList perceptionTopics = (ArrayList) item.get("perceptionTopics");
								ArrayList<String> topics = new ArrayList<String>();
								ArrayList<String> types = new ArrayList<String>();
								ArrayList<String> beliefNames = new ArrayList<String>();
								HashMap<String, ArrayList<String>> ignoreParams = new HashMap<>();
								if(perceptionTopics!=null)
									for(int j=0;j<perceptionTopics.size();j++) {
										topics.add(((LinkedHashMap)perceptionTopics.get(j)).get("topicName").toString());
										types.add(((LinkedHashMap)perceptionTopics.get(j)).get("topicType").toString());
										if(((LinkedHashMap)perceptionTopics.get(j)).get("beliefName")==null)
											beliefNames.add(((LinkedHashMap)perceptionTopics.get(j)).get("topicName").toString());
										else
											beliefNames.add(((LinkedHashMap)perceptionTopics.get(j)).get("beliefName").toString());	
										ArrayList tempParams =  (ArrayList) ((LinkedHashMap)perceptionTopics.get(j)).get("ignoreValues");
										ignoreParams.put(((LinkedHashMap)perceptionTopics.get(j)).get("topicName").toString(), tempParams);
									}

								if(((LinkedHashMap)item.get("microcontroller")).get("className").equals("DefaultRos4EmbeddedMas"))
									microcontroller= createRos4EmbeddedMas(((LinkedHashMap)item.get("microcontroller")).get("connectionString").toString(),topics,types,beliefNames, ignoreParams);
								else
									if(((LinkedHashMap)item.get("microcontroller")).get("className").equals("DefaultRos4Bdi"))
										microcontroller = createRos4Bdi(((LinkedHashMap)item.get("microcontroller")).get("connectionString").toString(),topics,types,beliefNames, ignoreParams);


								//handle topic writing actions
								if(item.get("actions")!=null) {
									if(((LinkedHashMap)item.get("actions")).get("topicWritingActions")!=null) {
										ArrayList topicWritingActions = (ArrayList) ((LinkedHashMap)item.get("actions")).get("topicWritingActions");
										for(int j=0;j<topicWritingActions.size();j++) {
											ServiceParameters params = new ServiceParameters();
											if(((LinkedHashMap)topicWritingActions.get(j)).get("params")!=null)
												params = buildServiceParameters( (ArrayList<Object>) ((LinkedHashMap) topicWritingActions.get(j)).get("params"));

											embeddedActionList.add(new TopicWritingAction(createAtom(((LinkedHashMap) topicWritingActions.get(j)).get("actionName").toString()),
													((LinkedHashMap) topicWritingActions.get(j)).get("topicName").toString(),
													((LinkedHashMap) topicWritingActions.get(j)).get("topicType").toString(),null,params));
										}
									}

									//handle service request actions
									//if(((LinkedHashMap)((LinkedHashMap)item.get("microcontroller")).get("actions")).get("serviceRequestActions")!=null) {
									if(((LinkedHashMap)item.get("actions")).get("serviceRequestActions")!=null) {
										ArrayList serviceRequestActions = (ArrayList) ((LinkedHashMap)item.get("actions")).get("serviceRequestActions");
										for(int j=0;j<serviceRequestActions.size();j++) {
											ServiceParameters params = new ServiceParameters();
											if(((LinkedHashMap)serviceRequestActions.get(j)).get("params")!=null)
												for(int k=0;k< ((ArrayList)((LinkedHashMap)serviceRequestActions.get(j)).get("params")).size();k++) {
													ServiceParam p = new ServiceParam(((ArrayList)((LinkedHashMap)serviceRequestActions.get(j)).get("params")).get(k).toString(), 
															null);
													params.add(p);
												}
											ServiceRequestAction serviceAction = null;
											serviceAction = new ServiceRequestAction(createAtom(((LinkedHashMap)serviceRequestActions.get(j)).get("actionName").toString()), 
													((LinkedHashMap)serviceRequestActions.get(j)).get("serviceName").toString(), params);
											embeddedActionList.add(serviceAction);

										}
									}	

								}
							}
							else
								if(((LinkedHashMap)item.get("microcontroller")).get("className").equals("DemoDevice")) {
									//do nothing (so far)
								}		


						DefaultDevice device = null;
						try {
							Class c = Class.forName((String) item.get("className"));
							Object obj = null;
							if(item.get("className").equals("embedded.mas.bridges.jacamo.DemoDevice")) {
								obj = new DemoDevice(createAtom(item.get("device_id").toString()));
							}
							else {
								Constructor constructor = c.getConstructor(jason.asSyntax.Atom.class,getIExternalDevice(microcontroller.getClass()));
								obj = constructor.newInstance(createAtom(item.get("device_id").toString()),microcontroller);
							}
							for(EmbeddedAction a : embeddedActionList)
								((DefaultDevice) obj).addEmbeddedAction(a);


							List<Actuator> actuators;
							if(item.get("className").equals("embedded.mas.bridges.ros.RosHost")|
									item.get("className").equals("embedded.mas.bridges.ros.RosMaster"))
								actuators = processRosActuators((ArrayList) item.get("actuators"));
							else
								actuators = processActuators((ArrayList) item.get("actuators")); 
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

	private ServiceParameters buildServiceParameters(ArrayList<Object> object) {
		ServiceParameters result = new ServiceParameters();
		for(Object o:object) {
			if(o instanceof LinkedHashMap) {
				ServiceParam p;
				for (Map.Entry<String, ArrayList> oo : ((LinkedHashMap<String, ArrayList>) o).entrySet()) {
					if(oo.getValue() instanceof ArrayList) {
						result.add( new ServiceParam(oo.getKey(), buildServiceParameters((ArrayList<Object>) oo.getValue())));
					}
				}
			}
			else
				result.add(new ServiceParam(o.toString(), null));
		}
		return result;
	}


}
