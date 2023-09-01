+init(1)
   <- .print("Iniciando...").

+conditions(3)
   <- .print("Não há objetos a frente...\nLigando motores");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","Front",[]).

+conditions(1)
   <- .print("Virando para a Direita...");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","Left",[]).

+conditions(2)
   <- .print("Virando para a Esquerda...");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","Right",[]).

+conditions(0)
   <- .print("Objeto muito próximo! Retrocedendo...");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","Back",[]).