package embedded.mas.bridges.jacamo;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import java.io.File;

public class InternalActionGenerator  {

	private static void writeToFile(String deviceId, String actionName, String serviceName, List<String> params) {

		String fileContent = "package jason.stdlib; \n\n" +
				"import embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction;\n" +
				"import jason.asSemantics.DefaultInternalAction;\n" +
				"import jason.asSemantics.TransitionSystem;\n" +
				"import jason.asSemantics.Unifier;\n" +
				"import jason.asSyntax.ListTermImpl;\n" +
				"import jason.asSyntax.Term;\n" +

                "import static jason.asSyntax.ASSyntax.createAtom;\n\n" +

                "public class "+ actionName +" extends embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction {\n\n" +

                "        @Override\n" +
                "        public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {\n" +

                "            ListTermImpl parameters = new ListTermImpl();\n" +
                "            for(Term t:args) parameters.add(t);\n" +

                "            Term[] arguments = new Term[3];\n" +
                "            arguments[0] =  createAtom(\"" + deviceId + "\"); \n" +
                "            arguments[1] =  createAtom( this.getClass().getSimpleName());\n" +
                "            arguments[2] = parameters;\n" +
                "            return super.execute(ts, un,  arguments);            \n" +
                "        }\n" +
                "}";

		File directory = new File("src/java/jason/stdlib");
		if (!directory.exists()) directory.mkdirs();

		Path filePath = Paths.get("src/java/jason/stdlib/" + actionName + ".java");

		//List<String> content = Arrays.asList("Hello, this is a text file \n written in Java using NIO.");

		try {
			Files.write(filePath, fileContent.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Yaml yaml = new Yaml();

		InputStream inputStream = InternalActionGenerator.class
				.getClassLoader()
				.getResourceAsStream("robot1.yaml");

		if (inputStream == null) {
			throw new IllegalArgumentException("File not found! Check the file path.");
		}
		List<Map<String, Object>> yamlData = yaml.load(inputStream);

		for (Map<String, Object> device : yamlData) {
			String deviceId = (String) device.get("device_id");
			Map<String, Object> actions = (Map<String, Object>) device.get("actions");
			if(actions!=null) {
				List<Map<String, Object>> serviceRequestActions = (List<Map<String, Object>>) actions.get("serviceRequestActions");

				if(serviceRequestActions!=null){
					for (Map<String, Object> action : serviceRequestActions) {
						String actionName = (String) action.get("actionName");
						String serviceName = (String) action.get("serviceName");
						List<String> params = (List<String>) action.getOrDefault("params", List.of());

						System.out.println(String.format("(%s, %s, %s, %s)", deviceId, actionName, serviceName, params));

						InternalActionGenerator.writeToFile(deviceId, actionName, serviceName, params);
					}
				}


				List<Map<String, Object>> topicWritingActions = (List<Map<String, Object>>) actions.get("topicWritingActions");

				if(topicWritingActions!=null){
					for (Map<String, Object> action : topicWritingActions) {
						String actionName = (String) action.get("actionName");
						String serviceName = (String) action.get("serviceName");
						List<String> params = (List<String>) action.getOrDefault("params", List.of());

						System.out.println(String.format("(%s, %s, %s, %s)", deviceId, actionName, serviceName, params));

						InternalActionGenerator.writeToFile(deviceId, actionName, serviceName, params);
					}
				}
			}
		}
	}
}
