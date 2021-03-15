# serial_write_Arduino4EmbeddedMas example

This example shows how to send a single char to a serial port using the class Arduino4EmbeddedMas. 

It considers an environment setup with two leds, "a" and "b", plugged in the outputs 9 and 10 of an Arduino. The user types "a" or "b" and the leds turn on accordingly with the input


### Running the example
1. Check the serial port which the Arduino is connected to.

	1.1 In Debian base systems:

        
		1.1.1 Before to connect the Arduino, type ls /dev/ttyUSB*

                1.1.2 Connect the Arduino and type ls /dev/ttyUSB* again. The new listed value is the serial port.

1. Make sure you are permitted to write in the serial port which the Arduino is connected to. 

	1.1 In Unix systems, use sudo chmod a+rw /dev/{SERIAL_NAME} to enable writing in serial 
		
		(e.g. sudo chmod a+rw /dev/ttyUSB0)

1. Load the program [read_char](arduino/read_char/read_char.ino) to an Arduino
1. In a terminal, type ```gradle run -q --console=plain ```
