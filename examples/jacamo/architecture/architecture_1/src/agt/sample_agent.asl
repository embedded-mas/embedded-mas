
!start.


+!start : true <- .print("hello world.").

+current_second(S) <- .print("Current second: ", S).

+current_minute(M) <- .print("Current minute: ", M).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
