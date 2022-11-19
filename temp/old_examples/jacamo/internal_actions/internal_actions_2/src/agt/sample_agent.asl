
!start.


+!start  
   <- !!print_message; //act upon device 1
      !!lights_on. //act upon device 2.
      
      
+!lights_on
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("microcontroller2","lightA",[]);
      .wait(2000);
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("microcontroller2","lightB",[]);
      .wait(2000);
      !lights_on.    
      
+!print_message    
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("microcontroller1","print",["testing embedded action"]);
      .wait(5000);
      !print_message. 

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
