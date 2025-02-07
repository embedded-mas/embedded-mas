# Example of <em>mrs_msgs/Path</em> topic-based action

## Scenario

This example illustrates the execution of agents' actions that are actually realized by writing in a ROS topic whose data type is [<em>mrs_msgs/Path</em>](https://ctu-mrs.github.io/mrs_msgs/msg/Path.html). This is a complex data type that cannot be set in the yaml configuration of the agent in the current version of <em>embedded-mas</em>.

Actions that write on topics of this data type must be managed by an extension of the default Jason-ROS interface [DefaultRos4EmbeddedMas](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/ros/DefaultRos4EmbeddedMas.java). In this example, this extension is implemented by the class [<em>MyRosMaster</em>](src/java/MyRosMaster.java). In this class, the method <em>execEmbeddedAction</em> translates the agent's action to topic writings. To add the extension provided by <em>MyRosMaster</em> to the agent, it is set as the interface class between agent and ROS in line 8 of [sample_agent.yaml](src/agt/sample_agent.yaml). Notice that it does not replaces the configurations specified in sample_agent.yaml. It just adds some Jason-ROS integration features that cannot be specified in the yaml file.

This example illustrates two agent actions: `test_mrs_topic_action_full` and `test_mrs_topic_action_light`. The former is an action that requires the agent to specify all the parameters of a <em>mrs_msgs/Path</em>. The later requires to specify a subset of these parameters (in this example, an array of coordinates), while the remainder ones are hardcoded in the <em>MyRosMaster</em> interface.


## Running the example

### 1. Ros node setup:
In this example, the actions of the agents produce writings in a ROS topic called `/path_topic`. The easiest way to launch a ROS node that contains this topic is using Docker, through the following command:

```
sudo docker run -it --name ros --rm --net=ros -p9090:9090 maiquelb/embedded-mas-ros:0.7 \
   /bin/bash -c 'source /opt/ros/noetic/setup.bash && roscore &\
   (sleep 2 && source /opt/ros/noetic/setup.bash && \
    source /catkin_wsp/devel/setup.bash &&\ 
    roslaunch rosbridge_server rosbridge_websocket.launch) &\
   (sleep 3  && source /opt/ros/noetic/setup.bash  && cd /catkin_wsp/src &&\
    git clone https://github.com/CTU-MRS/mrs_msgs.git &&\
    cd /catkin_wsp &&\
    catkin_make &&\
    source /catkin_wsp/devel/setup.bash &&\
    cd /catkin_wsp/src &&\
    catkin_create_pkg simple_path_subscriber rospy mrs_msgs &&\
    cd /catkin_wsp/src/simple_path_subscriber &&\
    mkdir scripts &&\
    cd / &&\
    git clone https://github.com/embedded-mas/embedded-mas.git &&\
    cp /embedded-mas/tests/mrs_data_types/src/ros/path_subscriber.py /catkin_wsp/src/simple_path_subscriber/scripts/ &&\
    echo "catkin_install_python(PROGRAMS scripts/path_subscriber.py DESTINATION \${CATKIN_PACKAGE_BIN_DESTINATION})" >> /catkin_wsp/src/simple_path_subscriber/CMakeLists.txt &&\
    chmod +x /catkin_wsp/src/simple_path_subscriber/scripts/path_subscriber.py &&\
    cd /catkin_wsp &&\
    catkin_make &&\
    source /catkin_wsp/devel/setup.bash &&\
    rosrun simple_path_subscriber path_subscriber.py) &&\
   wait'
```

### 2. Launch the JaCaMo application:

#### Linux:
```
./gradlew run
```
#### Windows:
```
gradlew run 
```


