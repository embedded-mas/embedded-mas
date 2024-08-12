package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
public class Utils {

	public static  String jsonToPredArguments(JsonNode node) {
		//System.out.println("[Utils] processando json " + node.toString());
		if(node==null) return null;
		String s = "", f;
		if(!node.isObject()&&!(node instanceof ArrayNode)) { //base case
			s =  node.toString(); 
		}else 
			if(node instanceof ArrayNode) {				
				s = s.concat("[");
				int i;
				for(i=0;i<((ArrayNode)node).size()-1;i++) {
					s = s.concat(jsonToPredArguments(((ArrayNode)node).get(i))) + ",";
				}
				s = s.concat(jsonToPredArguments(((ArrayNode)node).get(((ArrayNode)node).size()-1))); //latest item of the list - without comma
				s = s.concat("]");
			}
			else {
				Iterator<String> fields = node.fieldNames();
				while(fields.hasNext()) { //iterate over JSON fields
					f = fields.next(); 
					s = s.concat(f+"(");
					s = s.concat(jsonToPredArguments(node.get(f)));
					s = s.concat(")");
					if(fields.hasNext()) s = s.concat(",");
				}
			}
		return s;
	}


	public static String jsonToPredArguments(JsonNode node, ArrayList<String> paramsToIgnore) {
		if(paramsToIgnore!=null) {
			ObjectNode object = (ObjectNode) node;
			for(String s:paramsToIgnore)
				object.remove(s);		
		}
		return  Utils.jsonToPredArguments(node);
	}

	
	public static String jsonToPredArgumentsWithParamsToInclude(JsonNode node, ArrayList<String> paramsToInclude) {
		ObjectNode object = (ObjectNode) node;
		ArrayList<String> paramsToIgnore = new ArrayList<String>();
		//get a list of params to ignore		
		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while(fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			String key = field.getKey();
			if(!paramsToInclude.contains(key))
				paramsToIgnore.add(key);
		}
		return jsonToPredArguments(node, paramsToIgnore);
	}
}
