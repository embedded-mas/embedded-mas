/**
 * Exemplo de tratamento de JSON com java.
 * 
 * 
 * Este exemplo ilustra o tratamento de um JSON que contém um string, um integer, um float e um vetor. 
 * 
 * {"belief_string": "blablabla",
 *   "belief_integer": 99,
 *   "belief_float": 3.14,
 *   "belief_array": ["abc", 123, 1.99]
 *   }
 * 
 * Maiores informações sobre JSON:
 * 		- https://www.oracle.com/technical-resources/articles/java/json.html
 *      - https://www.devmedia.com.br/java-ee-7-introducao-ao-json-padrao-e-a-api-streaming/30426
 * 
 */

package json_processing;

import java.io.ByteArrayInputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class App {    
    
    public void processJson(String json) {
    	JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()));
    	JsonObject jsonObject = reader.readObject(); //transformar a string em um "objeto JSON"
    	
    	
    	for(String key: jsonObject.keySet()) { //iterar sobre todos os elementos do JsonObject - a variável "key" armazena cada chave do objeto json    		
    		Object value = jsonObject.get(key); //obtém o valor associado à chave "key"
    		String belief = key +"(";
    		if(!(value instanceof JsonArray)) //se o valor não for um vetor (ou seja, se for uma belief com apenas um valor)
    		   belief = belief + value;
    		else { //se for um vetor [v1,v2,...,vn], monta uma belief key(v1,v2,...,vn)    			
    				belief = belief + value.toString().replace("[","").replace("]", "");	 
    			
    		}
    		belief = belief + ")";
    		System.out.println(belief);
    	}
    }
    
    public static void main(String[] args) {
    	String json_text = "{\"belief_string\": \"blablabla\",\n" + 
    			           " \"belief_integer\": 99,\n" + 
    		               " \"belief_float\": 3.14,\n" + 
    			           " \"belief_array\": [\"abc\", 123, 1.99]\n" + 
    			           "}";
        new App().processJson(json_text);
    }
}
