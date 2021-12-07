//Contantes
const int LIGHT = 13;
const int TRIG = 6; 
const int ECHO = 7;

#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
Communication com;

#include <Ultrasonic.h>
Ultrasonic ultrassom(TRIG,ECHO); // define o nome do sensor(ultrassom)

// Crenças
int light_state = 0;
float distancia=0;

void setup() 
{
  pinMode(LIGHT,OUTPUT);
  digitalWrite(LIGHT,1);  //light starts off
  Serial.begin(9600);

  delay(10000); //wait 30 seconds (to set up the multi-agent system)
}

void loop() 
{
  //Açoes-----------------------------------------------------------------------------------------------------------
  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
     String s = Serial.readString();
     
     if(s.equals("light_on")){ //if the agent sends "light_on", then switch the light on
        digitalWrite(LIGHT,1); 
        light_state = 1; 
     }
        
     if(s.equals("light_off")){
      digitalWrite(LIGHT,0); 
      light_state = 0;  
     }   
   }  
  
   //Crenças----------------------------------------------------------------------------------------------------------
    distancia = ultrassom.Ranging(CM);// ultrassom.Ranging(CM) retorna a distancia em
                                      // centímetros(CM) ou polegadas(INC)
    com.startBelief("distance");
    com.beliefAdd(distancia);
    com.endBelief();

    com.startBelief("light_state");
    com.beliefAdd(light_state);
    com.endBelief();

    com.sendMessage();
     delay(500);
}
