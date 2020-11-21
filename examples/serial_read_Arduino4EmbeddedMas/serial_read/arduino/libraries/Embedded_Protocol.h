/*
 *  Embedded_Protocol.h
 *  
 *  T: biblioteca usada pelo atuador (arduino) para mandar os dados "para cima"
 */

#ifndef Embedded_Protocol_h    
#define Embedded_Protocol_h 

#include "Arduino.h"

class Communication {

        public:
          void send_message(String message);
          
          
        private:
          int get_message_length(String msg);
  
          String _preamble = "==";
          String _start_message = "::";
          String _end_message = "--";
};

#endif
