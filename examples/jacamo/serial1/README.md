# Example: agent acting upon Arduino

This example illustrates an agent acting upon a microcontroller.

It considers an environment setup with two leds, "a" and "b", plugged in two outputs of an Arduino. The agents send the chars "a" or "b" and the leds turn on accordingly with the input
The Arduino program is available in the arduino folder. Default outputs for leds "a" and "b" are, respectively, 9 and 10. 


### Running the example
1. Check the serial port which the Arduino is connected to.

	1.1 In Debian based systems:
        
		1.1.1 Before to connect the Arduino, type ls /dev/ttyUSB*

                1.1.2 Connect the Arduino and type ls /dev/ttyUSB* again. The new listed value is the serial port.

1. Make sure you are permitted to write in the serial port which the Arduino is connected to. 

	1.1 In Unix systems, use sudo chmod a+rw /dev/{SERIAL_NAME} to enable writing in serial 
		
		(e.g. sudo chmod a+rw /dev/ttyUSB0)

1. Load the program [read_char](arduino/read_char/read_char.ino) to an Arduino
1. In a terminal, type ```./gradlew run -q --console=plain ```
