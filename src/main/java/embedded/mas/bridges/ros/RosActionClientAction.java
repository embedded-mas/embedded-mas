package embedded.mas.bridges.ros;

import jason.asSyntax.Atom;

public class RosActionClientAction extends RosAction {

    private final String rosActionName;
    private final String actionType;
    private final ServiceParameters goalParameters;
    private final String feedbackBelief;
    private final String resultBelief;
    private final String statusBelief;
    private final boolean feedbackEnabled;

    public RosActionClientAction(
            Atom actionName,
            String rosActionName,
            String actionType,
            ServiceParameters goalParameters,
            String feedbackBelief,
            String resultBelief,
            String statusBelief,
            boolean feedbackEnabled) {

        if (actionName == null) {
            throw new IllegalArgumentException("The Jason action name cannot be null.");
        }
        if (rosActionName == null) {
            throw new IllegalArgumentException("The ROS action name cannot be null.");
        }
        if (actionType == null) {
            throw new IllegalArgumentException("The ROS action type cannot be null.");
        }

        this.setActionName(actionName);
        this.rosActionName = rosActionName;
        this.actionType = actionType;
        this.feedbackBelief = feedbackBelief;
        this.resultBelief = resultBelief;
        this.statusBelief = statusBelief;
        this.feedbackEnabled = feedbackEnabled;

        if (goalParameters == null) {
            this.goalParameters = new ServiceParameters();
        } else {
            this.goalParameters = goalParameters.clone();
        }
    }

    public String getRosActionName() {
        return this.rosActionName;
    }

    public String getActionType() {
        return this.actionType;
    }

    public String getFeedbackBelief() {
        return this.feedbackBelief;
    }

    public String getResultBelief() {
        return this.resultBelief;
    }

    public String getStatusBelief() {
        return this.statusBelief;
    }

    public boolean isFeedbackEnabled() {
        return this.feedbackEnabled;
    }

    public ServiceParameters buildGoalParameters(Object[] values) {
        ServiceParameters parameters = this.goalParameters.clone();
        Object[] safeValues;

        if (values == null) {
            safeValues = new Object[0];
        } else {
            safeValues = values;
        }

        if (!parameters.setValues(safeValues)) {
            throw new IllegalArgumentException(
                    "Action " + this.rosActionName + " expects " + parameters.size()
                            + " parameters, but received " + safeValues.length + ".");
        }

        return parameters;
    }

    @Override
    public String toString() {
        return "RosActionClientAction [actionName=" + this.getActionName()
                + ", rosActionName=" + this.rosActionName
                + ", actionType=" + this.actionType
                + ", feedbackEnabled=" + this.feedbackEnabled + "]";
    }
}
