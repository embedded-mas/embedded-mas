!start.
!walk.           //move forward and avoid obstacles
!alert_blink.    //blink alert lights when not moving forward

+!start : .my_name(Me)
   <- //.move("forward");
      .print("Hello from ", Me).


+!walk : distance(D) & D <= 50
   <- .print("Obstacle...") ;      
      .brakeLight(true);           //brakeLight on
      .motorSpeed("low");          //slow down
      .move("backward");           //move backward for 1 second
      .wait(1000);
      .move("stop");               //stop for 500 milliseconds
      .wait(500);
      .move("right");              //move right for 2 seconds
      .wait(2000);   
      .move(stop);
      !walk.   



+!walk 
   <- //.turnSignal("off");
      //.brakeLight(false);
      //.motorSpeed("default");
      //.wait(500);
      .move("forward");         .wait(300); //não aciona os motores, apesar de escrever no tópico 
      .turnSignal("off");       .wait(300);
      .brakeLight(false);       .wait(300);
      .motorSpeed("default");   .wait(300);
      .print("Moving forward");
      .wait(distance(D) & D <= 50);
      //.wait(2000);
      !walk;
      .
      

+!alert_blink : not motor_status("running")
   <- .turnSignal("alert");           //alert light on 
      .wait(300);
      .turnSignal("off");            //alert light off
      .wait(200);
      !alert_blink.

+!alert_blink
   <- .wait( motor_status(S)&S\=="running");
      !alert_blink.

+distance(D)
   <- .print("Distance: ", D).

