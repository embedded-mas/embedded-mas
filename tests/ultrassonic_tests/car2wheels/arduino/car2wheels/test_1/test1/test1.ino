 
#include<Embedded_Protocol_2.h>
#include<ArduinoJson.h>
#include<NewPing.h> 

Communication communication;

//Definindo variáveis de entrada (sensor)
#define TRIGGER_PIN_RIGHT A0
#define ECHO_PIN_RIGHT A1
#define TRIGGER_PIN_LEFT A5
#define ECHO_PIN_LEFT A4

#define MAX_DISTANCE 200

//Definindo pinos da ponte H
int IN[4] = {4, 5, 6, 7};

//Definindo pinos dos led's
int alert = 3;
int move = 2;

int leitura = 1;
int sensor_state = 5;
int dist_sensor_right, dist_sensor_left;

//Chama a função responsável pelo cálculo das distâncias
NewPing sonar_right(TRIGGER_PIN_RIGHT, ECHO_PIN_RIGHT, MAX_DISTANCE);
NewPing sonar_left(TRIGGER_PIN_LEFT, ECHO_PIN_LEFT, MAX_DISTANCE);

void setup() {
  
  for(int i = 0; i <= 4; i++){
    pinMode(IN[i], OUTPUT);
    digitalWrite(IN[i], LOW);
  }

  pinMode(alert, OUTPUT);
  digitalWrite(alert, LOW);
  
  pinMode(move, OUTPUT);
  digitalWrite(move, LOW);

  Serial.begin(9600);

  /* Build perception that the lihgt is on and send it to the upper layers*/ 
  communication.startBelief("init");
  communication.beliefAdd(1);
  communication.endBelief();
  communication.sendMessage();

  delay(10000);
}

void loop() {
  
  dist_sensor_right = sonar_right.ping_cm();
  dist_sensor_left = sonar_left.ping_cm();
 
//*****CASO NÃO LEIA NADA À SUA FRENTE, A DISTÂNCIA É "INFINITA"*****
  distance_correction();

  //Serial.print("Sensor 1: ");
  //Serial.println(dist_sensor_right);
  Serial.print("Sensor 2: ");
  Serial.println(dist_sensor_left);

//*******************************************************************

  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
    String s = Serial.readString();
  
    if(s.equals("infront")){ //if the agent sends "light_on", then switch the light on
      digitalWrite(alert, LOW);
      digitalWrite(move, HIGH);
      halt();
      move_front();       
    }

    if(s.equals("toleft")){ //if the agent sends "light_on", then switch the light on
      digitalWrite(alert, HIGH);
      digitalWrite(move, LOW);
      halt();
      move_back();
      delay(500);
      halt();
      move_left();
      delay(500);
      halt();     
    }

    if(s.equals("toright")){ //if the agent sends "light_on", then switch the light on
      
      digitalWrite(alert, HIGH);
      digitalWrite(move, LOW);
      halt();
      move_back();
      delay(500);
      halt();
      move_right();
      delay(500);
      halt();     
    }

    if(s.equals("toback")){ //if the agent sends "light_on", then switch the light on
      digitalWrite(alert, HIGH);
      digitalWrite(move, LOW);
      move_back();
      delay(1000);
      halt();
      move_right();
      delay(2000);
      halt();     
    }
    
    leitura = 1;
  }

  if(leitura == 1){
    //*****SE NÃO ENCONTRAR NADA A 15CM DOS SENSORES, ENTÃO SEGUE*****
    if(dist_sensor_right >= 15 && dist_sensor_left >= 15 && sensor_state != 1){
      sensor_state = 1;
      leitura = 0;
      //pode seguir em frente
      communication.startBelief("conditions");
      communication.beliefAdd(3);
      communication.endBelief();
      communication.sendMessage();
    }
    //*****SE ENCONTRAR ALGO PŔOXIMO, ENTÃO PARA E VERIFICA*****
    else if(dist_sensor_right < 15 && sensor_state != 0 || dist_sensor_left < 15 && sensor_state != 0){
      sensor_state = 0;
      leitura = 0;

      int dist_difference = dist_sensor_right - dist_sensor_left;

      if(dist_difference > 5){
        //move-se para um lado
        communication.startBelief("conditions");
        communication.beliefAdd(2);
        communication.endBelief();
        communication.sendMessage();
      }
      else if(dist_difference < -5){
        //move-se para o outro lado
        communication.startBelief("conditions");
        communication.beliefAdd(1);
        communication.endBelief();
        communication.sendMessage();
      }
      else{
        //obstáculo nos dois sensores com msm distancia
        communication.startBelief("conditions");
        communication.beliefAdd(0);
        communication.endBelief();
        communication.sendMessage();
      }
    }
  }
}

void move_front(){

  digitalWrite(IN[1], HIGH);
  digitalWrite(IN[0], LOW);
  digitalWrite(IN[2], HIGH);
  digitalWrite(IN[3], LOW);

}

void halt(){

  digitalWrite(IN[1], LOW);
  digitalWrite(IN[0], LOW);
  digitalWrite(IN[2], LOW);
  digitalWrite(IN[3], LOW);
  delay(100);

}

void move_back(){

  digitalWrite(IN[0], HIGH);
  digitalWrite(IN[1], LOW);
  digitalWrite(IN[3], HIGH);
  digitalWrite(IN[2], LOW);

}

void move_right(){

  digitalWrite(IN[1], HIGH);
  digitalWrite(IN[0], LOW);
  digitalWrite(IN[2], LOW);
  digitalWrite(IN[3], LOW);

}

void move_left(){

  digitalWrite(IN[0], LOW);
  digitalWrite(IN[1], LOW);
  digitalWrite(IN[2], HIGH);
  digitalWrite(IN[3], LOW);

}

void distance_correction(){

  if(dist_sensor_right == 0 || dist_sensor_right <= 6) dist_sensor_right = 1000;
  if(dist_sensor_left == 0 || dist_sensor_left <= 6) dist_sensor_left = 1000;

}
