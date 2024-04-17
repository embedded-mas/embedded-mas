package jason.stdlib;

import java.util.ArrayList;
import java.util.List;

import embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Term;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.ASSyntax;


import static jason.asSyntax.ASSyntax.createAtom;

public class embeddedInternalAction extends defaultEmbeddedInternalAction {
    
        @Override
        public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
            System.out.println("<<<<<<<<<< testing internal action  >>>>>>>>>> " + this.getClass().getSimpleName() + " - " + args[0]);
            Term[] arguments = new Term[3];
            arguments[0] =  createAtom("sample_roscore"); 
            arguments[1] =  createAtom( this.getClass().getSimpleName());
            arguments[2] = args[0];
            super.execute(ts, un,  arguments);
            return true;
        }
    
    
}
