package jason.stdlib;



import java.util.ArrayList;
import java.util.List;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Term;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.ASSyntax;



import static jason.asSyntax.ASSyntax.createAtom;

import embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction;

/**
<p>Internal action: <b><code>set_reward(<i>B</i>, <i>A</i>, <i>R</i>, <i>F</i>)</code></b>.
<p>Description: sets the reward of an arm's latest "pull" and returns the bandit's current arm frequencies (after the update)
<p>Parameter:<ul>
<li>- bandit (number): index (ID) of the bandit that is to be used</li>
<li>- arm (string): name of the arm whose reward list should be appended</li>
<li>- reward (number): reward that should be append to the arm's reward list</li>
<li>- unifier (list of numbers): unifies with the bandit's current arm frequencies</li>

</ul>
<p>Example:<ul>
<li><code>set_reward(0, "a", 5, F)</code>: Sets the latest reward of the arm "a" of the bandit with index 0 to 5 and unifies F with the bandit's arm frequencies.</li>
</ul>
 */


public class testia extends defaultEmbeddedInternalAction{

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        System.out.println("<<<<<<<<<< testing ia >>>>>>>>>> " + args[0]);
        Term[] arguments = new Term[3];
        arguments[0] =  createAtom("sample_roscore"); 
        arguments[1] =  createAtom( "move_turtle" );
        arguments[2] = args[0];
        super.execute(ts, un,  arguments);
        return true;
    }
}