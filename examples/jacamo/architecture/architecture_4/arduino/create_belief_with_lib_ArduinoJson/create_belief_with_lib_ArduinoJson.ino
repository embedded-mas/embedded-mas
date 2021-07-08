#include<Embedded_Protocol.h>
#include<ArduinoJson.h>

Communication communication;
const int capacity = JSON_OBJECT_SIZE(6);
StaticJsonDocument<capacity> doc;
int num = 1500;

void setup() {
  Serial.begin(9600);
}

void loop() {
  doc["value"] = num;
  doc["lat"] = 48.748010;
  doc["lon"] = 2.293491;
  doc["teste"] = "ola";
  doc["vetor"] = serialized("[1,2,\"ola\",\"teste\"]");
  
  String output = doc.as<String>();
  communication.create_belief(output);
  Serial.println();
  delay(500); 
  num++;
}
