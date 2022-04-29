//pin definition
const int ACTUATOR_1 = 13;
const int ACTUATOR_2 = 12;

#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
Communication com;

  //beliefs
int actuator1State = 0;
int actuator2State = 0;

void setup() 
{
  pinMode(ACTUATOR_1,OUTPUT);
  digitalWrite(ACTUATOR_1,0);  //Actuator starts off
  pinMode(ACTUATOR_2,OUTPUT);
  digitalWrite(ACTUATOR_2,0);  //Actuator starts off
  Serial.begin(9600);

  delay(5000); //wait 5 seconds (to set up the multi-agent system)
}

void loop() 
{
  //Açoes-----------------------------------------------------------------------------------------------------------
  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
     char c = Serial.read();
       switch (c) {
    case 'A':
       digitalWrite(ACTUATOR_1,1); 
       actuator1State = 1; 
       break;
    case 'B':
      digitalWrite(ACTUATOR_1,0);
      actuator1State = 0;
      break;
    case 'C':
      digitalWrite(ACTUATOR_2,1); 
      actuator2State = 1;
      break;
    case 'D':
      digitalWrite(ACTUATOR_2,0);
      actuator2State = 0;
      break;  
    default:
 
      break;
  }
         
   }  
  
   //Crenças---------------------------------------------------------------------------------------------------------
    com.startBelief("actuator1State");
    com.beliefAdd(actuator1State);
    com.endBelief();
    
    com.startBelief("actuator2State");
    com.beliefAdd(actuator2State);
    com.endBelief();

    com.sendMessage();
    delay(500);
     

}
