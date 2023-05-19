//initial goals
!move.
!get_loggers.

//test service request without response
+!move: .random(X) & .random(Y)
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [X,Y]);      
      .wait(500);
      !move.

//test service request with response, which is unified with the variable Response
+!get_loggers   
   <- embedded.mas.bridges.jacamo.requestResponseEmbeddedInternalAction("sample_roscore","get_loggers", [], Response);
      .print("Loggers: ", Response);
      .wait(1000);
      !get_loggers.       
      
//react to topic-based belief changes      
+turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV))
   <- .print("Position changed. x: ", X, "; y: ", Y, "; theta: ", T, "; linear velocity: ", LV, "; angular velocity: ", AV).  
      
     


