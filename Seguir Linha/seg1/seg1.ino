#include <NewPing.h> //Biblioteca para uso das funções dos sensores ultrassônicos

int lumi_Esq = 9; //sensor linha da esquerda (vista traseira)
int lumi_Dir = 8; //sensor linha da direita (vista traseira)   

#define TRIGGER_PIN_RIGHT A0 //Sensor ultrassônico direito (vista traseira)
#define ECHO_PIN_RIGHT A1    //Sensor ultrassônico direito (vista traseira)
#define TRIGGER_PIN_LEFT A5  //Sensor ultrassônico esquerdo (vista traseira)
#define ECHO_PIN_LEFT A4     //Sensor ultrassônico esquerdo (vista traseira)
#define MAX_DISTANCE 200 //Máxima distância de leitura dos sensores (dois metros)

NewPing sonar_right(TRIGGER_PIN_RIGHT, ECHO_PIN_RIGHT, MAX_DISTANCE); //Função da biblioteca NewPing.h a qual se responsabiliza pela conversão das unidades
NewPing sonar_left(TRIGGER_PIN_LEFT, ECHO_PIN_LEFT, MAX_DISTANCE);    //Função da biblioteca NewPing.h a qual se responsabiliza pela conversão das unidades

int IN[4] = {4, 5, 6, 7}; //Definindo pinos dos motores

int lumi_esq = 0;           //Necesidade de ser uma variável global
int lumi_dir = 0;           //Necesidade de ser uma variável global
int dist_sensor_right = 0;  //Necesidade de ser uma variável global
int dist_sensor_left = 0;   //Necesidade de ser uma variável global

/* Devido ao fato de terem muitos sensores conectados ao Vcc e o arduíno possuir apenas um pino para isto
defini-se o pino 13 em nível lógico alto, apenas para distribuir as cargas */

int fase = 13;

void setup() {

  for(int i = 0; i <= 4; i++){
    pinMode(IN[i], OUTPUT);
    digitalWrite(IN[i], LOW);
  }

  pinMode(lumi_Esq, INPUT);
  pinMode(lumi_Dir, INPUT);

  pinMode(fase, OUTPUT);
  digitalWrite(fase, HIGH);

  Serial.begin(9600);

}

void loop() {
  lumi_esq = digitalRead(lumi_Esq);           //Variável de medida do sensor de luminosidade esquerdo (vista traseira)
  lumi_dir = digitalRead(lumi_Dir);           //Variável de medida do sensor de luminosidade direito (vista traseira)
  dist_sensor_right = sonar_right.ping_cm();  //Variável de medida do sensor ultrassônico direito (vista traseira)
  dist_sensor_left = sonar_left.ping_cm();    //Variável de medida do sensor ultrassônico esquerdo (vista traseira)

  if(ultrassonic_action() == 0){ //Caso os sensores ultrassônicos não estejam identificando objetos
    lumi_action();               //O comportamento do seguidor de linha atua
  }


  /* OS PRINTS A SEGUIR DEVEM SER UTILIZADOS PARA CALIBRAÇÃO DOS SENSORES DE DISTÂNCIA */

  Serial.print("dist_sensor_right = ");
  Serial.println(dist_sensor_right);
  Serial.print("dist_sensor_left = ");
  Serial.println(dist_sensor_left);
  
  delay(25);
  halt();
  delay(10);

}

void lumi_action(){

  /*************** !leitura equivale a região luminosa ***************/
  /***************  leitura equivale a região sem luz  ***************/
  if(!lumi_esq && !lumi_dir){ //Caso ambos sensores estejam identificando luminosidade
    move_front();                   //O carrinho se locomove para frente
  }
  else if(lumi_esq && !lumi_dir){ //Caso o sensor da direita identifique luminosidade
    move_left();                        //O carrinho se move para a esquerda, afim de encontrar a linha
  }
  else if(!lumi_esq && lumi_dir){ //Caso o sensor da esquerda identifique luminosidade
    move_right();                       //O carrinho se move para a direita, afim de encontrar a linha
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
  
  else{                                                //Se não há objetos à frente
    return 0;                                          //Retorna 0, liberando o comportamento do seguidor de linha
  }+

}

//ABAIXO CONSTAM APENAS AS FUNÇÕES DE SENTIDO DE GIRO E COMBINAÇÃO ENTRE OS MOTORES

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
