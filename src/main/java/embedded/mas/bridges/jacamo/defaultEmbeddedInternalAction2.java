package embedded.mas.bridges.jacamo;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import static jason.asSyntax.ASSyntax.createAtom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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
			
			//get the actuation sequence that realize the action of the parameter args[0]
			ActuationSequence actuationSequence = agent.getActionMap().get(createAtom(args[0].toString())); 
				
			if(actuationSequence!=null) { //if the repertory contains the action
				
				
				
				Object[] arguments = ((ListTermImpl)args[1]).toArray();
				Term[] termArguments = new Term[arguments.length];
				for(int i=0;i<termArguments.length;i++)
					termArguments[i] = (Term) arguments[i];
				
				
				actuationSequence.setParameters(termArguments);
				
				/**
				 * TODO: Make the execution of sets actually parallel.
				 * 
				 * While the execution of all the sets is conceptually parallel, this impementation is based on an iteraction over the sets.
				 */
				for(ActuationSet actuationSet:actuationSequence.getActuations()) { //for each set of actuations of the sequence
					
					//split the set into subsets grouped by device
					HashMap<IDevice, ArrayList<ActuationDevice>> subsets = actuationSet.toActuationSetsByDevice(); 
					
					
					
					for(Map.Entry<IDevice, ArrayList<ActuationDevice>> map : subsets.entrySet()) { //for each device
						//System.out.println("... " + map.getKey()+"/" + map.getValue());						
						map.getKey().execActuationSet(map.getValue(),  un);
					}
						
					
					
				}
				return true; //returns true if all the actuations have been done
			}


		}
		//TODO: insert the old code here
		return false;
	}

}
