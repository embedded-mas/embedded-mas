//!test.


!test1.

+!test
   <- .print("The light A is off. I must turn it on");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction2(a0,["teste1","teste2", "teste3","teste2.1","teste2.2", "teste2.3"]);
       .

+!test1
   <- .print("testing 1...");
         embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction2(a1,["teste1",X, "teste2", 55, Y]);
       .print(X, ",", Y).

+!test2
   <- .print("testing 2....");
       //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("my_device1","double",[2,X]);
       //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction2(a3,[3,X,4,Y,5,Z]);
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction2(a4,[teste,1,X]);
       print("result: ", X, ", ", Y, ",", Z);
       .
