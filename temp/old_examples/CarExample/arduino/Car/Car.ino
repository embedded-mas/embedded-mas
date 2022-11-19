#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
#include <Ultrasonic.h>
#include<NoDelay.h>

//pinos de controle do motor 1(IN1 e IN2)  motor 2 (IN3 e IN4)
const int IN_1 = 4;
const int IN_2 = 5;
const int IN_3 = 6;
const int IN_4 = 7;

//pinos de controle dos sencores ultrassonicos
const int ECHO_DIR = 2;
const int TRIG_DIR = 3;
const int ECHO_ESQ = 10;
const int TRIG_ESQ = 11;
const int TRIG_FRENTE = 8;
const int ECHO_FRENTE = 9;

const int LIGHT = 13;



Communication com;

Ultrasonic sensorFrente(TRIG_FRENTE, ECHO_FRENTE);
//Ultrasonic sensorEsq(TRIG_ESQ, ECHO_ESQ);
//Ultrasonic sensorDir(TRIG_DIR, ECHO_DIR);

noDelay BeliefTime(500); //tempo do envio das crenças

// Crenças
int lightState = 0;
float distanciaFrente = 0;
float old_distanciaFrente = 0;
float distanciaEsq = 0;
float distanciaDir = 0;


//Classe para facilitar o uso da ponte H L298N na manipulação dos motores na função Setup e Loop.
class DCMotor {
    int spd = 255, pin1, pin2;
  public:
    void Pinout(int in1, int in2) { // Pinout é o método para a declaração dos pinos que vão controlar o objeto motor
      pin1 = in1;
      pin2 = in2;
      pinMode(pin1, OUTPUT);
      pinMode(pin2, OUTPUT);
    }
    void Velocidade(int in1) { // Speed é o método que irá ser responsável por salvar a velocidade de atuação do motor
      spd = in1;
    }
    void Horario() { //faz o motor girar para frente
      analogWrite(pin1, spd);
      digitalWrite(pin2, LOW);
    }
    void Antihorario() { //faz o motor girar para trás
      digitalWrite(pin1, LOW);
      analogWrite(pin2, spd);
    }
    void Para() { //faz o motor ficar parado.
      digitalWrite(pin1, LOW);
      digitalWrite(pin2, LOW);
    }
};

DCMotor Motor1, Motor2;



void setup() {
  Motor1.Pinout(IN_1, IN_2);
  Motor2.Pinout(IN_3, IN_4);

  Motor1.Velocidade(255); // A velocidade do motor pode variar de 0 a 255, onde 255 é a velocidade máxima.
  Motor2.Velocidade(255);

  pinMode(LIGHT, OUTPUT);
  digitalWrite(LIGHT, 1); //light starts off
  Serial.begin(9600);

  delay(3000); //wait 3 seconds (to set up the multi-agent system)
}


void loop()
{
  //Açoes-----------------------------------------------------------------------------------------------------------
  while (Serial.available() > 0) { //check whether there is some information from the serial (possibly from the agent)
    char c = Serial.read();
    switch (c) {
      case 'A': // Comando para o motor 1 girar para frente
          Motor1.Antihorario(); 
        break;
      case 'B': // Comando para o motor 1 girar para trás
          Motor1.Horario(); 
        break;
      case 'C':
          Motor1.Para(); // Comando para o motor 1 parar
        break;
      case 'D': // Comando para o motor 2 girar para frente
          Motor2.Antihorario(); 
        break;
      case 'E': // Comando para o motor 2 girar para trás
          Motor2.Horario(); 
        break;
      case 'F': // Comando para o motor 2 parar
          Motor2.Para(); 
        break; 
      case 'G': // Comando para acender o led de controle
          digitalWrite(LIGHT, HIGH);
        break;
      case 'H': // Comando para apagar o led de controle
          digitalWrite(LIGHT, LOW);
        break;   
      default:
        // comando(s)
        break;
    }
    
  }

  //Crenças----------------------------------------------------------------------------------------------------------
  distanciaFrente = sensorFrente.Ranging(CM);// ultrassom.Ranging(CM) retorna a distancia em centímetros(CM) ou polegadas(INC)


 if(BeliefTime.update())//Checks to see if set time has past
 {
  if(old_distanciaFrente != distanciaFrente){

  com.startBelief("distanciaFrente");
  com.beliefAdd(distanciaFrente);
  com.endBelief();
  old_distanciaFrente = distanciaFrente;
  com.sendMessage();

  }
  

 // com.startBelief("lightState");
 // com.beliefAdd(lightState);
 // com.endBelief();
 }
  //---------------------------------------------------------------------------------------------------------------------------

  //teste
  if(distanciaFrente<10){
    digitalWrite(LIGHT, HIGH);
  }else if(distanciaFrente>7){
    digitalWrite(LIGHT, LOW);
  }
  
  //delay(1000);
}
