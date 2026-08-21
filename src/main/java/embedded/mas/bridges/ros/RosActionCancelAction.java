package embedded.mas.bridges.ros;

import jason.asSyntax.Atom;

public class RosActionCancelAction extends RosAction {

    private final RosActionClientAction targetAction;

    public RosActionCancelAction(
            Atom actionName,
            RosActionClientAction targetAction) {

        if (actionName == null) {
            throw new IllegalArgumentException("The cancel action name cannot be null.");
        }
        if (targetAction == null) {
            throw new IllegalArgumentException("The target ROS action cannot be null.");
        }

        this.setActionName(actionName);
        this.targetAction = targetAction;
    }

    public RosActionClientAction getTargetAction() {
        return this.targetAction;
    }

    @Override
    public String toString() {
        String description;

        description = "RosActionCancelAction [";
        description = description + "actionName=";
        description = description + this.getActionName();
        description = description + ", targetAction=";
        description = description
                + this.targetAction.getRosActionName();
        description = description + "]";

        return description;
    }
}
