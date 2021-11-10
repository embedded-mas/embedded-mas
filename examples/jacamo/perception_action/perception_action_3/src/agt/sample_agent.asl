started.
teste.

+started : true
<- .print("Hello World!");
iaLib.sum(1,2,A);
+result(A);
+done.

+done : result(A)
<- .print("Result of sum: ",A).

+teste : true
	<- iaLib.lightOn.
	
+light_state(0) 
   <- .print("The light A is off. I must turn it on");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","lightOn",[]);
       .
       

+light_state(1) <- .print("The light A is on. It is fine").

