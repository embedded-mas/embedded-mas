/* sudo chmod a+rw /dev/ttyUSB0 */

const int LIGHT_A = 9;
const int LIGHT_B = 10;
void setup() 
{
  pinMode(LIGHT_A,OUTPUT);
  pinMode(LIGHT_B,OUTPUT);
  digitalWrite(LIGHT_A,LOW);  
  digitalWrite(LIGHT_B,LOW);  
  Serial.begin(9600);
}

void loop() 
{
  if(Serial.available() > 0)
  {  char c = Serial.read();
    if(c == 'a')
    {
      digitalWrite(LIGHT_A,HIGH);
      digitalWrite(LIGHT_B,LOW);  
      delay(2000);
    }
    else
    if(c == 'b')
    { digitalWrite(LIGHT_A,LOW);
      digitalWrite(LIGHT_B,HIGH);
      delay(2000);
    }    
  }  

    else
    {
      digitalWrite(LIGHT_A,LOW);
      digitalWrite(LIGHT_B,LOW);
    }
}
