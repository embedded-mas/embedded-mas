package embedded.mas.bridges.jacamo;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import embedded.mas.bridges.jacamo.action.Action;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;
import embedded.mas.bridges.jacamo.actuation.DefaultActuation;



public class defaultEmbeddedInternalAction2 extends EmbeddedInternalAction {



	private Object[] listToArguments(ListTermImpl args) {
		Object[] arguments = new Object[args.size()];			
		for(int i=0;i<args.size();i++) {
			if(args.get(i) instanceof ListTermImpl)
				arguments[i] = listToArguments((ListTermImpl) args.get(i));
			else
				if(args.get(i) instanceof NumberTermImpl)
					arguments[i] = args.get(i);
				else
					arguments[i] = args.get(i).toString().replaceAll("\"(.+)\"", "$1");
		}		
		return arguments;
	}



	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {  
		if(ts.getAg() instanceof EmbeddedAgent) {
			EmbeddedAgent agent = (EmbeddedAgent) ts.getAg();
			DefaultDevice device = null;
			//Case 1: check whether the action is in the agent's repertory (newest approach)

			Action action = agent.getActionMap().get(createAtom(args[0].toString())); 

			if(action!=null) { //if there is some configured action


				//get the actuation sequence that realize the action of the parameter args[0]
				ActuationSequence actuationSequence = action.getSequence(); 

				if(actuationSequence!=null) { //if the action has an associated actuation sequence



					Object[] arguments = ((ListTermImpl)args[1]).toArray();
					Term[] termArguments = new Term[arguments.length];
					for(int i=0;i<termArguments.length;i++)
						termArguments[i] = (Term) arguments[i];



					if(termArguments.length!=action.getParams().size())
						throw new Exception("The given parameter size ("+termArguments.length+") differs from the expected ("+action.getParams().size()+")");



					int i=0;
					for (Atom chave : action.getParams().keySet()) {
						action.getParams().put(chave, termArguments[i++]);
					}
					

					/**
					 * TODO: Make the execution of sets actually parallel.
					 * 
					 * While the execution of all the sets is conceptually parallel, this impementation is based on an iteraction over the sets.
					 */
					for(ActuationSet actuationSet:actuationSequence.getActuations()) { //for each set of actuations of the sequence
						
						
						for(ActuationDevice act : actuationSet) { //for each actuation in the sequence
							Map<Object, Atom> mappings = act.getActuation().getParamMapping();
							act.getActuation().setParamValuesFromMapping(mappings);
						}		
														

						//split the set into subsets grouped by device
						HashMap<IDevice, ArrayList<ActuationDevice>> subsets = actuationSet.toActuationSetsByDevice(); 



						for(Map.Entry<IDevice, ArrayList<ActuationDevice>> map : subsets.entrySet()) { //for each device
							
							for(ActuationDevice currencAct: map.getValue())
								currencAct.getActuation().setParamValuesFromMapping(action.getParams());
							
							System.out.println("[defaultEmbeddedInternalAction2] ... going to execute " + map.getKey()+"/" + map.getValue() + " - " + map.getValue().get(0).getClass().getName());						
							map.getKey().execActuationSet(map.getValue(),  un);
						}




					}
					return true; //returns true if all the actuations have been done
				}
			}


		}
		//TODO: insert the old code here
		return false;
	}

}
