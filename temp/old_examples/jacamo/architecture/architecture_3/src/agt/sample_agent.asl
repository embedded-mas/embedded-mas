!start.

+!start : true 
<- 	.time(H,M,S);
	+started(H,M,S);
	+messages(0);
	.print("hello world."," -- ",H,"-",M,"-",S);
	.
	
+milis(Milis) : messages(N) & started(Hs,Ms,Ss)
<-  +messages(N+1);
	.time(H,M,S);
	//+message(N+1,Milis,H-Hs,M-Ms,S-Ss);
	.print("mensagem: ",N+1," milessegundos: ",Milis," -- ",H-Hs,"-",M-Ms,"-",S-Ss);
.
+milis(M).

+led(Estado)<- .print("led: ",Estado).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
