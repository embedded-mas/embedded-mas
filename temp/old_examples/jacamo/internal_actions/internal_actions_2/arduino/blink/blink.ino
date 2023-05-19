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
  {  String s = Serial.readString();
    if(s.equals("light_a"))
    {
      digitalWrite(LIGHT_A,HIGH);
      digitalWrite(LIGHT_B,LOW);  
      delay(1000);
    }
    else
    if(s == "light_b")
    { digitalWrite(LIGHT_A,LOW);
      digitalWrite(LIGHT_B,HIGH);
      delay(1000);
    }    
  }  

    else
    {
      digitalWrite(LIGHT_A,LOW);
      digitalWrite(LIGHT_B,LOW);
    }
    
}
