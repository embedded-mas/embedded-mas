
/* The plans below illustrate the reading of integer values and the writing to ros topics */
+value1(V)  : .time(H,M,S) & .concat(H,":",M,":",S,Msg)
   <- .print("Read value 1: ", V);
      .wait(100);
      //execute the action "update_value2". Such action is translated to a rostopic pub
      .a2(V+1, Msg).

+value2(V)
   <- .print("Read topic 2: ", V);
      .wait(100);
      //execute the action "update_value1". Such action is translated to a rostopic pub
      .a1(V+1).
     
/* The plans below illustrate the reading of string values and the writing to ros topics */      
+current_hour(V) : .time(H,M,S) & .concat(H,":",M,":",S,Msg)
   <-.print("Read time ", V, " - ", Msg).
     
    
      
      
