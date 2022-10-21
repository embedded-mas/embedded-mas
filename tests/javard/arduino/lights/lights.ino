const int LIGHT_CONTROL = 13;

void setup() {
  Serial.begin(9600);
  pinMode(LIGHT_CONTROL, OUTPUT);
  digitalWrite(LIGHT_CONTROL, LOW);
  delay(3000);
}

void loop() {

   while (Serial.available() > 0) { //check whether there is some information from the serial (possibly from the agent)
    char c = Serial.read();
    switch (c) { 
      case 'I': // Comando para apagar o led de controle
          digitalWrite(LIGHT_CONTROL, HIGH);
        break;
      case 'O': // Comando para apagar o led de controle
          digitalWrite(LIGHT_CONTROL, LOW);
        break;   
      default:
        break;
      }
    }
}
