#include<NewPing.h>
#include<AFMotor.h>

int lumi_Esq = 6; //sensor linha da esquerda (vista traseira)
int lumi_Dir = 18; //sensor linha da direita (vista traseira)   

#define TRIGGER_PIN_RIGHT A1
#define ECHO_PIN_RIGHT A0
#define TRIGGER_PIN_LEFT A3
#define ECHO_PIN_LEFT A5
#define MAX_DISTANCE 200

NewPing sonar_right(TRIGGER_PIN_RIGHT, ECHO_PIN_RIGHT, MAX_DISTANCE); //Função da biblioteca NewPing.h a qual se responsabiliza pela conversão das unidades
NewPing sonar_left(TRIGGER_PIN_LEFT, ECHO_PIN_LEFT, MAX_DISTANCE);    //Função da biblioteca NewPing.h a qual se responsabiliza pela conversão das unidades

//DEFININDO VARIÁVEIS MOTORES
AF_DCMotor motorFL(1);
AF_DCMotor motorFR(4);
AF_DCMotor motorBL(2);
AF_DCMotor motorBR(3);
int lumi_esq = 0;           //Necesidade de ser uma variável global
int lumi_dir = 0;           //Necesidade de ser uma variável global
int dist_sensor_right = 0;  //Necesidade de ser uma variável global
int dist_sensor_left = 0;   //Necesidade de ser uma variável global

/* Devido ao fato de terem muitos sensores conectados ao Vcc e o arduíno possuir apenas um pino para isto
defini-se o pino 13 em nível lógico alto, apenas para distribuir as cargas */

void setup() {

  //VELOCIDADE DOS MOTORES -> AJUSTÁVEL CASO NÃO ESTEJAM SINCRONIZADOS (255 = Vmáx)
  motorFR.setSpeed(255);
  motorBR.setSpeed(255);
  motorFL.setSpeed(255);
  motorBL.setSpeed(255);

  halt(); //OS MOTORES INICIAM PARADOS

  pinMode(lumi_Esq, INPUT);
  pinMode(lumi_Dir, INPUT);

  Serial.begin(9600);

}

void loop() {
  lumi_esq = digitalRead(lumi_Esq);           //Variável de medida do sensor de luminosidade esquerdo (vista traseira)
  lumi_dir = digitalRead(lumi_Dir);           //Variável de medida do sensor de luminosidade direito (vista traseira)
  dist_sensor_right = sonar_right.ping_cm();  //Variável de medida do sensor ultrassônico direito (vista traseira)
  dist_sensor_left = sonar_left.ping_cm();    //Variável de medida do sensor ultrassônico esquerdo (vista traseira)

  Serial.print("esquerda: ");
  Serial.println(lumi_esq);
  Serial.print("direita: ");
  Serial.println(lumi_dir);

  //if(ultrassonic_action() == 0){ //Caso os sensores ultrassônicos não estejam identificando objetos
    lumi_action();               //O comportamento do seguidor de linha atua
  //}

  /* OS PRINTS A SEGUIR DEVEM SER UTILIZADOS PARA CALIBRAÇÃO DOS SENSORES DE DISTÂNCIA */

  /*Serial.print("dist_sensor_right = ");
  Serial.println(dist_sensor_right);
  Serial.print("dist_sensor_left = ");
  Serial.println(dist_sensor_left);*/

  //delay(30);
  //halt();
  //delay(10);

}

void lumi_action(){

  /*************** !leitura equivale a região luminosa ***************/
  /***************  leitura equivale a região sem luz  ***************/
  if(!lumi_esq && !lumi_dir){ //Caso ambos sensores estejam identificando luminosidade
    moveForward();                   //O carrinho se locomove para frente
  }
  else if(lumi_esq && !lumi_dir){ //Caso o sensor da direita identifique luminosidade
    moveLeft();                        //O carrinho se move para a esquerda, afim de encontrar a linha
  }
  else if(!lumi_esq && lumi_dir){ //Caso o sensor da esquerda identifique luminosidade
    moveRight();                       //O carrinho se move para a direita, afim de encontrar a linha
  }
  else{      //Caso ambos sensores estejam identificando uma região ofuscada
    halt();  //O carrinho para
  }

}

int ultrassonic_action(){

//*****CASO NÃO LEIA NADA NO RAIO DA DISTÂNCIA MÁXIMA, A DISTÂNCIA É "INFINITA"*****
  if(dist_sensor_right == 0){
    dist_sensor_right = 1000;
  }
  if(dist_sensor_left == 0){
    dist_sensor_left = 1000;
  }
//*******************************************************************

  if(dist_sensor_right < 15 || dist_sensor_left < 15){ //Se há algum objeto à frente
    halt();                                            //Para os motores
    return 1;                                          //Retorna 1, ou seja, os sensores atuaram
  }
  else{                                                //Se não há objetos à frente
    return 0;                                          //Retorna 0, liberando o comportamento do seguidor de linha
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