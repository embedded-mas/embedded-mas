#include<Embedded_Protocol.h>
#include<ArduinoJson.h>

#define botaoOnOff 2
#define ledAzul 13

Communication communication;
const int capacity = JSON_OBJECT_SIZE(6);
StaticJsonDocument<capacity> doc;
int interrupt2 = 0;


void setup()
{
  Serial.begin(9600);
  pinMode(botaoOnOff, INPUT);
  pinMode(ledAzul, OUTPUT);
  attachInterrupt(digitalPinToInterrupt(botaoOnOff),ladOnOff, RISING);
}

void ladOnOff(){
 interrupt2 = 1;
}

void loop()
{
 if(interrupt2){
   digitalWrite(ledAzul, digitalRead(ledAzul) ^1);
   interrupt2 = 0;
   //delay(10);
 }
 
if(digitalRead(ledAzul)){
   doc["led"] = "ligado";
}else{
   doc["led"] = "desligado";}

 doc["milis"] = millis();
 
  if(Serial.available() > 0)
  {  char c = Serial.read();
  delay(1000);
    if(c == 'b')
    {
      String output = doc.as<String>();
      communication.create_belief(output);
     Serial.println();
    }
  delay(500); 
  }
}
