package embedded.mas.bridges.jacamo;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import java.io.File;

public class InternalActionGenerator  {

	private static void writeToFile(String deviceId, String actionName, String serviceName, List<String> params) {
		Path filePath = Paths.get("src/java/jason/stdlib/" + actionName + ".java");		
		if(Files.exists(filePath))
			System.out.println("*** internal action " + actionName + " already exists in src/java/jason/stdlib and will not be overwritten ***");
		else {
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

				


			try {
				Files.write(filePath, fileContent.getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	private static void writeToFile_MultiActuation(String actionName) {
		Path filePath = Paths.get("src/java/jason/stdlib/" + actionName + ".java");		
		if(Files.exists(filePath))
			System.out.println("*** internal action " + actionName + " already exists in src/java/jason/stdlib and will not be overwritten ***");
		else {
			String fileContent = "package jason.stdlib; \n\n" +
					"import jason.asSemantics.TransitionSystem;\n"
					+ "import jason.asSemantics.Unifier;\n"
					+ "import jason.asSyntax.ListTermImpl;\n"
					+ "import jason.asSyntax.NumberTermImpl;\n"
					+ "import jason.asSyntax.Term;\n"
					+ "\n"
					+ "import static jason.asSyntax.ASSyntax.createAtom;\n"
					+ "\n"
					+ "import java.util.Iterator;\n"
					+ "\n"
					+ "import embedded.mas.bridges.jacamo.actuation.Actuation;\n"
					+ "import embedded.mas.bridges.jacamo.actuation.ActuationDevice;\n"
					+ "import embedded.mas.bridges.jacamo.actuation.ActuationSequence;\n"
					+ "import embedded.mas.bridges.jacamo.actuation.ActuationSet;\n"
					+ "import embedded.mas.bridges.jacamo.actuation.Actuator;\n\n" +
					

               

                "public class "+ actionName +" extends embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction2 {\n\n" +

                "        @Override\n" +
                "        public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {\n" +

                "            ListTermImpl parameters = new ListTermImpl();\n" +
                "            for(Term t:args) parameters.add(t);\n" +

                "            Term[] arguments = new Term[3];\n" +
                "            arguments[0] =  createAtom(\"" + actionName + "\"); \n" +
                "            arguments[1] = parameters;\n" +
                "            return super.execute(ts, un,  arguments);            \n" +
                "        }\n" +
                "}";

			File directory = new File("src/java/jason/stdlib");
			if (!directory.exists()) directory.mkdirs();




			try {
				Files.write(filePath, fileContent.getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static List<String> getFilesByExtension(String diretorioCaminho, String extensao) {
		File diretorio = new File(diretorioCaminho);
		List<String> listaDeArquivos = new ArrayList<>();

		if (!diretorio.isDirectory()) {
			System.out.println("Invalid directory path.");
			return listaDeArquivos;  // returns an empty list
		}

		File[] arquivos = diretorio.listFiles((dir, nome) -> nome.toLowerCase().endsWith(extensao.toLowerCase()));

		if (arquivos != null) {
			for (File arquivo : arquivos) {
				listaDeArquivos.add(arquivo.getName());
			}
		}

		return listaDeArquivos;
	}


	public static void main(String[] args) {
		List<String> yamlFiles = getFilesByExtension("src/agt/", "yaml");
		//List<String> yamlFiles = getFilesByExtension("/mnt/1C4C766F4C764414/maiquel/git/embedded-mas/examples/jacamo/serial_device/perception_action/src/agt/", "yaml");

		for(String s : yamlFiles) {
			Yaml yaml = new Yaml();

			InputStream inputStream = InternalActionGenerator.class
					.getClassLoader()
					//.getResourceAsStream("robot1.yaml");
					.getResourceAsStream(s);

			if (inputStream == null) {
				throw new IllegalArgumentException("File not found! Check the file path.");
			}
			List<Map<String, Object>> yamlData = yaml.load(inputStream);



			for (Map<String, Object> device : yamlData) {
				//iterate over devices
				String deviceId = (String) device.get("device_id");
				if(deviceId!=null) {
					//ROS-actions
					Map<String, Object> actions = (Map<String, Object>) device.get("actions");
					if(actions!=null) {
						
						//*** old yaml configuration
						List<Map<String, Object>> serviceRequestActions = (List<Map<String, Object>>) actions.get("serviceRequestActions");

						if(serviceRequestActions!=null){
							for (Map<String, Object> action : serviceRequestActions) {
								String actionName = (String) action.get("actionName");
								String serviceName = (String) action.get("serviceName");
								List<String> params = (List<String>) action.getOrDefault("params", List.of());				
								InternalActionGenerator.writeToFile(deviceId, actionName, serviceName, params);
							}
						}


						List<Map<String, Object>> topicWritingActions = (List<Map<String, Object>>) actions.get("topicWritingActions");

						if(topicWritingActions!=null){
							for (Map<String, Object> action : topicWritingActions) {
								String actionName = (String) action.get("actionName");
								String serviceName = (String) action.get("serviceName");
								List<String> params = (List<String>) action.getOrDefault("params", List.of());						
								InternalActionGenerator.writeToFile(deviceId, actionName, serviceName, params);
							}
						}
						
						
						
						
					}
					else {
						//Serial actions
						ArrayList l = (ArrayList) device.get("serialActions");
						if(l!=null)
							for(int i=0;i<l.size();i++) {
								LinkedHashMap map = (LinkedHashMap)l.get(i);
								InternalActionGenerator.writeToFile(deviceId, map.get("actionName").toString(), "", null);
							}
					}
				}
				
			
				//iterate over actions
				ArrayList actions = (ArrayList) device.get("actions");
				if(actions!=null) {
					for(int i=0;i<actions.size();i++) {
						 for (Entry<String, ArrayList> action : ((LinkedHashMap<String, ArrayList>)actions.get(i)).entrySet()) {
							 writeToFile_MultiActuation(action.getKey());
						 }
						
					}
				}

			}



		}
	}
}
