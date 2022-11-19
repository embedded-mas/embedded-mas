
!start.


+!start : true <- .print("hello world.").

//********** Crenças provenientes de create_belief_with_lib_ArduinoJson.ino *********/
+value(Numero) <- .print("valor: ", Numero).

//********** Crenças provenientes de teste_crencas_por_sensores.ino *********/
+botao(Numero) <- .print("botao: ", Numero).
+led(Estado)<- .print("led: ",Estado).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
