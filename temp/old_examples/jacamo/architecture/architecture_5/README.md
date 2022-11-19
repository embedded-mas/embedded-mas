# architecture 5 example

This example shows the belief updating based on values from the microcontroller. 

The microcontroller produces some beliefs (with no relation with any sensor) and sends it to the agent. The agent reacts to the new belief by printing messages.


### Running the example
1. Check the serial port which the Arduino is connected to.

	1.1 In Debian base systems:

        
		1.1.1 Before to connect the Arduino, type ls /dev/ttyUSB*

                1.1.2 Connect the Arduino and type ls /dev/ttyUSB* again. The new listed value is the serial port.

1. Make sure you are permitted to write in the serial port which the Arduino is connected to. 

	1.1 In Unix systems, use sudo chmod a+rw /dev/{SERIAL_NAME} to enable writing in serial 
		
		(e.g. sudo chmod a+rw /dev/ttyUSB0)

1. Set the proper serial port in the src/main/java/DemoEmbeddedAgent.java
1. Load the program arduino/create_belief_with_lib_ArduinoJson/create_belief_with_lib_ArduinoJson.ino to an Arduino
1. In a terminal, type ```./gradlew run ```
