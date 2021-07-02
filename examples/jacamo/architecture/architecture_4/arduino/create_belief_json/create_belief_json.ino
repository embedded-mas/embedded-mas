#include<Embedded_Protocol.h>
Communication communication;

String msg = String("{\"belief_string\": \"blablabla\",\n    \"belief_integer\": 99,\n  \"belief_float\": 3.14,\n  \"belief_array\": [\"abc\", 123, 1.99]\n }");

             
                     
void setup() {
  Serial.begin(9600);
}

void loop() {
     communication.create_belief(msg);
     Serial.println();
     delay(500);
}
