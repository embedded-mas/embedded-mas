/*
 *  Embedded_Protocol.cpp
 *  
 *  T: biblioteca usada pelo atuador (arduino) para mandar os dados "para cima"
 */

#include "Arduino.h"
#include "Embedded_Protocol.h"


void Communication::send_message(String message)
{
  Serial.print(_preamble + get_message_length(message) + _start_message + message + _end_message);
}

int Communication::get_message_length(String msg)
{
  return msg.length();
}
