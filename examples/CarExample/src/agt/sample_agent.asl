started.

+started : true
<- .print("Hello World!");
    !move.

+!move : distanciaFrente(X) & X<=10
<-  iaLib.ligaLed;
	iaLib.frente;
	.wait(1000);
    !move.
    
+!move : distanciaFrente(X) & X>10
<-  iaLib.desligaLed;
	iaLib.tras;
	.wait(1000);
    !move.
    
+!move <- !move.

+distanciaFrente(X) <- .print("Distancia Frente: ",X).