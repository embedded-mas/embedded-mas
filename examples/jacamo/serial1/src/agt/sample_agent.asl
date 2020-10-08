// Agent sample_agent in project example1

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <-
   embedded.mas.bridges.jacamo.default_serial_internal_action("/dev/ttyUSB0", 9600, "a");
   embedded.mas.bridges.jacamo.default_serial_internal_action("/dev/ttyUSB0", 9600, "b");
   .print("hello world.").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
