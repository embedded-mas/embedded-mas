#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças

Communication com;


float belief_1 = 1;
//int belief_2 = 11;
//String beleif_3 = "Hello World!";

void setup() {
  Serial.begin(9600);
  delay(5000);
}

void loop() {
  for(int i=0; i<10; i++){
    com.startBelief("belief_1");
    com.beliefAdd(belief_1);
    com.endBelief();
    

//    com.startBelief("belief_1");
//    com.beliefAdd(belief_2);
//    com.endBelief();
//
//    com.startBelief("belief_1");
//    com.beliefAdd(belief_3);
//    com.endBelief();

    com.sendMessage();
    belief_1++;
    delay(10);
  }
    delay(100000);
}
