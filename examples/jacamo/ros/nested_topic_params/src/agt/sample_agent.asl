//initial goals
!move.

//test service request without response
+!move: .random(X) & .random(Y) & .random(Z)
   <- //the action move_turtle, triggered below, is defined in sample_agent.yaml
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [[X,Y,Z],[X*2,Y*2,Z*2]] );      
      .wait(500);
      !move.

  
//react to topic-based belief changes      
+turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV))
   <- .print("Position changed. x: ", X, "; y: ", Y, "; theta: ", T, "; linear velocity: ", LV, "; angular velocity: ", AV).  
      
     

