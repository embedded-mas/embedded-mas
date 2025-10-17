/**
 * Classes extending SensorValueTransformer transform values of the type Tinput in a list of Literal 
 */

package embedded.mas.bridges.jacamo;

import java.util.Collection;

import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;

public abstract class SensorValueTransformer<Tinput> {

	public SensorValueTransformer() {
		// TODO Auto-generated constructor stub
	}

	public abstract Collection<Literal> transform(Tinput value);
	
}
