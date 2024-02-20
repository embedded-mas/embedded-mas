package embedded.mas.bridges.jacamo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;



/*
 * A demo implementation of a sensor interface. 
 * 
 * The getPercept method simply produces a collection of literals without any relation with a real sensor. 
 * 
 * This agent enables an action called "print" that just prints a message.
 * 
 */

public class DemoDevice extends DefaultDevice  {

	public DemoDevice(Atom id) {
		super(id,null);
		
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
		percepts.add(Literal.parseLiteral("msg(ola)"));
		
		return percepts;
	}

	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un) {
		if(actionName.equals("print"))
			return doEmbeddedPrint(args[0].toString());	
		else
			if(actionName.equals("double")) { 
				double r = Double.parseDouble(args[0].toString()) * 2;
				NumberTerm result = new NumberTermImpl(r);
				return un.unifies(result, (Term) args[1]);			

			}
		return false;
	}
	
	private boolean doEmbeddedPrint(String text) {
		System.out.println("[" + this.id +" - print] " + text);
		return true;
	}

	@Override
	public IExternalInterface getMicrocontroller() {
		return this.microcontroller;
	}

	

	



	@Override
	public boolean execEmbeddedAction(Atom actionName, Object[] args, Unifier un) {
		IEmbeddedAction action = getEmbeddedAction(actionName);
		if(action!=null) {
			if(action instanceof EmbeddedAtomAction) {
				String arguments = "";
				for(int i=0;i<args.length;i++)
					arguments = arguments.concat(args[i].toString());
				System.out.println("[" + ((EmbeddedAtomAction)action).getActuationName() +"]" + arguments);			
				return true;
			}
		}
		else
			System.out.println("null!!");
		return false;
	}

	@Override
	public boolean execActuation(Atom actuationId, Object[] args) {
		return execEmbeddedAction(actuationId, args, null);
	}

	
}
