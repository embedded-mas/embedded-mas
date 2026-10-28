!walk.

+!walk : distance(D) & D <= 30
   <- .print("Obstacle...") ;
      .motorSpeed("low"); 
      .move("backward");
      .wait(1000);
      .move("right");
      .wait(500);
      .move("forward");
      !walk.   
      
+!walk 
   <- .motorSpeed("default");
      .move("forward");
      .wait(distance(D)&D<=30);
      !walk.         

+distance(D)
   <- .print("Distance: ", D).

