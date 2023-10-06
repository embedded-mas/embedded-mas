 #include <NewPing.h> //Biblioteca para uso das funções do sensor

//Definindo variáveis de entrada (sensor)
#define TRIGGER_PIN_RIGHT A0
#define ECHO_PIN_RIGHT A1
#define TRIGGER_PIN_LEFT A2
#define ECHO_PIN_LEFT A3

#define MAX_DISTANCE 200

//Definindo pinos da ponte H
int IN[4] = {4, 5, 6, 7};

//Definindo pinos dos led's
int alert = 3;
int move = 2;

//Chama a função responsável pelo cálculo das distâncias
NewPing sonar_right(TRIGGER_PIN_RIGHT, ECHO_PIN_RIGHT, MAX_DISTANCE);
NewPing sonar_left(TRIGGER_PIN_LEFT, ECHO_PIN_LEFT, MAX_DISTANCE);

void setup() {
  
  for(int i = 0; i <= 4; i++){
    pinMode(IN[i], OUTPUT);
    digitalWrite(IN[i], LOW);
  }

  pinMode(alert, OUTPUT);
  digitalWrite(alert, LOW);
  
  pinMode(move, OUTPUT);
  digitalWrite(move, LOW);

  delay(10);

}

void loop() {
  
  int dist_sensor_right = sonar_right.ping_cm();
  int dist_sensor_left = sonar_left.ping_cm();

//*****CASO NÃO LEIA NADA À SUA FRENTE, A DISTÂNCIA É "INFINITA"*****
  if(dist_sensor_right == 0){

    dist_sensor_right = 1000;

  }

    if(dist_sensor_left == 0){

    dist_sensor_left = 1000;

  }
//*******************************************************************

  if(dist_sensor_right > 15 && dist_sensor_left > 15){
    
    digitalWrite(alert, LOW);
    digitalWrite(move, HIGH);
    move_front();
    
  }

  else
  {
    
    digitalWrite(move, LOW);
    digitalWrite(alert, HIGH);
    halt();
    move_back();
    delay(1000);
    halt();

  int dist_difference = dist_sensor_right - dist_sensor_left;

  if(dist_difference > 5){

    move_right();
    delay(300);
    halt();
    delay(10);

  }

  else if(dist_difference < -5){

    move_left();
    delay(300);
    halt();
    delay(10);

  }

  else{

    move_back();
    delay(1000);
    halt();
    delay(10);
    move_right();
    delay(1000);
    halt();
    delay(10);

  }

  }

}

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
  delay(100);

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
