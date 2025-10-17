package embedded.mas.bridges.jacamo;

import java.util.Collection;

import jason.asSyntax.Literal;

public abstract class SensorValueConverter {
	
	public abstract Literal convert(Object value);

}
