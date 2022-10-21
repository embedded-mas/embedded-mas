package embedded.mas.bridges.ros;

import embedded.mas.bridges.jacamo.EmbeddedAction;
import embedded.mas.bridges.jacamo.IEmbeddedAction;
import jason.asSyntax.Atom;
import jason.asSyntax.patterns.goal.EBDG;

public abstract class RosAction extends EmbeddedAction implements  IEmbeddedAction {

	private Atom actionName;
	
	public Atom getActionName() {
		return actionName;
	}

	public void setActionName(Atom actionName) {
		this.actionName = actionName;
	}

}
