
!start.


+!start : .my_name(Me) 
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("microcontroller1","print","Hello");
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("microcontroller2","print",Me).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
