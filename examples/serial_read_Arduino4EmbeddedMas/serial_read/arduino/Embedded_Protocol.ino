/*
 *  Embedded_Protocol example
 *  
 *  T: biblioteca usada pelo atuador (arduino) para mandar os dados "para cima"
 */

#include"Embedded_Protocol.h"

Communication communication;

void setup() {
  Serial.begin(9600);
}

void loop() {
  communication.send_message("Hello World");
  Serial.println();
}
