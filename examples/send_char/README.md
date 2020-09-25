# send_char example

This example shows how to send a single char to an Arduino. 

It considers an environment setup with two leds, "a" and "b", plugged in the outputs 9 and 10 of an Arduino. The user types "a" or "b" and the leds turn on accordingly with the input


### Running the example
1. Make sure you are permitted to write in the serial port which the Arduino is connected to. 

	1.1 In Unix systems, use sudo chmod a+rw /dev/{SERIAL_NAME} to enable writing in serial 
		
		(e.g. sudo chmod a+rw /dev/ttyUSB0)

2. Load the program [read_char](arduino/read_char/read_char.ino) to an Arduino
3. In a terminal, type ./gradlew run -q --console=plain
