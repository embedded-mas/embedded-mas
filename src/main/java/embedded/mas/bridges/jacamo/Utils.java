package embedded.mas.bridges.jacamo;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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

}
