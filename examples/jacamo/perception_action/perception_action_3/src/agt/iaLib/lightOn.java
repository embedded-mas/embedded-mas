package iaLib;

import embedded.mas.bridges.jacamo.EmbeddedAgent;
import src.main.java.*;

import jason.*;
import jason.asSyntax.*;
import jason.asSemantics.*;

public class lightOn extends DefaultInternalAction {
	@Override
	public Object execute( TransitionSystem ts,Unifier un,Term[] args ) throws Exception {
	
	try {
		return ((DemoEmbeddedAgent)ts.getAg()).lightOn();
	}
	catch (ArrayIndexOutOfBoundsException e) {
		throw new JasonException("The internal action" +
		"has not received arguments enought!");
	} 
	catch (ClassCastException e) {
		throw new JasonException("The internal action "+
		"has received arguments that are not numbers!");
	} 
	catch (Exception e) {
		throw new JasonException("Error in internal action");
	}
	}
}