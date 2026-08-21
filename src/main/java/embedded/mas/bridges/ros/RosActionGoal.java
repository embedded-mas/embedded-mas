package embedded.mas.bridges.ros;

import com.fasterxml.jackson.databind.JsonNode;

public class RosActionGoal {

    public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_EXECUTING = 2;
    public static final int STATUS_CANCELING = 3;
    public static final int STATUS_SUCCEEDED = 4;
    public static final int STATUS_CANCELED = 5;
    public static final int STATUS_ABORTED = 6;

    private final String goalId;
    private final RosActionClientAction action;

    private volatile int status;
    private volatile JsonNode lastFeedback;
    private volatile JsonNode result;

    public RosActionGoal(
            String goalId,
            RosActionClientAction action) {

        if (goalId == null) {
            throw new IllegalArgumentException("The ROS action goal ID cannot be null.");
        }
        if (action == null) {
            throw new IllegalArgumentException("The ROS action configuration cannot be null.");
        }

        this.goalId = goalId;
        this.action = action;
        this.status = STATUS_UNKNOWN;
        this.lastFeedback = null;
        this.result = null;
    }

    public String getGoalId() {
        return this.goalId;
    }

    public RosActionClientAction getAction() {
        return this.action;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JsonNode getLastFeedback() {
        return this.lastFeedback;
    }

    public void setLastFeedback(JsonNode lastFeedback) {
        this.lastFeedback = lastFeedback;
    }

    public JsonNode getResult() {
        return this.result;
    }

    public void setResult(JsonNode result) {
        this.result = result;
    }

    public boolean isFinished() {
        if (this.status == STATUS_SUCCEEDED) {
            return true;
        }

        if (this.status == STATUS_CANCELED) {
            return true;
        }

        if (this.status == STATUS_ABORTED) {
            return true;
        }

        return false;
    }

    public String getStatusName() {
        if (this.status == STATUS_ACCEPTED) {
            return "accepted";
        }

        if (this.status == STATUS_EXECUTING) {
            return "executing";
        }

        if (this.status == STATUS_CANCELING) {
            return "canceling";
        }

        if (this.status == STATUS_SUCCEEDED) {
            return "succeeded";
        }

        if (this.status == STATUS_CANCELED) {
            return "canceled";
        }

        if (this.status == STATUS_ABORTED) {
            return "aborted";
        }

        return "unknown";
    }

    @Override
    public String toString() {
        String description;

        description = "RosActionGoal [";
        description = description + "goalId=" + this.goalId;
        description = description + ", action=";
        description = description + this.action.getRosActionName();
        description = description + ", status=";
        description = description + this.getStatusName();
        description = description + ", lastFeedback=";
        description = description + this.lastFeedback;
        description = description + ", result=";
        description = description + this.result;
        description = description + "]";

        return description;
    }
}
