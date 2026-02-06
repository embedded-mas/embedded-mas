//initial goals
!move.
!print_loggers.

//testing service request without response
+!move: .random(X) & .random(Y)
   <- .move_turtle(X,Y);
      .wait(500);
      !move.


/*
//testing service request with response, which is unified with the variable Response
//this plan works only in ROS 1 because the corresponding service is only available in that version.
///to run in ROS 2, configure a different service in sample_agent.yaml.
+!print_loggers
   <- //embedded.mas.bridges.jacamo.requestResponseEmbeddedInternalAction("sample_roscore","get_loggers", [], Response);
      .do_get_loggers(Response);
      .print("Loggers: ", Response);
      .wait(1000);
      !get_loggers.       

*/
     
-!print_loggers.   

      
//react to topic-based belief changes      
+turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV))
   <- .print("Position changed. x: ", X, "; y: ", Y, "; theta: ", T, "; linear velocity: ", LV, "; angular velocity: ", AV).  
      
     

