
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
const int ECHO_FRENTE = 8;
const int TRIG_FRENTE = 9;

const int LIGHT = 13;

#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
Communication com;

#include <Ultrasonic.h>
Ultrasonic sensorFrente(TRIG_FRENTE, ECHO_FRENTE);
Ultrasonic sensorEsq(TRIG_ESQ, ECHO_ESQ);
Ultrasonic sensorDir(TRIG_DIR, ECHO_DIR);

// Crenças
int lightState = 0;
String carState = "forward";
float distanciaFrente = 0;
float distanciaEsq = 0;
float distanciaDir = 0;


///Classe para facilitar o uso da ponte H L298N na manipulação dos motores na função Setup e Loop.
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

DCMotor MotorEsq, MotorDir;


void setup() {
  MotorEsq.Pinout(IN_1, IN_2);
  MotorDir.Pinout(IN_3, IN_4);

  MotorEsq.Velocidade(255); // A velocidade do motor pode variar de 0 a 255, onde 255 é a velocidade máxima.
  MotorDir.Velocidade(255);

  pinMode(LIGHT, OUTPUT);
  digitalWrite(LIGHT, 1); //light starts off
  Serial.begin(9600);

  delay(1000); //wait 30 seconds (to set up the multi-agent system)
}


void loop()
{
  //Acoes do arduino
  if(distanciaFrente<10){
    MotorEsq.Horario(); // Comando para o carro girar para esquerda
    MotorDir.Antihorario();
    carState = "esquerda";
  }
  if(distanciaFrente>=10){
    MotorEsq.Horario(); // Comando para o carrro ir para frente
    MotorDir.Horario();
    carState = "frente";
  }

  //Crenças----------------------------------------------------------------------------------------------------------
  distanciaFrente = sensorFrente.Ranging(CM);// ultrassom.Ranging(CM) retorna a distancia em centímetros(CM) ou polegadas(INC)
  distanciaEsq = sensorEsq.Ranging(CM);
  distanciaDir = sensorDir.Ranging(CM);


  if(distanciaFrente < 30)
  {
    carState="stop";
  }
  com.startBelief("distanceFrente");
  com.beliefAdd(distanciaFrente);
  com.endBelief();
  com.startBelief("distanciaEsq");
  com.beliefAdd(distanciaEsq);
  com.endBelief();
  com.startBelief("distanceDir");
  com.beliefAdd(distanciaDir);
  com.endBelief();

  com.startBelief("lightState");
  com.beliefAdd(lightState);
  com.endBelief();

  com.startBelief("carState");
  com.beliefAdd(carState);
  com.endBelief();

  com.sendMessage();
  //delay(10);
}
