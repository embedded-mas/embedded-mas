package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
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
				if(((ArrayNode)node).size()==0) return "[]";
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
			int i;
			ObjectNode object = (ObjectNode) node;				
			for(String s:paramsToIgnore) {
				ObjectNode objectTemp = object;
				String elements[] = s.split("\\.");				
				i=0;
				while(i<elements.length) { //traverse the elements (e.g. linear.x)
					/* if the value of the current element is a nested node and is not the latest element, traverse the nested node
					 *  the latest element must be ignored in nested nodes because it is the "value" of the node to be removed.
					 *  for instance, to remove y.y2 from the node {x: 1, y: {y1:2, y2:{a:3,b:4}}}, 
					 *  the objectTemp must be  {y1:2, y2:{a:3,b:4}}, which is the value of the key "y"
					 */
					if((objectTemp.get(elements[i]) instanceof ObjectNode)&& //
							(i<elements.length-1)) 	//if this is not the latest element. This checking is necessary because the 
						objectTemp = (ObjectNode) objectTemp.get(elements[i]);
					i++;
				}
				objectTemp.remove(elements[i-1]);				
			}
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
