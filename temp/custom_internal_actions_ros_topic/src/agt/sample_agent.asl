//initial goals
!move.
!print_loggers.

+!move: .random(X) & .random(Y) & .random(Z)
   <- .move_turtle(X,Y,Z); //move the turtle with velocity linear X and Y, and with angular velocity Z
      .wait(500);
      !move.

     
-!print_loggers.   

      
//react to topic-based belief changes      
+turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV))
   <- .print("Position changed. x: ", X, "; y: ", Y, "; theta: ", T, "; linear velocity: ", LV, "; angular velocity: ", AV).  
      
     

