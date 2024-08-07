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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IExternalInterface;
import embedded.mas.bridges.jacamo.SerialEmbeddedAction;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import embedded.mas.bridges.ros.DefaultRos4Bdi;
import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import embedded.mas.bridges.ros.ServiceRequestAction;
import embedded.mas.bridges.ros.TopicWritingAction;

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
	public List<DefaultDevice> loadFromYaml(String filename) {

		/*Yaml yaml = new Yaml();
		Iterable<Object> itr;
		try {
			itr = yaml.loadAll(new FileInputStream(filename));
			for (Object o : itr) {
				System.out.println(o);
				//ArrayList l = (ArrayList) o; //"l" is a list of JSON where each element is a single device configuration
				//for(int i=0;i<l.size();i++) {
				//	
				//}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/	

		ArrayList<DefaultDevice> devices = new ArrayList<DefaultDevice>();

		IExternalInterface microcontroller = null;
		ArrayList<EmbeddedAction> embeddedActionList = new ArrayList<EmbeddedAction>();

		Yaml yaml = new Yaml();

		try {
			Iterable<Object> itr = yaml.loadAll(new FileInputStream(filename));	
			for (Object o : itr) { 
				ArrayList l = (ArrayList) o; //"l" is a list of JSON where each element is a single device configuration

				for(int i=0;i<l.size();i++) {
					LinkedHashMap item = (LinkedHashMap) l.get(i);
					//System.out.println("--> " + l.size() + " - " +  l.get(i));
					if(((LinkedHashMap)item.get("microcontroller")).get("className").equals("Arduino4EmbeddedMas")|
							((LinkedHashMap)item.get("microcontroller")).get("className").equals("SerialReader")) {
						microcontroller= createArduino4EmbeddedMas(((LinkedHashMap)item.get("microcontroller")).get("serial").toString(),
								Integer.parseInt(((LinkedHashMap)item.get("microcontroller")).get("baudRate").toString()));
						//actions
						//System.out.println("Actions:::: "+ item.get("serialActions") + "- " + item.get("serialActions").getClass().getName());
						ArrayList actionsArray = (ArrayList) item.get("serialActions");
						for(int j=0;j<actionsArray.size();j++) {
							/*System.out.println(actionsArray.get(j) + " - " + actionsArray.get(j).getClass().getName() + 
									           ((LinkedHashMap)actionsArray.get(j)).get("actionName").toString() + "->" + 
									           ((LinkedHashMap)actionsArray.get(j)).get("actuationName").toString());
							 */
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
					//System.out.println("*** " + microcontroller.getClass().getInterfaces());
					/*** pegar todas as interfaces do microcontroller  e encontrar uma que estenda IExternalInterface */ 
					//System.out.println("!!! " + microcontroller.getClass().getName() + " !!!");
					//getInterface( microcontroller.getClass());
					/***/


					DefaultDevice device = null;
					try {
						Class c = Class.forName((String) item.get("className"));
						//Constructor constructor = c.getClass().getConstructor(jason.asSyntax.Atom.class,microcontroller.getClass());
						Constructor constructor = c.getConstructor(jason.asSyntax.Atom.class,getIExternalDevice(microcontroller.getClass()));
						//Constructor constructor = c.getConstructor(jason.asSyntax.Atom.class,IPhysicalInterface.class);
						//devices.add( constructor.newInstance(createAtom(item.get("id").toString()),microcontroller));
						Object obj = constructor.newInstance(createAtom(item.get("device_id").toString()),microcontroller);
						//System.out.println("Classe criada " + obj.getClass().getName());
						for(EmbeddedAction a : embeddedActionList)
							((DefaultDevice) obj).addEmbeddedAction(a);
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
				//System.out.println(l.get(0).getClass().getName());
			}
			return devices;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
