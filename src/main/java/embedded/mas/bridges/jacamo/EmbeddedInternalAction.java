package embedded.mas.bridges.jacamo;

import jason.asSemantics.DefaultInternalAction;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

public abstract class EmbeddedInternalAction extends DefaultInternalAction{
	
	protected Term adaptTerm(Term t) {
		if(t.toString().matches("\"(.+)\"")) 
			return new StringTermImpl(t.toString().replaceAll("\"(.+)\"", "$1")); 
		return t;
		
	}

}
