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
String carState = "parado";
float distanciaFrente = 0;
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

  delay(3000); //wait 5 seconds (to set up the multi-agent system)
}


void loop()
{
  //Açoes-----------------------------------------------------------------------------------------------------------
  while (Serial.available() > 0) { //check whether there is some information from the serial (possibly from the agent)
    char c = Serial.read();
    switch (c) {
      case 'A':
          Motor1.Horario(); // Comando para o carrro ir para frente
        break;
      case 'B':
          Motor1.Antihorario(); // Comando para o carro ir para trás
        break;
      case 'C':
          Motor1.Para(); // Comando para o carro parar
        break;
      case 'D':
          Motor2.Horario(); // Comando para o carrro ir para frente
        break;
      case 'E':
          Motor2.Antihorario(); // Comando para o carrro ir para frente
        break;
      case 'F':
          Motor2.Para(); // Comando para o carrro ir para frente
        break;    
      default:
        // comando(s)
        break;
    }
    
  }

  //Crenças----------------------------------------------------------------------------------------------------------
  distanciaFrente = sensorFrente.Ranging(CM);// ultrassom.Ranging(CM) retorna a distancia em centímetros(CM) ou polegadas(INC)
  distanciaEsq = sensorEsq.Ranging(CM);
  distanciaDir = sensorDir.Ranging(CM);

  com.startBelief("distanciaFrente");
  com.beliefAdd(distanciaFrente);
  com.endBelief();
  com.startBelief("distanciaEsquerda");
  com.beliefAdd(distanciaEsq);
  com.endBelief();
  com.startBelief("distanciaDireita");
  com.beliefAdd(distanciaDir);
  com.endBelief();

  com.startBelief("lightState");
  com.beliefAdd(lightState);
  com.endBelief();

  com.startBelief("carState");
  com.beliefAdd(carState);
  com.endBelief();

  com.sendMessage();
  delay(1000);
}
