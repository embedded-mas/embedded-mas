//Programa do sensor

#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
Communication com;

const int BUTTON = 7;
const int LED = 13; //led interno do arduino

int botao = 0;
bool enviaCrenca = true;

void setup() {
  pinMode(BUTTON, INPUT);
  pinMode(LED,OUTPUT);
  digitalWrite(LED, LOW);
  
  Serial.begin(9600);
  delay(5000); //wait 2 seconds (to set up the multi-agent system)
}

void loop()
{
  //-------------------------------------------------------  ATUAÇAO  -------------------------------------------------------------
//  while (Serial.available() > 0) { //check whether there is some information from the serial (possibly from the agent)
//    char c = Serial.read();
//    switch (c) {
//      case 'Z': // Comando para acender o led de controle
//          // atuaçao
//        break; 
//      default:
//        // comando(s)
//        break;
//      }
//    }

  //---------------------------------------------------------  PROCESSAMENTO  -------------------------------------------------------
  //botao = digitalRead(BUTTON);

  if(digitalRead(BUTTON)){
    
    botao = !botao;
    digitalWrite(LED,!digitalRead(LED));
    enviaCrenca=true;
    delay(200);
  }

  //----------------------------------------------------------  ENVIO DE CRENÇAS  -----------------------------------------------------------------

  // nomes as crenças deve começar com letra minuscula
  if(enviaCrenca){
    com.startBelief("botao");
    com.beliefAdd(botao);
    com.endBelief();
    
    com.sendMessage();
    enviaCrenca=false;
  }
  
  
}
