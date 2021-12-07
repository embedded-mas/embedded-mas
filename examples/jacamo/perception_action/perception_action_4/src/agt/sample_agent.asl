started.

+started : true
<- .print("Hello World!");
iaLib.sum(1,2,A);
+result(A);
+done.

+done : result(A)
<- .print("Result of sum: ",A).
       
+light_state(1) <- .print("The light A is on. It is fine").

+distance(X) : X<100
<- .print("The distance is: ",X);
   iaLib.lightOn.

+distance(X) : X>=100
<- .print("The distance is: ",X);
   iaLib.lightOff.