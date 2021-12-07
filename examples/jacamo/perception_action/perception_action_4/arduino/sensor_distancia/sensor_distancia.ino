//Leitura de distância com o sensor HC-SR04
#include <Ultrasonic.h>

 const int TRIG = 6; //e onde esta ligado o trig(6) e o echo(7) respectivamente
 const int ECHO = 7;
 const int LIGHT = 13;
  
 Ultrasonic ultrassom(TRIG,ECHO); // define o nome do sensor(ultrassom)
 double distancia;
 
void setup() {
  Serial.begin(9600); //Habilita Comunicação Serial a uma taxa de 9600 bauds.
  pinMode(LIGHT,OUTPUT);
  delay(10000); //(wait to set up the multi-agent system)
 }

// sem usar a biblioteca --------------------------------------------------------
// void loop()
// {
//   digitalWrite(LIGHT,1); 
//   distancia = ultrassom.Ranging(CM);// ultrassom.Ranging(CM) retorna a distancia em
//                                     // centímetros(CM) ou polegadas(INC)
//   String msg = String("{\"distance\": 1}");
//        Serial.print("==");
//        Serial.print(msg.length());
//        Serial.print("::");
//        Serial.print(msg);
//        Serial.println("--");
//   delay(100);
//   digitalWrite(LIGHT,0); 
//   delay(100);
// }

 //usando a biblioteca -----------------------------------------------------------
#include<Embedded_Protocol_2.h>
Communication com;
void loop(){
    digitalWrite(LIGHT,1);
    distancia = ultrassom.Ranging(CM);// ultrassom.Ranging(CM) retorna a distancia em
                                      // centímetros(CM) ou polegadas(INC)
    com.startBelief("distance");
    com.beliefAdd(distancia);
    com.endBelief();

    com.startBelief("light_state");
    com.beliefAdd("on");
    com.endBelief();

    com.sendMessage();
     delay(200);
     digitalWrite(LIGHT,0); 
     delay(300);
   }
 

 
