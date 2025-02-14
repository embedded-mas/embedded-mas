package jason.stdlib; 

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;

import java.util.Iterator;

import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;

public class a2 extends embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction2 {

        @Override
        public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
            ListTermImpl parameters = new ListTermImpl();
            for(Term t:args) parameters.add(t);
            Term[] arguments = new Term[3];
            arguments[0] =  createAtom("a2"); 
            arguments[1] = parameters;
            return super.execute(ts, un,  arguments);            
        }
}