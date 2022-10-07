#include <NewPing.h>
#include <AFMotor.h> //Adafruit Motor Shield


#define TRIGGER_PIN A1
#define ECHO_PIN A0
#define MAX_DISTANCE 200

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

  AF_DCMotor  motorBL(1);
  AF_DCMotor  motorFL(4);
  AF_DCMotor  motorBR(2);
  AF_DCMotor  motorFR(3);
  

void setup() {

  delay(2000);
  
  Serial.begin(115200);
  
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
  
  if(sonar.ping_cm() > 10 || sonar.ping_cm() == 0){
    
   moveForward();
  
  }
  
  else{

    halt(); 
    moveRight();
    delay(2000);
    halt();
    
  }
     
}
  void moveForward() {
    motorBL.run(BACKWARD);
    motorFL.run(BACKWARD);
    motorBR.run(BACKWARD);
    motorFR.run(BACKWARD);
    
  }

  void moveBackward() {
    motorBL.run(FORWARD);
    motorFL.run(FORWARD);
    motorBR.run(FORWARD);
    motorFR.run(FORWARD);
  }

  void moveRight() {
    motorBL.run(RELEASE);
    motorFL.run(RELEASE);
    motorBR.run(FORWARD);
    motorFR.run(FORWARD);
  }

  void moveLeft() {
    motorBL.run(RELEASE);
    motorFL.run(RELEASE);
    motorBR.run(FORWARD);
    motorFR.run(FORWARD);
  }

  void halt() {
    motorBL.run(RELEASE);
    motorBR.run(RELEASE);
    motorFL.run(RELEASE);
    motorFR.run(RELEASE);
  }
