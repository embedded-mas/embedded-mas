#include<Embedded_Protocol_2.h>
#include<ArduinoJson.h>
#include<NewPing.h>

Communication communication;

//DEFININDO VARIÁVEIS DO SENSOR
#define TRIGGER_PIN A0
#define ECHO_PIN A1
int leitura = 1;
int sensor_state = 5;

int time = 0;

NewPing sonar (TRIGGER_PIN, ECHO_PIN, 200);

//DEFININDO VARIÁVEIS MOTORES
int in[4] = {4, 5, 6, 7};

const int LIGHT_RUN = 2;
int parado = 0;
const int LIGHT_STOP = 3;

void setup() 
{
  pinMode(LIGHT_RUN,OUTPUT);
  pinMode(LIGHT_STOP,OUTPUT);
  digitalWrite(LIGHT_STOP, HIGH);

  for(int i = 0; i < 5; i++){
    pinMode(in[i], OUTPUT);
  }

  Serial.begin(9600);

  time = millis();
  /* Build perception that the lihgt is on and send it to the upper layers*/ 
  communication.startBelief("init");
  communication.beliefAdd(1);
  communication.beliefAdd(time);
  communication.endBelief();
  communication.sendMessage();
  
  delay(10000); //wait 10 seconds (to set up the multi-agent system)
  digitalWrite(LIGHT_STOP, LOW);

}

void loop() 
{
  int distance = sonar.ping_cm();
  if(distance == 0) distance = 1000;
  
  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
    String s = Serial.readString();

    if(s.equals("occuped_way")){ //if the agent sends "light_on", then switch the light on
      time = millis();
      HALT();
      digitalWrite(LIGHT_RUN, LOW);
      digitalWrite(LIGHT_STOP, HIGH);
      communication.startBelief("acao_concluida");
      communication.beliefAdd(time);
      communication.endBelief();
      communication.sendMessage();         
    }

    if(s.equals("no_occuped_way")){ //if the agent sends "light_on", then switch the light on
      time = millis();
      digitalWrite(LIGHT_RUN, HIGH);
      digitalWrite(LIGHT_STOP, LOW);
      FRONT();
      communication.startBelief("acao_concluida");
      communication.beliefAdd(time);
      communication.endBelief();
      communication.sendMessage();      
    }
      leitura = 1;
  }      

  if(leitura == 1){
    if(distance < 15 && sensor_state != 1){
      sensor_state = 1;
      time = millis();
      communication.startBelief("sensor_state");
      communication.beliefAdd(sensor_state);
      communication.beliefAdd(time);
      communication.endBelief();
      communication.sendMessage();
      leitura = 0;
    }
    else if(distance >= 15 && sensor_state != 0){
      sensor_state = 0;
      time = millis();
      communication.startBelief("sensor_state");
      communication.beliefAdd(sensor_state);
      communication.beliefAdd(time);
      communication.endBelief();
      communication.sendMessage();
      leitura = 0;
    }
  }
}

void HALT(){
  for(int i = 0; i < 5; i++){
    digitalWrite(in[i], LOW);
  }
}

void FRONT(){
  digitalWrite(in[0], LOW);
  digitalWrite(in[1], HIGH);
  digitalWrite(in[2], HIGH);
  digitalWrite(in[3], LOW);
}

void BACK(){
  digitalWrite(in[1], LOW);
  digitalWrite(in[0], HIGH);
  digitalWrite(in[3], HIGH);
  digitalWrite(in[2], LOW);
}
