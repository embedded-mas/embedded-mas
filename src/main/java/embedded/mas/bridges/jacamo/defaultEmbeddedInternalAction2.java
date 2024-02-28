package embedded.mas.bridges.jacamo;

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

	@Override
	/**
	 * args:
	 * 0. ActionName
	 */
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {  
		if(ts.getAg() instanceof EmbeddedAgent) {
			EmbeddedAgent agent = (EmbeddedAgent) ts.getAg();
			DefaultDevice device = null;
			//Case 1: check whether the action is in the agent's repertory (newest approach)	
			ActuationSequence actuationSequence = agent.getActionMap().get(createAtom(args[0].toString()));
			if(actuationSequence!=null) { //if the repertory contains the action
				Object[] arguments = ((ListTermImpl)args[1]).toArray();								
				for(ActuationSet actuationSet:actuationSequence.getActuations()) { //for each set of actuations of the sequence
					Iterator<ActuationDevice> it = actuationSet.iterator(); 
					while(it.hasNext()) { //for each actuation in the set
						ActuationDevice currenctActuation = it.next();
						//1. find the device the action is applyied upon
						device = currenctActuation.getDevice();
						Actuator actuator = currenctActuation.getActuator();
						Actuation actuation = currenctActuation.getActuation();
						
						Term[] params = new Term[actuation.getParameters().size()];
						for(int i=0;i<actuation.getParameters().size();i++)
							params[i] = (Term) arguments[i];
						
						Term[] newParams = new Term[arguments.length-params.length];
						System.out.println("New params length: " + newParams.length);
						for(int i=0;i<newParams.length;i++) {
							newParams[i] = (Term) arguments[params.length+i]; //((ListTermImpl)args[1]).get(params.length+i);
						}
						arguments = newParams;
						
						
						
						
						device.execActuation(actuator.getId(), actuation.getId(), params, un);

					}
					un.compose(un);
				}
				return true; //returns true if all the actuations have been done
			}


		}
		//TODO: insert the old code here
		return false;
	}

}
