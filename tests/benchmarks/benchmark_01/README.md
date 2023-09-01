# Example: agent acting and perceiving the environment

This example simulates an embedded agent that (i) percepts the environment through the simulated sensors plugged in an Arduino and (ii) acts upon this environment.

The agent is equipped with an Arduino which has a led plugged to. If the led is on, it is switched off after a random time. The goal of the agent is to keep the led on. 

The agent has a perception "light_state(S)" (where S=1 means led on; S=0 means led off). The repertory of actions of the agent includes the action lightOn that turns the led on.


### Running the example
1. Check the serial port which the Arduino is connected to.

	1.1 In Debian based systems:
        
		1.1.1 Before to connect the Arduino, type ls /dev/ttyUSB*

                1.1.2 Connect the Arduino and type ls /dev/ttyUSB* again. The new listed value is the serial port.

1. Make sure you are permitted to write in the serial port which the Arduino is connected to. 

	1.1 In Unix systems, use sudo chmod a+rw /dev/{SERIAL_NAME} to enable writing in serial 
		
		(e.g. sudo chmod a+rw /dev/ttyUSB0)

1. Load [this code](arduino/lights/lights.ino) to an Arduino

1. In a terminal, type ```./gradlew run -q --console=plain ```


#### Arduino requirements ###
1. [ArduinoJson](https://arduinojson.org/)

1. [Rosserial Arduino Library](https://github.com/frankjoshua/rosserial_arduino_lib)

   2.1 Procedure required after installation: edit the ```msg.h``` file (usually located in Arduino/libraries/Rosserial_Arduino_Library/src/ros in line 40 write ```#include <string.h>``` and in line 68 and 182 remove ```std:: before memcpy``` 
   
1. Embedded Protocol 

   3.1 Installation: copy the [Embedded_Protocol_2 folder](https://github.com/embedded-mas/embedded-mas/tree/master/src/arduino/Embedded_Protocol_2) to the Arduino libraries folder (in Linux, tipically ~/Arduino/)



## Some notes on the Jason-Arduino integration
This integration is part of a broader integration framework available [here](https://github.com/embedded-mas/embedded-mas)

Agents are configured in the jcm file (in this example, [perception_action.jcm](perception_action.jcm)). This example has what we call a <em>cyber-physical agent</em>, which is a software agent that includes physical elements. It may get perceptions from sensors while its actions' repertory may include those enabled by physical actuators. Cyber-physical agents are implemented by the class [`CyberPhysicalAgent`](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/CyberPhysicalAgent.java), that extends [Jason Agents](https://github.com/jason-lang/jason/blob/master/src/main/java/jason/asSemantics/Agent.java). The physical portion of cyber-physical agents is set up in a yaml file with the same name and placed in the same folder as the asl file where the agent is specified. In this example, this file is placed [here](src/agt/sample_agent.yaml).


A cyber-physical agent can be composed of one to many <em>devices</em> which are defined in the yaml configuration file. A <em>device</em> is any external element which sensors and actuators are connected to. A device that may be either physical (e.g. an Arduino board), or virtual (e.g. a ROS core). Each device has a unique identifier set up in the ```device_id``` key of the yaml file. In this example, the agent is composed of a single device, that is a microcontroller connected to a serial port (Arduino, Esp32, etc). An agent can connect with multiple devices, if necessary. Any device is implemented by a Java class that provides interfaces between the parception/action systems of the agent and the real device, according to the [IDevice interface](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/IDevice.java). In this example, it is implemented by the class [JSONWatcherDevice](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/JSONWatcherDevice.java). The device implementing class is defined in the ```className``` key of the configuration file.


A device has a <em>microcontroller</em>, which is a Java interface that enables reading from and writing in the physical/virtual device. This interface is set up under the ```microcontroller``` key in the yaml file. Any microcontroller has an identifier, defined in the key ```id```. Any microcontroller implementation must implement the [IExternalInterface](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/IExternalInterface.java). In this example, it is implemented by the class [Arduino4EmbeddedMas](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/javard/Arduino4EmbeddedMas.java). The device implementing class is defined in the ```microcontroller/className``` key of the configuration file. Different microcontrollers have different parameters depending on their nature. In this example, the parameters are related to the configuration of the serial device. They are ```serial```, which specifies the serial port which the microcontroller is connected to, and ```baudRate```, which specifies the baud rate of the microcontroller.


### Perception aquisition
The sensors connected to a <em>device</em> may be source of perceptions of the agent. In this example, the [Arduino program](../arduino/lights/lights.ino) sends the sensors information to the serial port in a JSON format, following a so called [embedded protocol](https://github.com/embedded-mas/embedded-mas/tree/master/src/arduino/Embedded_Protocol_2). This infrmation is handled by the [JSONWatcherDevice](https://github.com/embedded-mas/embedded-mas/blob/master/src/main/java/embedded/mas/bridges/jacamo/JSONWatcherDevice.java) and automatically converted in perceptions, and, then, in beliefs.

### Action configuration
The actions enabled by the physical/virtual device are included in the agent's action repertory. The agent executes an <em>action</em> that is realized through an <em>actuation</em> of the physical actuators. Actions and atuations are configured in the ```serialActions``` element of the yaml file.