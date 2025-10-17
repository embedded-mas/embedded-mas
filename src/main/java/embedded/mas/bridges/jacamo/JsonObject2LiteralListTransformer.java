package embedded.mas.bridges.jacamo;

import java.util.Collection;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;

public class JsonObject2LiteralListTransformer extends SensorValueTransformer<Collection<Map.Entry<String, JsonValue>>> {

	public JsonObject2LiteralListTransformer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Literal> transform(Collection<Map.Entry<String, JsonValue>> value) {
		try {
			return Utils.JsonObject2ListTermImpl(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TokenMgrError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

}
