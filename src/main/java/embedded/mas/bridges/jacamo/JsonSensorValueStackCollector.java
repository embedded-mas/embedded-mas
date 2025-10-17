package embedded.mas.bridges.jacamo;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import jason.asSyntax.Literal;

public class JsonSensorValueStackCollector extends JSONSensorValueCollector {


	private ConcurrentLinkedDeque<JsonObject> internalPerceptList;

	public JsonSensorValueStackCollector( IExternalInterface microcontroller) {
		super(microcontroller,new ConcurrentLinkedDeque<JsonObject>());
		this.internalPerceptList = (ConcurrentLinkedDeque<JsonObject>) this.getPercptList();
	}


	protected ConcurrentLinkedDeque<JsonObject> getInternalPerceptList() {
		return this.internalPerceptList;
	}

	@Override
	public Collection<Map.Entry<String, JsonValue>> collect() {

		//if(this.getInternalPerceptList().size()==0) return null;

		if(this.getInternalPerceptList().size()==0) return null; 
//			return new AbstractCollection<Map.Entry<String, JsonValue>>() {
//
//				@Override
//				public Iterator<Map.Entry<String, JsonValue>> iterator() {
//					return null;
//				}
//
//				@Override
//				public int size() {
//					return 0;
//				}
//			};
		

		/**
		 * TODO: find a more efficient way to clone.
		 * This clonning method os O(n) (where n is the number of json elements in the JSON object
		 */
		JsonObject clone =  Json.createObjectBuilder(getInternalPerceptList().getLast()).build();


		AbstractCollection result = 
				new AbstractCollection() {
			@Override
			public Iterator<Object> iterator() {
				Iterator<Map.Entry<String, JsonValue>> it = clone.entrySet().iterator();
				return new Iterator() {
					@Override
					public boolean hasNext() {
						return it.hasNext();
					}

					@Override
					public Object next() {
						return it.next(); // cast implícito para Object
					}
				};
			}

			@Override
			public int size() {
				return clone.size();
			}
		};

		this.getInternalPerceptList().removeLast();
		//System.out.println("result size: " + result.size());
		//		System.out.println("result size: " + result);
		return result;


	}

}
