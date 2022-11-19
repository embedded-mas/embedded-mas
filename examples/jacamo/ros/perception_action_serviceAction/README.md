# Example of ROS-Based agent

## Scenario

This example illustrates an agent whose (i) beliefs include values read from ROS topics and (ii) whose actions include wrinting in ROS Topics.

The scenario includes two topics `value1` and `value2`, which store integer values. When `value1` changes, the agent increments its value and writes it in `value2`. When `value2` changes, the agent increments its value and writes it in `value1`. This process runs in a loop.

The scenario also includes a topic `current_time`, which stores a string describing the current time. The agent perceives this information and updates the topic.

## Requirements
1. ROS (recommended [ROS Noetic](http://wiki.ros.org/noetic))
2. [Rosbridge](http://wiki.ros.org/rosbridge_suite/Tutorials/RunningRosbridge)


## Running the example

1. Start the roscore
```
roscore
```

2. Launch the bridge between ROS and Java
```
roslaunch rosbridge_server rosbridge_websocket.launch
```

3. Write some initial values in ROS topics
```
rostopic pub /value1 std_msgs/Int32 0
rostopic pub /current_time std_msgs/String "unknown"
```

4. Launch the JaCaMo application:

Linux:
```
./gradlew run
```
Windows:
```
gradlew run 
```

## Some notes on the ROS-Jason integration
This integration is part of a broader integration framework available [here](https://github.com/embedded-mas/embedded-mas)

Agents are configured in the jcm file (in this example, [perception_action.jcm](https://github.com/embedded-mas/ros-devs/tree/main/examples/perception_action)). 
Agents extend the class [`EmbeddedAgent`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/EmbeddedAgent.java), that extends a [Jason Agent](https://github.com/jason-lang/jason/blob/master/src/main/java/jason/asSemantics/Agent.java). In the example, this extension is implemented in the class [/src/main/java/DemoEmbeddedAgentROS.java](https://github.com/embedded-mas/ros-devs/blob/main/examples/perception_action/src/main/java/DemoEmbeddedAgentROS.java). Each `EmbeddedAgent` has a method `setupSensors()` to define where the perceptions come from.

An agent can connect with multiple ROS core. Additional connectons should be also be defined in [/src/main/java/DemoEmbeddedAgentROS.java](https://github.com/embedded-mas/ros-devs/blob/main/examples/perception_action/src/main/java/DemoEmbeddedAgentROS.java) if necessary (it is not the case in this example). Besides, an agent can connect with non-ros devices (not shown in this example). 


Values of topics are added to the belief base of the agent as `topic_name(topic_value)`. 
The agents use the [`defaultEmbeddedInternalAction`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/defaultEmbeddedInternalAction.java) to act upon external devices (or to write values in topics, in this example). This internal action is decoupled of any external device or ROS topic. They are supposed to be translated to physical actions by the interface between the agent and the proper physical device. I this example, this is done in [/src/main/java/MyRosMaster.java](https://github.com/embedded-mas/ros-devs/blob/main/examples/perception_action/src/main/java/MyRosMaster.java).

