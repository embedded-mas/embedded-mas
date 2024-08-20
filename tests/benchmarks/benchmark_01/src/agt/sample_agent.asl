+light_state(0, Time_arduino):.time(H,M,S, MS) //O plano recebe o tempo em que o arduino identificou que o led apagou e 
                                           //tambem marca o horário que o agente recebeu esta informação
   <-  +arduino_envio(Time_arduino); //Crença que guarda o valor do timer do arduino
       +agente_recebe(H*3600*1000+M*60*1000+S*1000+MS); //Conversão para milisegundos
       .print("The light A is off. I must turn it on");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","lightOn",[]);
       .time(A,B,C,D); //Verifica o tempo de processamento do agente entre receber e enviar um comando
       +agente_envia(A*3600*1000+B*60*1000+C*1000+D). //Conversão para milisegundos


+light_state(1, Time_arduino) //O plano recebe o tempo do timer do arduino quando este já executou a tarefa dada
   <-  +arduino_recebe(Time_arduino); //Crença para guardar o valor do timer do arduino
       .print("The light A is on. It is fine").
      

+arduino_recebe(AR):arduino_envio(AE) & agente_recebe(AgtR) & agente_envia(AgtE) //Quando esta crença ocorre há a
                                                                                 //ligação com as demais crenças
   <- //.print("Tempo do Arduino -> ", (AR - AE)-(AgtE - AgtR));
      //.print("Tempo do Agente -> ", (AgtE - AgtR));
      //.print("Tempo Total -> ", (AR - AE));
      .concat("Tempo do Arduino;",(AR - AE)-(AgtE - AgtR), ";Tempo do Agente:;",(AgtE - AgtR), ";Tempo Total:;",(AR - AE), Texto); //montar uma string com as variáveis
      .save(teste.log, Texto).
