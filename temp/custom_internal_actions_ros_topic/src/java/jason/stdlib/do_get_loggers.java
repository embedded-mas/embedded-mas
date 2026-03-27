package jason.stdlib; 

import embedded.mas.bridges.jacamo.requestResponseEmbeddedInternalAction;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;
import static jason.asSyntax.ASSyntax.createAtom;

public class do_get_loggers extends embedded.mas.bridges.jacamo.requestResponseEmbeddedInternalAction {

        @Override
        public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
            ListTermImpl parameters = new ListTermImpl();
            for(Term t:args) parameters.add(t);
            parameters.remove(parameters.size()-1);//the latest parameter is reserved for recording the return value
            Term[] arguments = new Term[4];
            arguments[0] =  createAtom("sample_roscore"); 
            arguments[1] =  createAtom( this.getClass().getSimpleName());
            arguments[2] = parameters;
            arguments[3] = args[args.length-1]; //the 4th argument is the response variable
            return super.execute(ts, un,  arguments);            
        }
}