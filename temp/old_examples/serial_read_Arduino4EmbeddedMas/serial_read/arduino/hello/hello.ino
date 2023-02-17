#include<Embedded_Protocol_2.h>

Communication communication;

void setup() {
  Serial.begin(9600);
}

void loop() {
   communication.startBelief("msg");
   communication.beliefAdd("Hello World");
   communication.endBelief();

   communication.sendMessage();
}
