
#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
#include <NewPing.h>
#include <AFMotor.h> //Adafruit Motor Shield
#include<NoDelay.h>


#define TRIGGER_PIN A1
#define ECHO_PIN A0
#define MAX_DISTANCE 200

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

  AF_DCMotor  motor3(1); //motor 1 - back left
  AF_DCMotor  motor1(4); //motor 4 - front left 
  AF_DCMotor  motor4(2); //motor 2 - back right
  AF_DCMotor  motor2(3); //motor 3 - front right
  

noDelay BeliefTime(500); //tempo do envio das crenças
Communication com;
float distanciaFrente;

int counter = 0;

void setup() {

  delay(2000);
  
  Serial.begin(115200);
  
  // VELOCIDADE DOS MOTORES
  motor2.setSpeed(255);
  motor4.setSpeed(255);
  motor1.setSpeed(255);
  motor3.setSpeed(255);
  

  // INICIAM PARADOS
  motor2.run(RELEASE);
  motor4.run(RELEASE);
  motor1.run(RELEASE);
  motor3.run(RELEASE);

  
}

void loop() {

  //***** actions******
  while (Serial.available() > 0) { //check whether there is some information from the serial (possibly from the agent)
    char c = Serial.read();
    switch (c) {
      //motor 1 (front left)
      case 'A': // Comando para o motor 1 girar para frente
          motor1.run(FORWARD);
        break;
      case 'B': // Comando para o motor 1 girar para trás
          motor1.run(BACKWARD);
        break;
      case 'C':
          motor1.run(RELEASE);; // Comando para o motor 1 parar
        break;


      //motor 2 (front right)
      case 'D': // Comando para o motor 1 girar para frente
          motor2.run(FORWARD);
        break;
      case 'E': // Comando para o motor 1 girar para trás
          motor2.run(BACKWARD);
        break;
      case 'F':
          motor2.run(RELEASE);; // Comando para o motor 1 parar
        break;


      //motor 3 (back left)
      case 'G': // Comando para o motor 1 girar para frente
          motor3.run(FORWARD);
        break;
      case 'H': // Comando para o motor 1 girar para trás
          motor3.run(BACKWARD);
        break;
      case 'I':
          motor3.run(RELEASE);; // Comando para o motor 1 parar
        break;   


      //motor 4 (back right)
      case 'J': // Comando para o motor 1 girar para frente
          motor4.run(FORWARD);
        break;
      case 'K': // Comando para o motor 1 girar para trás
          motor4.run(BACKWARD);
        break;
      case 'L':
          motor4.run(RELEASE);; // Comando para o motor 1 parar
        break;   

      
      /*case 'A': // Comando para o motor 1 girar para frente
          motor3.run(FORWARD);
        break;
      case 'B': // Comando para o motor 1 girar para trás
          motor3.run(BACKWARD);
        break;
      case 'C':
          motor3.run(RELEASE);; // Comando para o motor 1 parar
        break;
        
      case 'D': // Comando para o motor 1 girar para frente
          motor4.run(FORWARD);
        break;
      case 'E': // Comando para o motor 1 girar para trás
          motor4.run(BACKWARD);
        break;
      case 'F':
          motor4.run(RELEASE);; // Comando para o motor 1 parar
        break;


      case 'G': // Comando para o motor 1 girar para frente
          motor2.run(FORWARD);
        break;
      case 'H': // Comando para o motor 1 girar para trás
          motor2.run(BACKWARD);
        break;
      case 'I':
          motor2.run(RELEASE);; // Comando para o motor 1 parar
        break;
        
      */

      default:
        // comando(s)
        break;
    }
    
  }

    //**** beliefs
  if(BeliefTime.update())//Checks to see if set time has past
 {
  distanciaFrente = sonar.ping_cm();
  com.startBelief("distanciaFrente");
  com.beliefAdd(distanciaFrente);
  com.endBelief();

  com.startBelief("count");
  com.beliefAdd(counter++);
  com.endBelief();
  
  com.sendMessage();

  }

}

  
