/* sudo chmod a+rw /dev/ttyUSB0 */

const int LIGHT = 13;

int delay_time;
int light_state = 1;

void setup() 
{
  pinMode(LIGHT,OUTPUT);
  digitalWrite(LIGHT,1);  //light starts on
     
  Serial.begin(9600);
  /* Build perception that the lihgt is on and send it to the upper layers */
  String msg = String("{\"light_state\":1 }");
  Serial.print("==");
  Serial.print(msg.length());
  Serial.print("::");
  Serial.print(msg);
  Serial.println("--");
  delay(10000); //wait 30 seconds (to set up the multi-agent system)
}

void loop() 
{
  while(Serial.available() > 0){ //check whether there is some information from the serial (possibly from the agent)
     String s = Serial.readString();
     if(s.equals("light_on")){ //if the agent sends "light_on", then switch the light on
        digitalWrite(LIGHT,1); 
        light_state = 1; 
        /* Build perception that the lihgt is on and send it to the upper layers */
        String msg = String("{\"light_state\": 1}");
        Serial.print("==");
        Serial.print(msg.length());
        Serial.print("::");
        Serial.print(msg);
        Serial.println("--");
     } 
   }  
  
  
  
  if(light_state==1){ 
    /* wait a random time and switch the light off if it is on */
     delay_time = random(2,10)*1000;
     delay(delay_time);
     digitalWrite(LIGHT,0);
     light_state = 0;
     /* Build perception that the lihgt is on and send it to the upper layers */
     String msg = String("{\"light_state\": 0}");
     Serial.print("==");
     Serial.print(msg.length());
     Serial.print("::");
     Serial.print(msg);
     Serial.println("--");
  }
  
  
 
    
}
