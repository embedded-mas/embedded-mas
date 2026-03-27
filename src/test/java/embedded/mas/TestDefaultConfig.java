package embedded.mas;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.yaml.snakeyaml.Yaml;

import embedded.mas.bridges.jacamo.config.DefaultConfig;
import embedded.mas.bridges.ros.ServiceParameters;

public class TestDefaultConfig {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private File writeYaml1() {
		File file = null;
		try {
			file = tempFolder.newFile("test1.yaml");
			try (FileWriter fw = new FileWriter(file)) {
				fw.write("- device_id: sample_roscore\n"
						+ "  className: embedded.mas.bridges.ros.RosMaster\n"
						+ "  microcontroller: \n"
						+ "      id: ros1\n"
						+ "      connectionString: ws://localhost:9090\n"
						+ "      className: DefaultRos4Bdi\n"
						+ "  perceptionTopics:         \n"
						+ "      - topicName: scan\n"
						+ "        topicType: sensor_msgs/LaserScan\n"
						+ "        beliefName: distance_reading\n"
						+ "        ignoreValues: \n"
						+ "          - header \n"
						+ "          - angle_min\n"
						+ "          - angle_max\n"
						+ "          - angle_increment\n"
						+ "          - time_increment\n"
						+ "          - scan_time\n"
						+ "          - range_min\n"
						+ "          - range_max\n"
						+ "          - intensities   \n"
						+ "  actions:\n"
						+ "    topicWritingActions:          \n"
						+ "      - actionName: move_robot\n"
						+ "        topicName: cmd_vel\n"
						+ "        topicType: geometry_msgs/Twist \n"
						+ "        params:\n"
						+ "           - linear:\n"
						+ "              - x\n"
						+ "              - y\n"
						+ "              - z\n"
						+ "           - angular:\n"
						+ "              - x\n"
						+ "              - y\n"
						+ "              - z \n"
						+ "           - p\n"
						+ "\n"
						+ "- perception_rules:\n"
						+ "  - obstacle_front(X) :- distance_reading(ranges(L)) & .length(L,S) & .nth(0,L,X).    \n"
						+ "  - obstacle_right(X) :- distance_reading(ranges(L)) & .length(L,S) & .nth(40,L,X).    \n"
						+ "  - obstacle_left(X) :- distance_reading(ranges(L)) & .length(L,S) & .nth(300,L,X).    \n"
						+ "");
			}
		} catch (IOException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	
	private File writeYaml2() {
		File file = null;
		try {
			file = tempFolder.newFile("test2.yaml");
			try (FileWriter fw = new FileWriter(file)) {
				fw.write("- device_id: sample_roscore\n"
						+ "  className: embedded.mas.bridges.ros.RosMaster\n"
						+ "  microcontroller: \n"
						+ "      id: ros1\n"
						+ "      connectionString: ws://localhost:9090\n"
						+ "      className: DefaultRos4Bdi\n"
						+ "  perceptionTopics:         \n"
						+ "      - topicName: scan\n"
						+ "        topicType: sensor_msgs/LaserScan\n"
						+ "        beliefName: distance_reading\n"
						+ "        ignoreValues: \n"
						+ "          - header \n"
						+ "          - angle_min\n"
						+ "          - angle_max\n"
						+ "          - angle_increment\n"
						+ "          - time_increment\n"
						+ "          - scan_time\n"
						+ "          - range_min\n"
						+ "          - range_max\n"
						+ "          - intensities   \n"
						+ "  actions:\n"
						+ "    topicWritingActions:          \n"
						+ "      - actionName: move_robot\n"
						+ "        topicName: cmd_vel\n"
						+ "        topicType: geometry_msgs/Twist \n"
						+ "        params:\n"
						+ "           - a:\n"
						+ "               - x\n"
						+ "               - y:\n"
						+ "                   - [y11, y12]\n"
						+ "               - w: []\n"
						+ "               - z"
						+ "\n"
						+ "- perception_rules:\n"
						+ "  - obstacle_front(X) :- distance_reading(ranges(L)) & .length(L,S) & .nth(0,L,X).    \n"
						+ "  - obstacle_right(X) :- distance_reading(ranges(L)) & .length(L,S) & .nth(40,L,X).    \n"
						+ "  - obstacle_left(X) :- distance_reading(ranges(L)) & .length(L,S) & .nth(300,L,X).    \n"
						+ "");
			}
		} catch (IOException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	
	@Test
	public void testLoadFromYaml() {
		File file = writeYaml1();
		DefaultConfig config = new DefaultConfig();
		Yaml yaml = new Yaml();
		try (InputStream in = new FileInputStream(file.getAbsolutePath())) {
			ArrayList<Object> paramsList = (ArrayList)((LinkedHashMap)((ArrayList)((LinkedHashMap)((LinkedHashMap)((ArrayList)yaml.load(in)).get(0)).get("actions")).get("topicWritingActions")).get(0)).get("params");
			ServiceParameters parameters = config.buildServiceParameters(paramsList);
			((ServiceParameters)parameters.get(0).getParamValue()).get(0).setParamValue(1.0);
			((ServiceParameters)parameters.get(0).getParamValue()).get(1).setParamValue(2.0);
			((ServiceParameters)parameters.get(0).getParamValue()).get(2).setParamValue(3.0);

			((ServiceParameters)parameters.get(1).getParamValue()).get(0).setParamValue(4.0);
			((ServiceParameters)parameters.get(1).getParamValue()).get(1).setParamValue(5.0);
			((ServiceParameters)parameters.get(1).getParamValue()).get(2).setParamValue(6.0);
			
			parameters.get(2).setParamValue(7.0);

			assertEquals(parameters.toJson().toString(), "{\"linear\":{\"x\":1.0,\"y\":2.0,\"z\":3.0},\"angular\":{\"x\":4.0,\"y\":5.0,\"z\":6.0},\"p\":7.0}");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	
	
	@Test
	public void testLoadFromYamlWithParamArray() {
		File file = writeYaml2();
		DefaultConfig config = new DefaultConfig();
		Yaml yaml = new Yaml();
		try (InputStream in = new FileInputStream(file.getAbsolutePath())) {
			ArrayList<Object> paramsList = (ArrayList)((LinkedHashMap)((ArrayList)((LinkedHashMap)((LinkedHashMap)((ArrayList)yaml.load(in)).get(0)).get("actions")).get("topicWritingActions")).get(0)).get("params");
			ServiceParameters parameters = config.buildServiceParameters(paramsList);			
			assertTrue(parameters.toJson().toString().equals("{\"a\":{\"x\":null,\"y\":[{\"y11\":null,\"y12\":null}],\"w\":[null],\"z\":null}}"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}


}
