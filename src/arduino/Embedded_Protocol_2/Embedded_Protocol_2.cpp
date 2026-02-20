/*
 *  Embedded_Protocol_2.cpp
 *
 *  T: biblioteca usada pelo atuador (arduino) para mandar os dados "para cima"
 */

#include "Arduino.h"
#include "Embedded_Protocol_2.h"
#include <ros.h>
#include <std_msgs/String.h>
#include <string.h>

void Communication::startBelief(String beliefName){
    if(_allBeliefs.length() != 0){    // se não for o primeiro elemento adiciona uma virgula.
      _allBeliefs.concat(",");
    }

    _beliefBuffer = "";
    _beliefBuffer.concat("\"");
    _beliefBuffer.concat(beliefName);
    _beliefBuffer.concat("\"");
    _beliefBuffer.concat(":[");
}

void Communication::endBelief(){
    _beliefBuffer.setCharAt(_beliefBuffer.length()-1,']');
    _allBeliefs.concat(_beliefBuffer);
    _beliefBuffer = "";
}

void Communication::beliefAdd(String msg){
    _beliefBuffer.concat("\"");
    _beliefBuffer.concat(msg);
    _beliefBuffer.concat("\"");
    _beliefBuffer.concat(',');
}

void Communication::beliefAdd(int Int){
    _beliefBuffer.concat(String(Int));
    _beliefBuffer.concat(',');
}

void Communication::beliefAdd(float Float){
    _beliefBuffer.concat(String(Float, 3));
    _beliefBuffer.concat(',');
}

void Communication::beliefAdd(double Double){ // adiciona um parametro do tipo double a crença.
    _beliefBuffer.concat(String(Double, 5));
    _beliefBuffer.concat(',');
}

void Communication::beliefAdd(unsigned long Long){
    _beliefBuffer.concat(String(Long));
    _beliefBuffer.concat(',');
}    

void Communication::sendMessage(){
    Serial.print(_preamble);
    Serial.print(_allBeliefs.length()+2);
    Serial.print(_start_message);
    Serial.print('{');
    Serial.print(_allBeliefs);
    Serial.print('}');
    Serial.println(_end_message);
    _allBeliefs = "";
}

void Communication::publishROSMessage(ros::Publisher chatter, std_msgs::String str_msg){

    int lenBeliefs = _allBeliefs.length()+2;
    char lenBeliefsStr[5];

    sprintf(lenBeliefsStr, "%d", lenBeliefs);

    String msg = _preamble + lenBeliefsStr + _start_message + '{' + _allBeliefs + '}' + _end_message;

    _allBeliefs = "";

    int n = msg.length();
    char char_array[n + 1];
    strcpy(char_array, msg.c_str());

    str_msg.data = char_array;
    chatter.publish( &str_msg );

}


// Returns the i-th parameter inside parentheses as a String.
// Example: "cmd(10,abc,3.14)" → paramStrAt(s, 1) = "abc"
String Communication::paramStr(String s, int i) {
    int n = s.length();
    int openIdx = -1, closeIdx = -1;

    // Find the first '(' and the first ')'
    for (int j = 0; j < n; j++) {
        if (s[j] == '(' && openIdx == -1) {
            openIdx = j;
        } else if (s[j] == ')') {
            closeIdx = j;
            break;
        }
    }

    // If parentheses are missing or malformed, return empty string
    if (openIdx == -1 || closeIdx == -1 || closeIdx <= openIdx) {
        return "";
    }

    // Extract the substring inside the parentheses
    String inside = s.substring(openIdx + 1, closeIdx);

    // Split parameters by commas and return the i-th one
    int start = 0;
    int count = 0;
    for (int j = 0; j <= inside.length(); j++) {
        if (inside[j] == ',' || j == inside.length()) {
            if (count == i) {
                String param = inside.substring(start, j);
                param.trim();  // remove surrounding spaces
                return param;
            }
            count++;
            start = j + 1;
        }
    }

    // If the requested index does not exist
    return "";
}


int Communication::paramInt(String s, int p=0){
    int var[10]={1};
    int p1=0, p2, d, v=0;
    char a='(', b=')', c = ',';
    int n = s.length();
    int p3[n-3];

    for (int i=0; i<=n; i++)
    {
      if(s[i]==c)
      {
        p3[v] = i;
        v++;
      }
    }

    for (int i=0; i<=n; i++){
      if(s[i]==a){
        p1 = i;
      }
      if(s[i]==b){
        p2 = i;
      }
    }

    if(p1!=0){
      char tC[10];
      
      
      
    if (v == 0) {
       // só um valor entre parênteses
       int d = p2 - p1 - 1;
       for (int k = 0; k < d; k++) {
          tC[k] = s[p1 + 1 + k];
       }   
       tC[d] = '\0';
       var[0] = atoi(tC);
    } else {
      for (int j=0; j<=v; j++){
        if(j==0){
          int d, p1aux;
            d = p3[j]-p1;
            p1aux = p1;
            for (int k=0; k<=d; k++){
              tC[k] = s[p1aux+1];
              p1aux++;
            }
            var[j] = atoi(tC);
        }
        if(j!=0 && j!=v){
          int d, p1aux;
            d = p3[j]-p3[j-1];
            p1aux = p3[j-1];
            for (int k=0; k<=d; k++){
              tC[k] = s[p1aux+1];
              p1aux++;
            }
            var[j] = atoi(tC);
        }
        if(j==v){
          int d, p1aux;
            d = p2-p3[j-1];
            p1aux = p3[j-1];
            for (int k=0; k<=d; k++){
              tC[k] = s[p1aux+1];
              p1aux++;
            }
            var[j] = atoi(tC);
        }
      }
      }
    }
    else{
      var[0] = 1;
    }
    return var[p];
}


// Returns the i-th parameter as a float.
// If conversion fails or parameter is missing, returns 0.0.
float Communication::paramFloat(String s, int i) {
    // Reuse the string extraction function
    String param = paramStr(s, i);

    // If parameter is found, convert to float
    if (param.length() > 0) {
        return param.toFloat();
    }

    // Default return if not found
    return 0.0;
}



/* Returns the string before "(".
   e.g. "test" returns "test
        "test(1,2,3)" returns "test"

 */
String Communication::actuationName(String input) {
  int index = input.indexOf('(');  // search the 1st '('
  if (index == -1) {
    //  '(' not found, returns the input
    return input;
  } else {
    // returns the substring before '('
    return input.substring(0, index);
  }
}
