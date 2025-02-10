
/* The plans below illustrate the reading of integer values and the writing to ros topics */
+value1(V) 
   <- .print("Read value 1: ", V);
      .wait(100);
      //execute "update_topic2" upon "sample_roscore". Such action is translated to a rostopic pub
      .update_value2(V+1).

+value2(V)
   <- .print("Read topic 2: ", V);
      .wait(100);
      //execute "action1" upon "sample_roscore". Such action is translated to rostopic pub
       .update_value1(V+1).
     
/* The plans below illustrate the reading of string values and the writing to ros topics */      
+current_hour(V) : .time(H,M,S) & .concat(H,":",M,":",S,Msg)
   <-.print("Read time ", V, " - ", Msg);
     .wait(2000);
     .update_time(msg).
     
      
      
