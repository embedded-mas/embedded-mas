/*
 *  Embedded_Protocol_2.cpp
 *  
 *  T: biblioteca usada pelo atuador (arduino) para mandar os dados "para cima"
 */

#include "Arduino.h"
#include "Embedded_Protocol_2.h"


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

