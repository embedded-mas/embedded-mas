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

String Communication::paramStr(String s){
    int n = s.length();
    int p1;
    char a='(';

    for (int i=0; i<=n; i++){
      if(s[i]==a){
        p1 = i;
      }
    }
    if(p1!=0){
      _t = s.substring(0,p1);
    }
    else{
      _t = s;
    }
    return _t;
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
    else{
      var[0] = 1;
    }
    return var[p];
}
