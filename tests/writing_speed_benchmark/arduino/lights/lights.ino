#include<Embedded_Protocol_2.h>
#include<ArduinoJson.h>

Communication communication;
const int capacity = JSON_OBJECT_SIZE(6);
StaticJsonDocument<capacity> doc;

const int LIGHT = 13;

int delay_time;
int light_state = 1;

void setup() 
{
  pinMode(LIGHT,OUTPUT);
  digitalWrite(LIGHT,1);  //light starts on
     
  Serial.begin(9600);
  Serial.println("==19::{\"light_state\":[1]}--"); //envia para a serial uma mensagem informando que a luz está acesa
  delay(10000); //wait 30 seconds (to set up the multi-agent system)
}

void loop() 
{
  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
     String s = Serial.readString();
     if(s.equals("light_on")){ //if the agent sends "light_on", then switch the light on
        digitalWrite(LIGHT,1); 
        light_state = 1; 
        Serial.println("==19::{\"light_state\":[1]}--"); //envia para a serial uma mensagem informando que a luz está acesa
     } 
   }  
  
  
  
  if(light_state==1){ 
    /* wait a random time and switch the light off if it is on */
     delay_time = 1000;
     delay(delay_time);
     digitalWrite(LIGHT,0);
     light_state = 0;
     Serial.println("==19::{\"light_state\":[0]}--"); //envia para a serial uma mensagem informando que a luz está apagada
  }
  
  
 
    
}
