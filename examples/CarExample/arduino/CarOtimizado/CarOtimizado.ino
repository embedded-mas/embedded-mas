#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças
#include <Ultrasonic.h>

//const int BUTTON_PIN = 3;//Define botao no pino digital 3
  
//pinos de controle do motor 1(IN1 e IN2)  motor 2 (IN3 e IN4)
const int IN_1 = 4;
const int IN_2 = 5;
const int IN_3 = 6;
const int IN_4 = 7;

//pinos de controle dos sencores ultrassonicos
const int TRIG_FRENTE = 8;
const int ECHO_FRENTE = 9;

const int LIGHT_ALERT = 11;
const int LIGHT_CONTROL = 12;

Communication com;
Ultrasonic sensorFrente(TRIG_FRENTE, ECHO_FRENTE);

// Crenças
int numCrenca = 0;
float distanciaFrente = 0;
int ledAlerta = 0;
int ledControle = 0;

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

  pinMode(LIGHT_ALERT, OUTPUT);
  digitalWrite(LIGHT_ALERT, LOW); //light starts off
  
  pinMode(LIGHT_CONTROL, OUTPUT);
  digitalWrite(LIGHT_CONTROL, LOW);
  
  Serial.begin(9600);

  delay(10000); //wait 2 seconds (to set up the multi-agent system)
}


void loop()
{
  //-------------------------------------------------------  ATUAÇAO  -------------------------------------------------------------
  while (Serial.available() > 0) { //check whether there is some information from the serial (possibly from the agent)
    char c = Serial.read();
    switch (c) {
      case 'Q': // Comando para o motor 1 girar para frente
          Motor1.Antihorario(); 
        break;
      case 'W': // Comando para o motor 1 girar para trás
          Motor1.Horario(); 
        break;
      case 'E':
          Motor1.Para(); // Comando para o motor 1 parar
        break;
      case 'A': // Comando para o motor 2 girar para frente
          Motor2.Antihorario(); 
        break;
      case 'S': // Comando para o motor 2 girar para trás
          Motor2.Horario(); 
        break;
      case 'D': // Comando para o motor 2 parar
          Motor2.Para(); 
        break; 
      case 'Z': // Comando para acender o led de controle
          digitalWrite(LIGHT_CONTROL, HIGH);
          ledControle = 1;
        break;
      case 'X': // Comando para apagar o led de controle
          digitalWrite(LIGHT_CONTROL, LOW);
          ledControle = 0;
        break;   
      default:
        // comando(s)
        break;
      }
    }

  //---------------------------------------------------------  PROCESSAMENTO  -------------------------------------------------------
  distanciaFrente = sensorFrente.Ranging(CM);// ultrassom.Ranging(CM) retorna a distancia em centímetros(CM) ou polegadas(INC)

  int lim = 10; //Aciona o led de alerta se a distancia do obstaculo for menor que 10.
  if(distanciaFrente<lim){
    digitalWrite(LIGHT_ALERT, HIGH);
    ledAlerta = 1;
  }else{
    digitalWrite(LIGHT_ALERT, LOW);
    ledAlerta = 0;
  }

  numCrenca++;

  //----------------------------------------------------------  ENVIO DE CRENÇAS  -----------------------------------------------------------------

  // nomes as crenças deve começar com letra minuscula
  com.startBelief("numeroDaCrenca");
  com.beliefAdd(numCrenca);
  com.endBelief();
  
  com.startBelief("distanciaFrente");
  com.beliefAdd(distanciaFrente);
  com.endBelief();

  com.startBelief("ledControle");
  com.beliefAdd(ledControle);
  com.endBelief();

  com.startBelief("ledAlerta");
  com.beliefAdd(ledAlerta);
  com.endBelief();
  
  com.sendMessage();
  
}
