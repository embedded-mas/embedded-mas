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

1. Load the program [read_char](arduino/lights/lights.ino) to an Arduino
1. In a terminal, type ```./gradlew run -q --console=plain ```
