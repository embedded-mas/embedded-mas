package jason.stdlib; 

import embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;
import static jason.asSyntax.ASSyntax.createAtom;

public class update_value2 extends embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction {

        @Override
        public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
            ListTermImpl parameters = new ListTermImpl();
            for(Term t:args) parameters.add(t);
            Term[] arguments = new Term[3];
            arguments[0] =  createAtom("sample_roscore"); 
            arguments[1] =  createAtom( this.getClass().getSimpleName());
            arguments[2] = parameters;
            return super.execute(ts, un,  arguments);            
        }
}