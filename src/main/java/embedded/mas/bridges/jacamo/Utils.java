package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;

import static jason.asSyntax.ASSyntax.parseLiteral;

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
	
	
	/**
	 * Convert a json {key:value} to a literal key(value).
	 * Examples
	 * 	-  {"value":1} -> value(1)
	 *  -  {"values":[1,2,3]} -> values(1,2,3)
	 * 
	 * @param key: The json key
	 * @param value: The json value
	 * @return A literal corresponding to the JSON 
	 */
	public static Literal Json2Literal(String key, JsonValue value) throws ParseException, TokenMgrError {
		String belief = null;

		belief = key +"(";
		if(!(value instanceof JsonArray)) //se o valor não for um vetor (ou seja, se for uma belief com apenas um valor)
			belief = belief + value;
		else { //se for um vetor [v1,v2,...,vn], monta uma belief key(v1,v2,...,vn)    			
			belief = belief + value.toString().replace("[","").replace("]", "");	 	
		}
		belief = belief + ")";

		return parseLiteral(belief);

	}

	/**
	 * Convert a JsonObject, which may include several Json elements, to a list o literals.
	 * 
	 * Example: {"name":"Alice","values":[1,2,3]} -> [name("Alice"),values(1,2,3)]
	 * 
	 * @param value: the JsonObject
	 * @return a list of literal
	 */
	public static ListTermImpl JsonObject2ListTermImpl (JsonObject value) throws ParseException, TokenMgrError {
		ListTermImpl result = new ListTermImpl();
		for(String key: value.keySet()) { //iterar sobre todos os elementos do JsonObject - a variável "key" armazena cada chave do objeto json    		
			Object jsonValue = value.get(key); //obtém o valor associado à chave "key"
			result.add(Json2Literal(key, value.get(key)));
		}
		return result;
		
	}
	
	
	public static Collection<Literal> JsonObject2ListTermImpl (Collection<Map.Entry<String, JsonValue>> value) throws ParseException, TokenMgrError {
		ArrayList<Literal> result = new ArrayList<Literal>();
		Iterator<Entry<String, JsonValue>> it =  value.iterator();
		if(it==null) return null;
		while(it.hasNext()){
			Entry<String, JsonValue> current = it.next();
			result.add(Json2Literal(current.getKey(), current.getValue()));
		}
		return result;
		
	}
}
