//Programa do atuador

#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
Communication com;

const int LIGHT = 13;
int led = 0;

void setup() {
  pinMode(LIGHT, OUTPUT);
  digitalWrite(LIGHT, LOW); //light starts off
  
  Serial.begin(9600);
  delay(5000); //wait 2 seconds (to set up the multi-agent system)
}


void loop()
{
  //-------------------------------------------------------  ATUAÇAO  -------------------------------------------------------------
  while (Serial.available() > 0) { //check whether there is some information from the serial (possibly from the agent)
    char c = Serial.read();
    switch (c) {
      case 'Z': // Comando para acender o led de controle
          digitalWrite(LIGHT, HIGH);
          led = 1;
        break;
      case 'X': // Comando para apagar o led de controle
          digitalWrite(LIGHT, LOW);
          led = 0;
        break;   
      default:
        // comando(s)
        break;
      }
    }

  //---------------------------------------------------------  PROCESSAMENTO  -------------------------------------------------------
  

  //----------------------------------------------------------  ENVIO DE CRENÇAS  -----------------------------------------------------------------

  // nomes as crenças deve começar com letra minuscula
  com.startBelief("led");
  com.beliefAdd(led);
  com.endBelief();
  
  com.sendMessage();
  
}
