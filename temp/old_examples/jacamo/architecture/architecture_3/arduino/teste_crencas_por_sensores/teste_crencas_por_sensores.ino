#include<Embedded_Protocol.h>
#include<ArduinoJson.h>

#define botaoOnOff 2
#define botaoContagem 3
#define ledAzul 13

Communication communication;
const int capacity = JSON_OBJECT_SIZE(6);
StaticJsonDocument<capacity> doc;
int interrupt2 = 0;
int interrupt3 = 0;
int cont = 0;

void setup()
{
  Serial.begin(9600);
  pinMode(botaoOnOff, INPUT);
  pinMode(botaoContagem, INPUT);
  pinMode(ledAzul, OUTPUT);
  attachInterrupt(digitalPinToInterrupt(botaoOnOff),ladOnOff, RISING);
  attachInterrupt(digitalPinToInterrupt(botaoContagem),contador, RISING);
}

void ladOnOff(){
 interrupt2 = 1;
}

void contador(){
 interrupt3 = 1;
}

void loop()
{
 if(interrupt2){
   digitalWrite(ledAzul, digitalRead(ledAzul) ^1);
   interrupt2 = 0;
   delay(10);
 }
 if(interrupt3){
   cont++;
   interrupt3 = 0;
   delay(10);
 }
 
if(digitalRead(ledAzul))
   doc["led"] = "ligado";
else
   doc["led"] = "desligado";

  doc["botao"] = cont;
  String output = doc.as<String>();
  communication.create_belief(output);
  Serial.println();
  delay(500); 
 
 /* teste de circuito no arduino
  Serial.print("led Azul : ");
  Serial.println(digitalRead(ledAzul));
  Serial.print("botao presionado: ");
  Serial.print(cont);
  Serial.println(" vezes");
  Serial.println();*/
}
