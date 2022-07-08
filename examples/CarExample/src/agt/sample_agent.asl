started.

+started : true
<- .print("Hello World!");
    !move.

+!move : distanciaFrente(X) & X<10
<-  iaLib.ligaLed;
	iaLib.frente;
    !move.
+!move : distanciaFrente(X) & X>10
<-  iaLib.desligaLed;
	iaLib.tras;
    !move.
+!move <- !move.

+distanciaFrente(X) <- .print("Distancia Frente: ",X).