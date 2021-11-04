
+light_state_a(0) 
   <- .print("The light A is off. I must turn it on");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","lightA",[]).       
+light_state_a(1) <- .print("The light A is on. It is fine").


+light_state_b(0) 
   <- .print("The light B is off. I must turn it on");
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","lightB",[]).   
      
+light_state_b(1) <- .print("The light B is on. It is fine").

