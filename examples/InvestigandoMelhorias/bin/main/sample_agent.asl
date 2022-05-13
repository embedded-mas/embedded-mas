count(0).

+count(0) : true
<- .print("Hello World!").

+belief_1(X) : count(N)
   <- -+count(N+1);
      .print("Recebeu crenca. Count: ", N).