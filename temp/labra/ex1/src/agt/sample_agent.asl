//export TURTLEBOT3_MODEL=burger &&\
//roslaunch turtlebot3_gazebo turtlebot3_world.launch

max_actuations(10000). //set a value X>0 to finish the application after X actuations
actuations(0).

!walk.

// Obstáculo à frente: limite em 1.2 metros
+!walk : obstacle_front(F) & F < 1.2 
   <- .print("Obstacle front") ;
      .move([[-1,0,0],[0,0,0.0]]);
      ?actuations(A);
      -+actuations(A+1);
      .wait(1000); // AJUSTADO: 1 segundo de espera
      .move([[0,0,0],[0,0,-1]]); //turn right
      ?actuations(A2);
      -+actuations(A2+1);
      .wait(1000); // AJUSTADO: 1 segundo de espera
      !walk.   

// Obstáculo à esquerda (< 1.2 metros)
+!walk : obstacle_left(L) & L < 1.2 &
         (not obstacle_right(_) | obstacle_right(R) & R > L)
   <- .print("Obstacle left") ;
      .move([[0,0,0],[0,0,-1]]);
      ?actuations(A);
      -+actuations(A+1);
      .wait(1000); // AJUSTADO: 1 segundo de espera
      !walk.

// Obstáculo à direita (< 1.2 metros)
+!walk : obstacle_right(R) & R < 1.2 &
         (not obstacle_left(_) | obstacle_left(L) & L > R)
   <- .print("Obstacle right") ;
      .move([[0,0,0],[0,0,1]]);
      ?actuations(A);
      -+actuations(A+1);
      .wait(1000); // AJUSTADO: 1 segundo de espera
      !walk.

// Livre de obstáculos
+!walk 
   <- .print("no obstacle") ;
      .move([[1.0,0,0],[0,0,0.0]]);
      ?actuations(A);
      -+actuations(A+1);
      .wait(1000); // AJUSTADO: 1 segundo de espera
      !walk.      

//-------------------------------------------------------------   
// REGRAS DE GIRO (TURN) CONFORME OS LIMITES ATUALIZADOS
//-------------------------------------------------------------   

//case 1: obstaculo à frente, direita e à esquerda - vai para trás
+!turn : obstacle_front(F) & F < 1.2 &  
         obstacle_right(R) & R < 1.2 &     
         obstacle_left(L)  & L < 1.2   
   <- .print("Obstacle left and right.", R, ", ", L);
      .move([[-1,0,0],[0,0,0]]);
      .wait(1000); // AJUSTADO: 1 segundo de espera
      !turn.    
      
//case 2: obstaculo à frente e à direita - vai para a esquerda
+!turn : obstacle_front(F) & F < 1.2 &  
         obstacle_right(R) & R < 1.2              
   <- .print("Obstacle right.", R);
      .move([[0,0,0],[0,0,1]]);
      .wait(1000); // AJUSTADO: 1 segundo de espera
      !turn. 

//case 3: obstáculo à frente, sem obstáculo à direita - gira para a direita
+!turn : obstacle_front(F) & F < 1.2 
 <- .print("No obstacle right");
    .move([[0,0,0],[0,0,-1]]);
    .wait(1000); // AJUSTADO: 1 segundo de espera
    !turn. 

//case 4: sem obstáculos - atingiu o objetivo turn
+!turn.

//-------------------------------------------------------------    

+actuations(A) : max_actuations(M) & M>-1 & A>M 
   <- .move([[0,0,0],[0,0,0.0]]);
      .print("Finishing system after ", A, " actuations."); 
      .stopMAS.

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }