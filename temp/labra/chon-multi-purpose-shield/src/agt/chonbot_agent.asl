!exec_actions.

+!exec_actions
   <- .print("Setting red led on"); .set_red_led(true); .wait(5000);
      .print("Setting red led off"); .set_red_led(false); .wait(5000);
      .print("Setting blue led on"); .set_blue_led(true); .wait(5000);
      .print("Setting blue led off"); .set_blue_led(false); .wait(5000);
      .print("Setting rgb led white"); .set_rgb_led(white); .wait(5000); 
      .print("Setting rgb led green"); .set_rgb_led(green); .wait(5000); 
      .print("Setting rgb led blue"); .set_rgb_led(blue); .wait(5000); 
      .print("Setting rgb led red"); .set_rgb_led(red); .wait(5000); 
      .print("Setting rgb led off"); .set_rgb_led("off"); .wait(5000); 
      .print("Setting buzzer on"); .buzzer(true); .wait(5000); 
      .print("Setting buzzer off"); .buzzer(false); .wait(5000); 
      .print("*** finish ***").
     

+red_led(Status)
   <- .print("The red led status is ", Status).
   
+blue_led(Status)
   <- .print("The blue led status is ", Status).
   
+rgb_led(Status)
   <- .print("The rgb led status is ", Status).      
   
+buzzer_status(Status)
   <- .print("The buzzer status is ", Status).   
   
+temperature(T)
   <- .print("The temperature is ", T).
   
+humidity(H)
   <- .print("The humidity is ", H).   

+luminosity(L)
   <- .print("The luminosity is ", L).   
   
+potentiometer(P)
   <- .print("The potentiometer level is ", P).
