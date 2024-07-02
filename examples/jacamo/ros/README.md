### Examples of JaCaMo-ROS integration

This folder includes examples of Jason agents with ROS. All the examples illustrate perceptions/beliefs acquired from reading of ROS topics. Besides, different examples illustrate different possible actions of Jason-ROS integrated agents:

1. [perception_action_topicWritingAction](perception_action_topicWritingAction): actions of the agents are based on writing in ROS topics.
1. [perception_action_serviceAction](perception_action_serviceAction): actions of the agents are based on requesting ROS services.

These are basic examples of Jason-ROS integration. Advanced examples are available [here](https://github.com/embedded-mas/ros-devs/tree/main/examples).



## Some notes on the ROS-Jason integration
This integration is part of a broader integration framework available [here](https://github.com/embedded-mas/embedded-mas)

Agents are configured in the a .`jcm` file, as usual in JaCaMo. 
Agents extend the class [`EmbeddedAgent`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/EmbeddedAgent.java), that extends a [Jason Agent](https://github.com/jason-lang/jason/blob/master/src/main/java/jason/asSemantics/Agent.java). 

An agent can connect with multiple ROS nodes. The connecton of an agent coded in the file `src/agt/<agent_file>.asl` is specified in the file `src/agt/<agent_file>.yaml`. This specification includes details on the connection with ROS nodes, as well as the mapping from topics to perceptions/beliefs and from topics/services to actions.

Values of topics are added to the belief base of the agent as `topic_name(topic_value)`. 
The agents use the [`defaultEmbeddedInternalAction`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/defaultEmbeddedInternalAction.java) to trigger actions enabled by physical/simulated actuators managed by ROS topics and services.  

