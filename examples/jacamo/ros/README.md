### Examples of JaCaMo-ROS integration

This folder includes examples of Jason agents with ROS. All the examples illustrate perceptions/beliefs acquired from reading of ROS topics. Besides, different examples illustrate different possible actions of Jason-ROS integrated agents:

1. [perception_action_topicWritingAction](perception_action_topicWritingAction): actions of the agents are based on writing in ROS topics.
1. [perception_action_serviceAction](perception_action_serviceAction): actions of the agents are based on requesting ROS services.

These are basic examples of Jason-ROS integration. Advanced examples are available [here](https://github.com/embedded-mas/ros-devs/tree/main/examples).



## Some notes on the ROS-Jason integration
This integration is part of a broader integration framework available [here](https://github.com/embedded-mas/embedded-mas)

Agents are configured in the jcm file (in this example, [perception_action.jcm](https://github.com/embedded-mas/ros-devs/tree/main/examples/perception_action)). 
Agents extend the class [`EmbeddedAgent`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/EmbeddedAgent.java), that extends a [Jason Agent](https://github.com/jason-lang/jason/blob/master/src/main/java/jason/asSemantics/Agent.java). In the example, this extension is implemented in the class [/src/main/java/DemoEmbeddedAgentROS.java](https://github.com/embedded-mas/ros-devs/blob/main/examples/perception_action/src/main/java/DemoEmbeddedAgentROS.java). Each `EmbeddedAgent` has a method `setupSensors()` to define where the perceptions come from.

An agent can connect with multiple ROS core. Additional connectons should be also be defined in [/src/main/java/DemoEmbeddedAgentROS.java](https://github.com/embedded-mas/ros-devs/blob/main/examples/perception_action/src/main/java/DemoEmbeddedAgentROS.java) if necessary (it is not the case in this example). Besides, an agent can connect with non-ros devices (not shown in this example). 


Values of topics are added to the belief base of the agent as `topic_name(topic_value)`. 
The agents use the [`defaultEmbeddedInternalAction`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/defaultEmbeddedInternalAction.java) to act upon external devices (or to write values in topics, in this example). This internal action is decoupled of any external device or ROS topic. They are supposed to be translated to physical actions by the interface between the agent and the proper physical device. I this example, this is done in [/src/main/java/MyRosMaster.java](https://github.com/embedded-mas/ros-devs/blob/main/examples/perception_action/src/main/java/MyRosMaster.java).

