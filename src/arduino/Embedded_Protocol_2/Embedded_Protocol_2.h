/*
 *  Embedded_Protocol_2.h
 *
 *  T: biblioteca usada pelo atuador (arduino) para mandar os dados "para cima"
 */

#ifndef Embedded_Protocol_2_h
#define Embedded_Protocol_2_h

#include <ros.h>
#include <std_msgs/String.h>
#include "Arduino.h"

class Communication {


        public:

          void startBelief(String beliefName);
          void beliefAdd(String msg);
          void beliefAdd(int Int);
          void beliefAdd(float Float);
          void beliefAdd(double Double);
          void beliefAdd(unsigned long Long);
          void endBelief();
          void sendMessage();
          void publishROSMessage(ros::Publisher chatter, std_msgs::String str_msg);

          String paramStr(String s);
          int paramInt(String s, int p);

        private:
          String _beliefBuffer = "";
          String _allBeliefs = "";

          String _preamble = "==";
          String _start_message = "::";
          String _end_message = "--";

          String _t;

};

#endif
