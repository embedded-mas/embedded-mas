String msg = String("{\"belief_string\": \"blablabla\",\n    \"belief_integer\": 99,\n  \"belief_float\": 3.14,\n  \"belief_array\": [\"abc\", 123, 1.99]\n }");
//String msg = String("{\"teste\":\"ON\"}");
             
                     
void setup() {
  Serial.begin(9600);
}

void loop() {
     Serial.print("==");
     Serial.print(msg.length());
     Serial.print("::");
     Serial.print(msg);
     Serial.println("--");
     delay(500);
}
