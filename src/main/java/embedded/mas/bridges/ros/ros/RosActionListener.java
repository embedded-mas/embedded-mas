package embedded.mas.bridges.ros.ros;

import com.fasterxml.jackson.databind.JsonNode;

public interface RosActionListener {

    void onFeedback(
            String goalId,
            String actionName,
            JsonNode values
    );

    void onResult(
            String goalId,
            String actionName,
            int status,
            boolean success,
            JsonNode values
    );
}
