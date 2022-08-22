started.

+started : true
<- .print("Hello World!");
    !move.
    

+!move : distanciaFrente(0)
   <- .wait(100);
      !move.

+!move : distanciaFrente(X) & X<10
<-  //iaLib.tras;
    //.wait(100);
    //!move.
    !volta.

+!move : distanciaFrente(X) & X>10
<-  iaLib.frente;
    .wait(100);
    !move.
    
+!move 
   <- .wait(100);
      !move.

+distanciaFrente(X) <- .print("Distancia Frente: ",X).



+!volta : distanciaFrente(X) & X<10
   <-  iaLib.tras;
       .wait(100);
       !volta.
       
+!volta : distanciaFrente(X) 
   <-  iaLib.direita;
       .wait(100);
       !move.       

