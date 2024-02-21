# Example of ROS-Based agent

## Scenario
This example illustrates agent actions that correspond to ROS service requests. It contains a randomly moving turtle agent (see the agent code [here](src/agt/sample_agent.asl)). The turtle agent moves itself by executing the action ```move_turtle```. This action is concretely realized through the ROS service ```/turtle1/teleport_relative```. This service does not have a response message. Actions based on services without response handling are triggered by the ```defaultEmbeddedInternalAction``` internal action.

To illustrate actions that consider the service responses, the agent also executes the action ```get_loggers```.  This action is concretely realized through the ROS service ```/turtlesim/get_loggers```. This service produces a response, which the agent prints in the console. Actions based on services with response handling are triggered by the ```requestResponseEmbeddedInternalAction``` internal action.





=== Requirements
1. ROS (recommended [ROS Noetic](http://wiki.ros.org/noetic))
2. [Rosbridge](http://wiki.ros.org/rosbridge_suite/Tutorials/RunningRosbridge)
3. [Turtlesim](http://wiki.ros.org/turtlesim)


## Running the example


### 1. Ros node setup:
It is possible to choose between a container-based setup (only Docker is required) and a local setup (ROS core and related tools are required).

#### 1.1 Container-based setup: 
Requirements: [Docker](https://www.docker.com/)

First of all, make sure that there is no container named ```novnc```, ```roscore```, or ```embedded-mas-example```. Then, use the following commands to launch the nodes either in ROS 1 or in ROS 2:
##### 1.1.1 ROS 1: 
   ```
sudo docker run -d --rm --net=ros --env="DISPLAY_WIDTH=3000" --env="DISPLAY_HEIGHT=1800" --env="RUN_XTERM=no" --name=novnc -p=8080:8080 theasp/novnc:latest && \
sudo docker run -d --net=ros --name roscore --rm osrf/ros:noetic-desktop-full roscore && \
sudo docker run -it --net=ros --env="DISPLAY=novnc:0.0" --env="ROS_MASTER_URI=http://roscore:11311" \
    --rm --name embedded-mas-example -p9090:9090 maiquelb/embedded-mas-ros:0.6 /bin/bash -c "source /opt/ros/noetic/setup.bash && rosrun turtlesim turtlesim_node" & \
(until sudo docker exec embedded-mas-example /bin/bash -c "echo '***** ROS container is ready *****'" 2>/dev/null; do
    echo "waiting for ROS container to start..."
    sleep 1
done)  && \
sleep 1 && \
sudo docker exec  embedded-mas-example /bin/bash -c "source /opt/ros/noetic/setup.bash && echo && echo && echo 'The ROS container is ready.' && echo 'Rosbridge is being launched' && echo 'The Multi-Agent System can be started. ' && echo && echo && roslaunch rosbridge_server rosbridge_websocket.launch "

   ```
##### 1.1.2 ROS 2:
```
sudo docker run -d --rm --net=ros --env="DISPLAY_WIDTH=3000" --env="DISPLAY_HEIGHT=1800" --env="RUN_XTERM=no" --name=novnc -p=8080:8080 theasp/novnc:latest  && \
sudo docker run -d --net=ros --name roscore --rm osrf/ros:noetic-desktop-full roscore && \
sudo docker run -it --net=ros --env="DISPLAY=novnc:0.0" --env="ROS_MASTER_URI=http://roscore:11311" --rm --name embedded-mas-example -p9090:9090 maiquelb/embedded-mas-ros2:0.5 /bin/bash -c "source /opt/ros/humble/setup.bash && ros2 run turtlesim turtlesim_node" & \
(until sudo docker exec embedded-mas-example /bin/bash -c "echo '***** ROS container is ready *****'"; do echo "waiting for ROS container to start..."; sleep 1; done  && \
sudo docker exec -d embedded-mas-example /bin/bash -c "source /opt/ros/humble/setup.bash && ros2 launch rosbridge_server rosbridge_websocket_launch.xml")
```

#### 1.2 Local setup: 
Requirements
1. ROS 1 (recommended [ROS Noetic](http://wiki.ros.org/noetic)) or ROS 2 (recommended [ROS Humble](http://wiki.ros.org/humble))
2. [Rosbridge](http://wiki.ros.org/rosbridge_suite/Tutorials/RunningRosbridge)

To run the ROS node in your computer, run the following steps:

##### 1.2.1  Start the roscore:
ROS 1: ``` roscore ```

ROS 2: this step is not requred.

##### 1.1.2. Launch the bridge between ROS and Java
ROS 1:
```
roslaunch rosbridge_server rosbridge_websocket.launch
```

ROS 2:
```
ros2 launch rosbridge_server rosbridge_websocket_launch.xml
```

##### 1.1.3. Launch the turtlesim simulation
ROS 1: 
```
rosrun turtlesim turtlesim_node
```
ROS 2:
```
ros2 run turtlesim turtlesim_node
```



### 2. Launch the JaCaMo application:

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

Agents are configured in the jcm file (in this example, [perception_action.jcm](perception_action.jcm)). This example has what we call a <em>cyber-physical agent</em>, which is a software agent that includes physical elements. It may get perceptions from sensors while its actions' repertory may include those enabled by physical actuators. Cyber-physical agents are implemented by the class [`CyberPhysicalAgent`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/CyberPhysicalAgent.java), that extends [Jason Agents](https://github.com/jason-lang/jason/blob/master/src/main/java/jason/asSemantics/Agent.java). The physical portion of cyber-physical agents is set up in a yaml file with the same name and placed in the same folder as the asl file where the agent is specified. In this example, this file is placed [here](src/agt/sample_agent.yaml).


A cyber-physical agent can be composed of one to many <em>devices</em>, which are defined in the yaml configuration file. A <em>device</em> is any external element which sensors and actuators are connected to. A device that may be either physical (e.g. an Arduino board), or virtual (e.g. a ROS core). Each device has a unique identifier, which is set in the ```device_id``` key of the yaml file. In this example, the agent is composed of a single device, that is a ROS core identified as <em>sample_roscore</em>. An agent can connect with multiple ROS core, if necessary (it is not the case in this example). Besides, an agent can connect with non-ros devices (not shown in this example). Any device is implemented by a Java class that provides interfaces between the parception/action systems of the agent and the real device, according to the [IDevice interface](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/IDevice.java). In this example, it is implemented by the class [RosMaster](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/ros/RosMaster.java). The device implementing class is defined in the ```className``` key of the configuration file. In addition, a <em>device</em> has three essential configuration items: <em>microcontrollers</em>, <em>perception sources</em>, and <em>enabled actions</em>. These items are explained below.


### Microcontrolers configuration
A device has a <em>microcontroller</em>, which is a Java interface that enables reading from and writing in the physical/virtual device. This interface is set up under the ```microcontroller``` key in the yaml file. Any microcontroller has an identifier, defined in the key ```id```. Any microcontroller implementation must implement the [IExternalInterface](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/IExternalInterface.java). In this example, it is implemented by the class [DefaultRos4EmbeddedMas](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/ros/DefaultRos4EmbeddedMas.java). The device implementing class is defined in the ```microcontroller/className``` key of the configuration file. In addition, different microcontrollers may have some parameters that are depending on their nature. For example, serial devices like Arduino require configuring serial ports and baud rates. In this example, the microcontroller is a ROS-Java interface. It has a ROS specific parameter, whose key is ```connectionString```. It sets the connection string to the ROS core (e.g. ws://localhost:9090).


### Perception configuration
The sensors connected to a <em>device</em> may be source of perceptions of the agent. If the device is a ROS core, then these sensors are abstracted through topics. The list of topics that produce perceptions is configured in the ```perceptionTopics``` item in the yaml file.  Each topic requires to define its name and its type, through the keys ```topicName``` and ```topicType```, respectively. The key ```beliefName``` defines the belief identifier (or <em>functor</em>) corresponding belief. For instance in this example, the topic ```turtle1/pose``` produces the belief ```turtle_position```. The ```beliefName``` configuration is optional. If it is omitted, the belief has the same identifier as the topic.

### Action configuration   
The actions enabled by the actuators connected to a <em>device</em> may be included in the agent's action repertory. If the device is a ROS core, then these actions may be realized both through topic writings and service requests, configured in the keys ```topicWritingActions``` and ```serviceRequestActions``` of the yaml file, respectively. In this example, the agent performs only service request actions, that require the following configurations:
    
   - ```actionName```: the name of the action performed by the agent;

   - ```serviceName```: the name of the service to be called;

   - ```params```: a list of the names of parameters required by the service.



