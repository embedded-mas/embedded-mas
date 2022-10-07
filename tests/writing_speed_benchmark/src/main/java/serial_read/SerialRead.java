/*
    This program checks the required time to turn on a led N times, where N is a variable defined in the program beginning.
    The result is printed when the program is finished.
*/

package serial_read;

import arduino.Arduino;


public class SerialRead {

    public static void main(String[] args) {
    
        int N = 10;
        
        Arduino a = new Arduino("/dev/ttyUSB0", 9600);      
        a.openConnection();
        
        String s = "";	
        int count = 0;
        long start = 0;

        //clear data from serial
        a.serialReadAll();

        while(count<N){                      
           s = "";
           count++;
           System.out.println("turn on ("+count+")");  
           a.serialWrite("light_on"); //send the commando to turn on the light
           if(count==1) start = System.currentTimeMillis(); //records the time of the first turn on command
           while(!s.equals("==19::{\"light_state\":[0]}--\r")){ //read until detects light turned off
            s = a.serialRead(); //read from serial until getting a linebreak (\n)
         } 
	} 
	long finish = System.currentTimeMillis(); //registra o horário em que terminou a execução
        long timeElapsed = finish - start; //calcula a diferença entre o horario de início e de fim
	System.out.println("tempo decorrido: " + timeElapsed);  
    
    }

    
}

