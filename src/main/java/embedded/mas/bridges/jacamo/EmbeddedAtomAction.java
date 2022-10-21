/**
 * An embedded action is an action enabled by a physical device.
 * The action has a name, represented by an Atom, with a correspondig actuation name also represented by an Atom.  
 */

package embedded.mas.bridges.jacamo;

import jason.asSyntax.Atom;

public class EmbeddedAtomAction extends EmbeddedAction {
	
	private Atom actionName;
	private Atom actuationName;

	
	public EmbeddedAtomAction(Atom actionName, Atom actuationName) {
		super();
		this.actionName = actionName;
		this.actuationName = actuationName;
	}


	public Atom getActionName() {
		return actionName;
	}


	public void setActionName(Atom actionName) {
		this.actionName = actionName;
	}


	public Atom getActuationName() {
		return actuationName;
	}


	public void setActuationName(Atom actuationName) {
		this.actuationName = actuationName;
	}
	
}
