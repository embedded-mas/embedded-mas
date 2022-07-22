started.

+started : true
<- .print("Hello World!");
	+moving(foward).

+distanciaFrente(X) : X<20 & not moving(back)
<- !moveBack.
	
+!moveBack : not moving(back) & distanciaFrente(X) & X<20
<- 	-+moving(back);
	iaLib.ligaLed;
	iaLib.tras;
	.print("Fazendo o carro andar para tras").
+!moveBack. 

+distanciaFrente(X) : X>=20 & not moving(forward)
<- !moveForward.

+!moveForward : not moving(forward) & distanciaFrente(X) & X>=20
<-  -+moving(forward);
	iaLib.desligaLed;
	iaLib.frente;
	.print("Fazendo o carro andar para frente").
+!moveForward.