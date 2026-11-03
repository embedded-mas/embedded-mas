!start.
!walk.

+!start : .my_name(Me)
   <- .print("Hello from ", Me).


+!walk : distance(D) & D <= 30
   <- .print("Obstacle...") ;
      .motorSpeed("default"); 
      .move("backward");
      .wait(1000);
      .move("stop");
      .wait(500);
      .motorSpeed("low");
      .move("right");
      .wait(2000);
      .motorSpeed("default");
      .move("forward");
      !walk.   
      
+!walk 
   <- .motorSpeed("default");
      .move("forward");
      .wait(distance(D)&D<=30);
      !walk.         

+distance(D)
   <- .print("Distance: ", D).

