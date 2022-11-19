#include<Embedded_Protocol_2.h>
#include<ArduinoJson.h>

Communication communication;
const int capacity = JSON_OBJECT_SIZE(6);
StaticJsonDocument<capacity> doc;
int num = 1500;

void setup() {
  Serial.begin(9600);
}

void loop() {

   /*doc["value"] = num;
   doc["lat"] = 48.748010;
   doc["lon"] = 2.293491;
   doc["teste"] = "ola";
   doc["vetor"] = serialized("[1,2,\"ola\",\"teste\"]");
   */

   communication.startBelief("value");
   communication.beliefAdd(num);
   communication.endBelief();

   communication.startBelief("lat");
   communication.beliefAdd(48.748010);
   communication.endBelief();

   communication.startBelief("lon");
   communication.beliefAdd(2.293491);
   communication.endBelief();

   communication.startBelief("teste");
   communication.beliefAdd("ola");
   communication.endBelief();
   
  
   communication.sendMessage();
   delay(500); 
   num++;
}
