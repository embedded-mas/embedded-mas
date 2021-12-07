/* sudo chmod a+rw /dev/ttyUSB0 */

#include <Ultrasonic.h>

const int LIGHT = 13;
const int TRIG = 6; //e onde esta ligado o trig(6) e o echo(7) respectivamente
const int ECHO = 7;

int delay_time;
int light_state = 1;
Ultrasonic ultrassom(TRIG,ECHO); // define o nome do sensor(ultrassom)
long distancia=0;

void setup() 
{
  delay(10000); //wait 30 seconds (to set up the multi-agent system)
  
  pinMode(LIGHT,OUTPUT);
  digitalWrite(LIGHT,1);  //light starts on
     
  Serial.begin(9600);
  /* Build perception that the lihgt is on and send it to the upper layers */
 String msg = String("{\"light_state\":1 ,\n \"dist\":0 \n }"); // initial beliefs
 // String msg = String("{\"belief_string\": \"blablabla\",\n    \"belief_integer\": 99,\n  \"belief_float\": 3.14,\n  \"belief_array\": [\"abc\", 123, 1.99]\n }");
  Serial.print("==");
  Serial.print(msg.length());
  Serial.print("::");
  Serial.print(msg);
  Serial.println("--");
}

void loop() 
{
  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
     String s = Serial.readString();
     if(s.equals("light_on")){ //if the agent sends "light_on", then switch the light on
        digitalWrite(LIGHT,1); 
        light_state = 1; 
        /* Build perception that the lihgt is on and send it to the upper layers */
        String msg = String("{\"light_state\": 1}");
        Serial.print("==");
        Serial.print(msg.length());
        Serial.print("::");
        Serial.print(msg);
        Serial.println("--");
     } 
   }  
  
  
  
  if(light_state==1){ 
    /* wait a random time and switch the light off if it is on */
     delay_time = random(2,10)*1000;
     delay(delay_time);
     digitalWrite(LIGHT,0);
     light_state = 0;
     /* Build perception that the lihgt is on and send it to the upper layers */
     String msg = String("{\"light_state\": 0}");
     Serial.print("==");
     Serial.print(msg.length());
     Serial.print("::");
     Serial.print(msg);
     Serial.println("--");
  }
  
  
 
    
}
