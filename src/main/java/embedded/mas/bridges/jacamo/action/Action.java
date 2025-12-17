package embedded.mas.bridges.jacamo.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import jason.asSyntax.Atom;

public class Action {

	private Atom actionName;
	private ActuationSequence sequence = new ActuationSequence();
	private Map<Atom, Object> params = new HashMap<>(); //atom - param id; object: param value
	
	public Action(Atom actionName) {
		super();
		this.actionName = actionName;
	}

	public Atom getActionName() {
		return actionName;
	}

	public void setActionName(Atom actionName) {
		this.actionName = actionName;
	}

	public ActuationSequence getSequence() {
		return sequence;
	}

	public void setSequence(ActuationSequence sequence) {
		this.sequence = sequence;
	}

	public Map<Atom, Object> getParams() {
		return params;
	}

	public void setParams(Map<Atom, Object> params) {
		this.params = params;
	}
	
	
	
}
