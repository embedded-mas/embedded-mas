
+light_state(0)
   <- .print("The light A is off. I must turn it on");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","lightOn",[]).


+light_state(1) <- .print("The light A is on. It is fine").
