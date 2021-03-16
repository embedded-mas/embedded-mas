current_light(a). //the light that is currently turned on (either a or b)

!start.


+!start : true <-
   //embedded.mas.bridges.jacamo.default_serial_internal_action("/dev/ttyUSB0", 9600, a);
   //embedded.mas.bridges.jacamo.default_serial_internal_action("/dev/ttyUSB0", 9600, b);
   //.print("hello world.").
   !manage_lights.
   
   
/* Swich from light a to b. */   
+!manage_lights : current_light(a)   
   <- -+ current_light(b);
      embedded.mas.bridges.jacamo.default_serial_internal_action("/dev/ttyUSB0", 9600, b);
      .wait(1000);
      !manage_lights.

/* Swich from light b to a. */
+!manage_lights : current_light(b)   
   <- -+ current_light(a);
      embedded.mas.bridges.jacamo.default_serial_internal_action("/dev/ttyUSB0", 9600, a);
      .wait(1000);
      !manage_lights.      

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
