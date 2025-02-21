package embedded.mas;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.FileWriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.Actuator;
import embedded.mas.bridges.jacamo.actuation.ros.ServiceRequestActuation;
import embedded.mas.bridges.jacamo.actuation.ros.TopicWritingActuation;
import embedded.mas.bridges.jacamo.config.DefaultConfig;
import embedded.mas.exception.InvalidActuationException;
import embedded.mas.exception.InvalidActuatorException;
import embedded.mas.exception.InvalidDeviceException;
import jason.asSyntax.Atom;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestDefaultConfig {


	@BeforeClass
	public static void setUpBeforeAll() {
		generateYaml();
		generateRosYaml();
	}


	private static boolean generateYaml() {
		try {
			// Get the directory where the application was started
			String appPath = new File(".").getCanonicalPath();

			// Define the complete path of the file
			String filePath = appPath + "/teste.yaml";
			File file = new File(filePath);

			System.out.println("gerando " + filePath);

			// If the file already exists, return true without overwriting it
			if (file.exists()) {
				return true;
			}

			// Create directories if they don't exist
			file.getParentFile().mkdirs();

			String fileContent = 
					"- device_id: my_device1\n" +
							"  className: embedded.mas.bridges.jacamo.DemoDevice\n" +
							"  microcontroller:\n" +
							"      id: arduino1\n" +
							"      className: DemoDevice\n" +
							"  actuators:\n" +
							"    - actuator_id: act1\n" +
							"      actuations:\n" +
							"        - actuation_id: print\n" +
							"          parameters:\n" +
							"            - text\n" +
							"        - actuation_id: double\n" +
							"          parameters:\n" +
							"            - value\n" +
							"            - result\n" +
							"    - actuator_id: act2\n" +
							"      actuations:\n" +
							"        - actuation_id: print\n" +
							"          parameters:\n" +
							"            - text\n" +
							"        - actuation_id: double\n" +
							"          parameters:\n" +
							"            - value\n" +
							"            - result\n" +
							"\n" +
							"- device_id: my_device2\n" +
							"  className: embedded.mas.bridges.jacamo.DemoDevice\n" +
							"  microcontroller:\n" +
							"      id: arduino1\n" +
							"      className: DemoDevice\n" +
							"  actuators:\n" +
							"    - actuator_id: act21\n" +
							"      actuations:\n" +
							"        - actuation_id: printx\n" +
							"          parameters:\n" +
							"            - textz\n" +
							"            - blaz\n" +
							"        - actuation_id: double\n" +
							"          parameters:\n" +
							"            - value\n" +
							"            - result\n" +
							"\n" +
							"- actions:\n" +
							"  - a0:\n" +
							"    - [my_device1.act1.print, my_device1.act2.print, my_device1.act1.print]\n" +
							"    - [my_device1.act1.print, my_device1.act2.print, my_device1.act1.print]\n" +
							"  - a1:\n" +
							"    - [my_device1.act1.print, my_device1.act2.double]\n" +
							"    - [my_device2.act21.printx, my_device1.act1.double]\n" +
							"  - a2: [[my_device1.act1.print, my_device2.act21.double], [my_device2.act21.printx, my_device1.act1.double]]\n" +
							"  - a3:\n" +
							"    - \n" +
							"      - actuation: my_device1.act1.double \n" +
							"        default_param_values: \n" +
							"          - value: 2\n" +
							"          - result: 5\n" +
							"      - my_device1.act1.print\n" +
							"    - [my_device1.act1.print]\n" +
							"  - a4:\n" +
							"    - [my_device1.act1.print, my_device1.act2.double]\n";



			// Try writing the file
			try (FileWriter writer = new FileWriter(file)) {
				writer.write(fileContent);
				return true; // File successfully created
			} catch (IOException e) {
				e.printStackTrace();
				return false; // Failed to generate the file
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false; // Failed to get the application directory
		}
	}



	private static boolean generateRosYaml() {
		try {
			// Get the directory where the application was started
			String appPath = new File(".").getCanonicalPath();

			// Define the complete path of the file
			String filePath = appPath + "/testeRos.yaml";
			File file = new File(filePath);

			System.out.println("gerando " + filePath);

			// If the file already exists, return true without overwriting it
			if (file.exists()) {
				return true;
			}

			// Create directories if they don't exist
			file.getParentFile().mkdirs();

			String fileContent = 
					"- device_id: my_device1\n"
							+ "  className: embedded.mas.bridges.ros.RosHost\n"
							+ "  microcontroller:\n"
							+ "      id: arduino1\n"
							+ "      className: DefaultRos4Bdi\n"
							+ "      connectionString: ws://localhost:9090\n" 
							+ "  actuators:\n"
							+ "    - actuator_id: actuator11\n"
							+ "      topicWritingActuations:\n"
							+ "        - actuation_id: act111\n"
							+ "          topicName: /value1\n"
							+ "          topicType: std_msgs/Int32\n"
							+ "    - actuator_id: actuator12\n"
							+ "      topicWritingActuations:\n"
							+ "        - actuation_id: act121\n"
							+ "          topicName: /value2\n"
							+ "          topicType: std_msgs/Int32\n"
							+ "      serviceRequestActuations:\n"
							+ "        - actuation_id: act122\n" 
							+ "          serviceName: /turtle1/teleport_relative\n"
							+ "          parameters:\n"
							+ "             - linear\n"
							+ "             - angular \n"
							+ "- device_id: my_device2\n"
							+ "  className: embedded.mas.bridges.ros.RosHost\n"
							+ "  microcontroller:\n"
							+ "      id: arduino1\n"
							+ "      className: DefaultRos4Bdi\n"
							+ "      connectionString: ws://localhost:9090\n" 
							+ "  actuators:\n"
							+ "    - actuator_id: actuator21\n"
							+ "      topicWritingActuations:\n"
							+ "        - actuation_id: act211\n"
							+ "          topicName: /value1\n"
							+ "          topicType: std_msgs/Int32\n"
							+ "        - actuation_id: act212\n"
							+ "          topicName: /current_time\n"
							+ "          topicType: std_msgs/String";  


			// Try writing the file
			try (FileWriter writer = new FileWriter(file)) {
				writer.write(fileContent);
				return true; // File successfully created
			} catch (IOException e) {
				e.printStackTrace();
				return false; // Failed to generate the file
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false; // Failed to get the application directory
		}
	}


	// Method to delete a directory and all its contents
	private static boolean deleteDirectory(File directory) {
		if (directory.isDirectory()) {
			// List all files and subdirectories in the directory
			File[] files = directory.listFiles();
			if (files != null) {
				// Recursively delete all files and subdirectories            	
				for (File file : files) {
					deleteDirectory(file);
				}
			}
		}
		// Try to delete the file or directory
		return directory.delete();
	}

	public static boolean isClassInClasspath(String className) {
		try {
			// Attempt to load the class
			Class.forName(className);
			return true; // Class is found in the classpath
		} catch (ClassNotFoundException e) {
			return false; // Class is not found in the classpath
		}
	}



	//TODO: check why the directory is not deleted
	@AfterClass
	public static void tearDownAfterClass() {
		try {
			String appPath = new File(".").getCanonicalPath();
			String filePath = appPath + "/teste.yaml";
			File file = new File(filePath);
			file.delete();

			filePath = appPath + "/testeRos.yaml";
			file = new File(filePath);
			file.delete();



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void test() {
		DefaultConfig config = new DefaultConfig();
		try {
			List<DefaultDevice> l = config.loadFromYaml(new File(".").getCanonicalPath() + "/teste.yaml");
			assertEquals(l.size(), 2); //check whether 2 devices have been loaded


			//check whether the acuations have been correctly loaded into the actuators
			assertNotNull(l.get(0).getActuatorById(createAtom("act1")).getActuationById(createAtom("print")));			
			assertNotNull(l.get(1).getActuatorById(createAtom("act21")).getActuationById(createAtom("printx")));



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testProcessRosActuators() {
		DefaultConfig config = new DefaultConfig();
		try {
			Yaml yaml = new Yaml();
			Iterable<Object> itr = yaml.loadAll(new FileInputStream(new File(".").getCanonicalPath() + "/testeRos.yaml"));
			for (Object o : itr) { 
				ArrayList devices = (ArrayList) o;
				ArrayList actuators = null;
				List<Actuator> act = null;
				
				//1st device
				actuators = (ArrayList) ((LinkedHashMap<?, ?>) devices.get(0)).get("actuators");
				act =  config.processRosActuators(actuators);
				assertEquals(act.size(), 2);
				assertEquals(act.get(0).getId().toString(), "actuator11");
				assertEquals(act.get(0).getActuations().size(), 1);
							
				assertEquals(act.get(1).getId().toString(), "actuator12");				
				assertNotNull(act.get(1).getActuationById(createAtom("act122")));
				assertEquals(((ServiceRequestActuation)act.get(1).getActuationById(createAtom("act122"))).getServiceName(), "/turtle1/teleport_relative");
				assertEquals(((ServiceRequestActuation)act.get(1).getActuationById(createAtom("act122"))).getParameters().size(), 2);
				
				
				//2nd device
				actuators = (ArrayList) ((LinkedHashMap<?, ?>) devices.get(1)).get("actuators");
				act =  config.processRosActuators(actuators);
				assertEquals(act.size(), 1);
				assertEquals(act.get(0).getId().toString(), "actuator21");
				assertEquals(act.get(0).getActuations().size(), 2);
				
				assertNotNull(act.get(0).getActuationById(createAtom("act211")));
				assertEquals(((TopicWritingActuation)act.get(0).getActuationById(createAtom("act211"))).getTopicName(), "/value1");
				assertEquals(((TopicWritingActuation)act.get(0).getActuationById(createAtom("act211"))).getTopicType(), "std_msgs/Int32");
				assertEquals(((TopicWritingActuation)act.get(0).getActuationById(createAtom("act211"))).getParameters().toString(), "value");


				assertNotNull(act.get(0).getActuationById(createAtom("act212")));
				assertEquals(((TopicWritingActuation)act.get(0).getActuationById(createAtom("act212"))).getTopicName(), "/current_time");
				assertEquals(((TopicWritingActuation)act.get(0).getActuationById(createAtom("act212"))).getTopicType(), "std_msgs/String");
				assertEquals(((TopicWritingActuation)act.get(0).getActuationById(createAtom("act212"))).getParameters().toString(),"value");



			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(true);

	}

	@Test
	public void testGetActions() {
		DefaultConfig config = new DefaultConfig();
		try {
			List<DefaultDevice> l = config.loadFromYaml(new File(".").getCanonicalPath() + "/teste.yaml");
			HashMap<Atom, ActuationSequence> actionMap = config.getActions(l, new File(".").getCanonicalPath() + "/teste.yaml");
			ActuationSequence sequence = actionMap.get(createAtom("a3"));
			assertEquals(sequence.size(), 2);
			assertEquals(sequence.get(0).size(), 2);
			assertEquals(sequence.get(1).size(), 1);
			assertEquals(sequence.get(0).get(0).getDevice().getId().toString(),"my_device1");
			assertEquals(sequence.get(0).get(0).getActuator().getId().toString(),"act1");
			assertEquals(sequence.get(0).get(0).getActuation().getId().toString(),"double");
			assertEquals(sequence.get(0).get(0).getActuation().getDefaultParameterValues().size(), 2);
			assertEquals(sequence.get(0).get(1).getDevice().getId().toString(),"my_device1");
			assertEquals(sequence.get(0).get(1).getActuator().getId().toString(),"act1");
			assertEquals(sequence.get(0).get(1).getActuation().getId().toString(),"print");
			assertNull(sequence.get(0).get(1).getActuation().getDefaultParameterValues());
			assertEquals(sequence.get(1).get(0).getDevice().getId().toString(),"my_device1");
			assertEquals(sequence.get(1).get(0).getActuator().getId().toString(),"act1");
			assertEquals(sequence.get(1).get(0).getActuation().getId().toString(),"print");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDeviceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidActuationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidActuatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
