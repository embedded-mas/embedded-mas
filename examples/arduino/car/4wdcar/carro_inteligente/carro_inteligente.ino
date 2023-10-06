#include <NewPing.h>
#include <AFMotor.h> //Adafruit Motor Shield


#define TRIGGER_PIN_1 A1
#define ECHO_PIN_1 A0
#define TRIGGER_PIN_2 A3
#define ECHO_PIN_2 A2
#define MAX_DISTANCE 200

NewPing sonar_1(TRIGGER_PIN_1, ECHO_PIN_1, MAX_DISTANCE);
NewPing sonar_2(TRIGGER_PIN_2, ECHO_PIN_2, MAX_DISTANCE);

  AF_DCMotor  motorFL(1);
  AF_DCMotor  motorFR(4);
  AF_DCMotor  motorBL(2);
  AF_DCMotor  motorBR(3);


void setup() {

  delay(2000);
  
  Serial.begin(9600);
  
  // VELOCIDADE DOS MOTORES
  motorFR.setSpeed(255);
  motorBR.setSpeed(255);
  motorFL.setSpeed(255);
  motorBL.setSpeed(255);
  

  // INICIAM PARADOS
  motorFR.run(RELEASE);
  motorBR.run(RELEASE);
  motorFL.run(RELEASE);
  motorBL.run(RELEASE);

  
}

void loop() {
  
  int Distance_1 = 0; //Distância do sensor ultrassônico (shield vermelha)
  int Distance_2 = 0; //Distância do sensor ultrassônico (shield amarela)
  int sonares = 0;
  int Direction = 0;

  Distance_1 = sonar_1.ping_cm();
  Distance_2 = sonar_2.ping_cm();
  sonares = sonar_1.ping_cm() + sonar_2.ping_cm();

  Serial.print("sensor 1 ");
  Serial.println(Distance_1);
  Serial.print("sensor 2 ");
  Serial.println(Distance_2);
  delay(10);

  halt();

    if(Distance_1 > 15 && Distance_2 > 15 || sonares == 0){
      
      moveForward();
      
      }

      else{
        halt();
        delay(50);
        Direction = Distance_1 - Distance_2;

        if(Direction > 0 && Distance_1 < 15 && sonares != 0){
          moveBackward();
          delay(1000);
          halt();
          //delay(50);
          moveRight();
        }
        else if(Direction < 0 && sonares != 0){
          moveBackward();
          delay(1000);
          halt();
          //delay(50);
          moveLeft();
        }
        else{
          moveBackward();
          delay(3000);
          moveRight();
        }
        delay(1000);
      }
     
}
  void moveForward() {
 
    motorBL.run(FORWARD);
    motorFL.run(FORWARD);
    motorBR.run(FORWARD);
    motorFR.run(FORWARD);
    
  }

  void moveBackward() {
    motorBL.run(BACKWARD);
    motorFL.run(BACKWARD);
    motorBR.run(BACKWARD);
    motorFR.run(BACKWARD);
  }

  void moveRight() {
    motorBL.run(RELEASE);
    motorFL.run(RELEASE);
    motorBR.run(FORWARD);
    motorFR.run(FORWARD);
  }

  void moveLeft() {
    motorBR.run(RELEASE);
    motorFR.run(RELEASE);
    motorBL.run(FORWARD);
    motorFL.run(FORWARD);
  }

  void halt() {
    motorBL.run(RELEASE);
    motorBR.run(RELEASE);
    motorFL.run(RELEASE);
    motorFR.run(RELEASE);
  }