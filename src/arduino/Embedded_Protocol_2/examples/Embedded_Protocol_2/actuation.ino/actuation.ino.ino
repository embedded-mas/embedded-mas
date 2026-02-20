/**
 * This program receives a value with three arguments and print them. The 1st argument must be an integer, the 2nd must be a string, and the 3rd must be a float - e.g. test(3,"hello", 3.14)
 * 
 * 
 * 
 */

#include<Embedded_Protocol_2.h>

Communication communication;


void setup() 
{
   Serial.begin(9600);  
}

void loop() 
{
  while(Serial.available() > 0){ //check whether there is some information from the serial
     String s = Serial.readString();     
     Serial.println("Actuation name: " + communication.actuationName(s));
     Serial.println("1st parameter: " + String(communication.paramInt(s,0)));
     Serial.println("2nd parameter: " + communication.paramStr(s,1));
     Serial.println("3r parameter: " + String(communication.paramFloat(s,2)));
     
        
     } 
   }  
  
  
  
  
    
