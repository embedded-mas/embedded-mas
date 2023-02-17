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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IDevice;
import embedded.mas.bridges.jacamo.IExternalInterface;
import embedded.mas.bridges.jacamo.IPhysicalInterface;
import embedded.mas.bridges.jacamo.SerialEmbeddedAction;
import embedded.mas.bridges.javard.Arduino4EmbeddedMas;
import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import embedded.mas.bridges.ros.ServiceRequestAction;
import embedded.mas.bridges.ros.TopicWritingAction;
import jason.asSyntax.Atom;

import static jason.asSyntax.ASSyntax.createAtom;

public class DefaultConfig {
	private List<DefaultDevice> devices = new ArrayList<DefaultDevice>();


	private Arduino4EmbeddedMas createArduino4EmbeddedMas(String serialPort, int baudRate) {
		Arduino4EmbeddedMas a = new Arduino4EmbeddedMas(serialPort, baudRate);
		return a;
	}

	private DefaultRos4EmbeddedMas createRos4EmbeddedMas(String connectionStr, ArrayList<String> topics, ArrayList<String> types, ArrayList<String> beliefNames) {
		return new DefaultRos4EmbeddedMas(connectionStr, topics, types, beliefNames);

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
						if(((LinkedHashMap)item.get("microcontroller")).get("className").equals("DefaultRos4EmbeddedMas")) {
							//ArrayList perceptionTopics = (ArrayList) ((LinkedHashMap)item.get("microcontroller")).get("perceptionTopics");
							ArrayList perceptionTopics = (ArrayList) item.get("perceptionTopics");
							ArrayList<String> topics = new ArrayList<String>();
							ArrayList<String> types = new ArrayList<String>();
							ArrayList<String> beliefNames = new ArrayList<String>();
							for(int j=0;j<perceptionTopics.size();j++) {
								topics.add(((LinkedHashMap)perceptionTopics.get(j)).get("topicName").toString());
								types.add(((LinkedHashMap)perceptionTopics.get(j)).get("topicType").toString());
								if(((LinkedHashMap)perceptionTopics.get(j)).get("beliefName")==null)
									beliefNames.add(((LinkedHashMap)perceptionTopics.get(j)).get("topicName").toString());
								else
									beliefNames.add(((LinkedHashMap)perceptionTopics.get(j)).get("beliefName").toString());	
							}
							microcontroller= createRos4EmbeddedMas(((LinkedHashMap)item.get("microcontroller")).get("connectionString").toString(),topics,types,beliefNames);


							//handle topic writing actions
							//if(((LinkedHashMap)((LinkedHashMap)item.get("microcontroller")).get("actions")).get("topicWritingActions")!=null) {
							if(((LinkedHashMap)item.get("actions")).get("topicWritingActions")!=null) {
								ArrayList topicWritingActions = (ArrayList) ((LinkedHashMap)item.get("actions")).get("topicWritingActions");
								for(int j=0;j<topicWritingActions.size();j++) {
									embeddedActionList.add(new TopicWritingAction(createAtom(((LinkedHashMap) topicWritingActions.get(j)).get("actionName").toString()),
											((LinkedHashMap) topicWritingActions.get(j)).get("topicName").toString(),
											((LinkedHashMap) topicWritingActions.get(j)).get("topicType").toString(),null));
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

}
