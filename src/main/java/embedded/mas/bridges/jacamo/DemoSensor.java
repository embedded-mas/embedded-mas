package embedded.mas.bridges.jacamo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import jason.asSyntax.Atom;
import jason.asSyntax.Literal;



/*
 * A demo implementation of a sensor interface. 
 * 
 * The getPercept method simply produces a collection of literals without any relation with a real sensor. 
 * 
 */

public class DemoSensor extends DefaultSensor  {

	public DemoSensor(Atom id) {
		super(id);
		
	}

	@Override
	public Collection<Literal> getPercepts() {
		ArrayList<Literal> percepts = new ArrayList<Literal>();
		
		LocalTime now = LocalTime.now();
		percepts.add(Literal.parseLiteral("current_hour("+ now.getHour() +")"));
		percepts.add(Literal.parseLiteral("current_minute("+ now.getMinute() +")"));
		percepts.add(Literal.parseLiteral("current_second("+ now.getSecond() +")"));
		
		LocalDate today = LocalDate.now();
		percepts.add(Literal.parseLiteral("current_day("+ today.getDayOfMonth() +")"));
		percepts.add(Literal.parseLiteral("current_month("+ today.getMonth().toString().toLowerCase() +")"));
		percepts.add(Literal.parseLiteral("current_year("+ today.getYear() +")"));
		
		return percepts;
	}

}
