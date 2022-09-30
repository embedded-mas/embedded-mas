This program checks the required time to turn on a led N times, where N is a variable defined in the program beginning.
The result is printed when the program is finished.

## Running:
  1. connect a led in the digital output #13 of an arduino
  2. upload the file arduino/lights/lights.ino to an arduino device
  3. if needed, assign permissions to read/write in the serial port. In Unix systems, use ```sudo chmod a+rw /dev/ttyUSB0``` (replacing "ttyUSB0" by the proper usb port)
  4. set the proper usb port in /src/main/java/serial_read/SerialRead.java
  5. in a terminal, type ```./gradlew run --console=plain``` (Unix/Linux) or ```gradlew run --console=plain ``` (Windows)

