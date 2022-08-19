started.

+started : true
<- .print("Hello World!");
	iaLib.ligaLed.

+botao(X) : X==1
<-  .print("Ligando led...");
	iaLib.ligaLed;
	.print("Led ligado!").

+botao(X) : X==0
<- .print("Desligando led...");
	iaLib.desligaLed;
	.print("Led desligado!").