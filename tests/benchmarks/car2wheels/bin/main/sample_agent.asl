+sensor_state(0, Time_arduino):.time(H,M,S) //Quando não há objetos a frente, arduino envia o tempo de simulação, e o agente calcula o tempo em hora:minuto:segundo
   <- +arduino_envio(Time_arduino); //Crença que adiciona o tempo de execução que o arduino envia situação do sensor
      +agente_recebe(H*3600*1000+M*60*1000+S*1000, H,M,S); //Conversão da hora medida pelo agente em milisegundos
      .print("Não há objetos a frente... \nLigando motores...");
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","NooccupedWay",[]);
      .time(A,B,C); //Marca o tempo em que a mensagem foi enviada para comparação de execução interna do agente
      +agente_envia(A*3600*1000+B*60*1000+C*1000, A,B,C). //Conversão em milisegundos

+sensor_state(1, Time_arduino):.time(H,M,S) //Mesmo procedimento que o plano acima
   <- +arduino_envio(Time_arduino);
      +agente_recebe(H*3600*1000+M*60*1000+S*1000);
      .print("Objeto identificado! \nDesligar os motores...");
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","occupedWay",[]);
      .time(A,B,C);
      +agente_envia(A*3600*1000+B*60*1000+C*1000).

+init(1, Time_arduino):.time(H,M,S) //Define os tempos iniciais do arduino e do agente
   <- +arduino_inicial(Time_arduino); //Nova crença pra armazenar o tempo inicial do arduino
      +agente_inicial(H*3600*1000+M*60*1000+S*1000, H,M,S); //Conversão da hora:minuto:segundo para milisegundos
    .print("Iniciando...").

+acao_concluida(Value_ard) //Quando o arduino recebe um comando, então este manda seu tempo de execução ao agente
   <- +arduino_recebe(Value_ard). //Adiciona uma nova crença para atualizar o tempo do arduino

//Crença responsável pelos cálculos dos tempos - Ard - Agt - Total
+arduino_recebe(AR):arduino_envio(AE) & agente_recebe(AgtR,_,_,_) & agente_envia(AgtE,_,_,_)
   <- .print("Tempo do Arduino -> ", (AR - AE)-(AgtE - AgtR));
      .print("Tempo do Agente -> ", (AgtE - AgtR));
      .print("Tempo Total -> ", (AR - AE)).

//**************************************************************************************
//
//Conclusões:
//O tempo médio de delay entre a leitura e execução do comando é de cerca de 5 segundos
//O tempo total é baseado nos tempos em que o arduino recebe e envia uma informação (Tfinal - Tinicial)
//pois o arduino primeiro envia seu tempo de execução e quando recebe o comando do agente ele retorna o
//o novo tempo de execução, que, por sua vez é maior que o anterior.
//As funções print acrescidas de embedded.mas.bridge.... geram um delay de cerca de 2 segundos
//O tempo entre o envio do estado do sensor pelo arduino até o tempo em que ele recebe o comando do
//agente leva cerca de 3 segundos.

