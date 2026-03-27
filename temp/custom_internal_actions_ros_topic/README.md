# 1. Requirements
<!-- 1. ROS (recommended [ROS Noetic](http://wiki.ros.org/noetic) or [ROS 2 Humble](https://docs.ros.org/en/humble/index.html))
2. [Rosbridge](http://wiki.ros.org/rosbridge_suite/Tutorials/RunningRosbridge)
3. [Turtlesim](http://wiki.ros.org/turtlesim)
-->
- Java JRE >= 21
- [Docker](https://www.docker.com/)


<!-- 2. 
3. Turtlesim ROS-based simulator infrastructure, available in some of the following options:   
  2.1. [Docker](https://www.docker.com/) (recommended - in the case of container-based simulation setup)   or  
  2.2.  (in the case of local simulation setup)  -->

# 2. Running the example
Running the example requires two steps:

1. Launch the simulation (cf. Section 2.1 below) 

2. Launch the JaCaMo application (cf. Section 2.2 below)


## 2.1. Launch the simulation:

Use the following commands to launch the ROS nodes: ```./launch_ros.sh``` (preceed with ```sudo``` if needed)


Then, go to [http://localhost:8080/vnc.html](http://localhost:8080/vnc.html) to inspect the turtle simulator.

## 2.2. Launch the JaCaMo application:

Linux:
```
./gradlew run
```

Windows:
```
gradlew run 
```

