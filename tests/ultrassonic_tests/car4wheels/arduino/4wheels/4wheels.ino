#include<Embedded_Protocol_2.h>
#include<ArduinoJson.h>
#include<NewPing.h>
#include<AFMotor.h>

Communication communication;

//DEFININDO VARIÁVEIS DO SENSOR
#define TRIGGER_PIN_1 A1
#define ECHO_PIN_1 A0
#define TRIGGER_PIN_2 A3
#define ECHO_PIN_2 A5
#define MAX_DISTANCE 200

int leitura = 1;
int sensor_state = 5;
int dist_sensor_1, dist_sensor_2;

NewPing sonar_1(TRIGGER_PIN_1, ECHO_PIN_1, MAX_DISTANCE);
NewPing sonar_2(TRIGGER_PIN_2, ECHO_PIN_2, MAX_DISTANCE);

//DEFININDO VARIÁVEIS MOTORES
AF_DCMotor motorFL(1);
AF_DCMotor motorFR(4);
AF_DCMotor motorBL(2);
AF_DCMotor motorBR(3);

void setup() 
{

  //VELOCIDADE DOS MOTORES -> AJUSTÁVEL CASO NÃO ESTEJAM SINCRONIZADOS (255 = Vmáx)
  motorFR.setSpeed(255);
  motorBR.setSpeed(255);
  motorFL.setSpeed(255);
  motorBL.setSpeed(255);

  halt(); //OS MOTORES INICIAM PARADOS

  Serial.begin(9600);

  /* Build perception that the lihgt is on and send it to the upper layers*/ 
  communication.startBelief("init");
  communication.beliefAdd(1);
  communication.endBelief();
  communication.sendMessage();
  
  delay(10000); //wait 10 seconds (to set up the multi-agent system)
}

void loop() 
{
  dist_sensor_1 = sonar_1.ping_cm();
  dist_sensor_2 = sonar_2.ping_cm();

  distance_correction();
  Serial.print("Sensor 1:");
  Serial.println(dist_sensor_1);
  //Serial.print("Sensor 2:");
  //Serial.println(dist_sensor_2);

  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
    String s = Serial.readString();
  
    if(s.equals("infront")){ //if the agent sends "light_on", then switch the light on
      halt();
      moveForward();         
    }

    if(s.equals("toleft")){ //if the agent sends "light_on", then switch the light on
      
      halt();
      moveBackward();
      delay(500);
      halt();

      halt();
      moveLeft();
      delay(500);
      halt();     
    }

    if(s.equals("toright")){ //if the agent sends "light_on", then switch the light on
      
      halt();
      moveBackward();
      delay(500);
      halt();

      halt();
      moveRight();
      delay(500);
      halt();     
    }

    if(s.equals("toback")){ //if the agent sends "light_on", then switch the light on
      
      moveBackward();
      delay(1000);
      halt();
      moveRight();
      delay(2000);
      halt();     
    }
    
    leitura = 1;
  }

  if(leitura == 1){
    //*****SE NÃO ENCONTRAR NADA A 15CM DOS SENSORES, ENTÃO SEGUE*****
    if(dist_sensor_1 >= 10 && dist_sensor_2 >= 10 && sensor_state != 1){
      sensor_state = 1;
      leitura = 0;
      //pode seguir em frente
      communication.startBelief("conditions");
      communication.beliefAdd(3);
      communication.endBelief();
      communication.sendMessage();
    }
    //*****SE ENCONTRAR ALGO PŔOXIMO, ENTÃO PARA E VERIFICA*****
    else if(dist_sensor_1 < 10 && sensor_state != 0 || dist_sensor_2 < 10 && sensor_state != 0){
      sensor_state = 0;
      leitura = 0;

      int dist_difference = dist_sensor_1 - dist_sensor_2;

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

void moveForward(){

  motorBL.run(FORWARD);
  motorFL.run(FORWARD);
  motorBR.run(FORWARD);
  motorFR.run(FORWARD);

}

void moveBackward(){

  motorBL.run(BACKWARD);
  motorFL.run(BACKWARD);
  motorBR.run(BACKWARD);
  motorFR.run(BACKWARD);

}

void moveRight(){

  motorBL.run(RELEASE);
  motorFL.run(RELEASE);
  motorBR.run(FORWARD);
  motorFR.run(FORWARD);

}

void moveLeft(){

  motorBR.run(RELEASE);
  motorFR.run(RELEASE);
  motorBL.run(FORWARD);
  motorFL.run(FORWARD);

}

void halt(){

  motorBR.run(RELEASE);
  motorFR.run(RELEASE);
  motorBL.run(RELEASE);
  motorFL.run(RELEASE);

}

void distance_correction(){

  if(dist_sensor_1 == 0) dist_sensor_1 = 1000;
  if(dist_sensor_2 == 0) dist_sensor_2 = 1000;

}