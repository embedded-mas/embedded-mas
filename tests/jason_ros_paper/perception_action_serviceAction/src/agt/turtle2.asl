direction(south).
velocity(0.8).
energy(100).
critical(0). //to manage the log about critical situations

positions([]).
velocities([]).
energies([]).
criticals([]).
//initial goals
//!move.
//!get_loggers.

robot_position(X,Y) :- robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)).

!clean.
!keep_energy.
!log.

+!clean :  robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
           & clean_position(CX,CY)
           & CX < (X-0.12) 
   <- !move;
      !clean;
      .       

+!clean :  robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
           & clean_position(CX,CY)
           & CX >= (X-0.12)
   <- .print("turtle1_velocity: ", velocities);           
      .print("turtle1_energy: ", energies);
      .print("turtle1_position: ", positions);
      .print("turtle1_critical: ", criticals).

+!clean: not(clean_position(CX,CY)) | not(robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) )
   <- !move; 
      !clean;
      .


+!keep_energy : myEnergy(X) & X >= 50
   <- .wait(myEnergy(E) & E\==X);
      !keep_energy.

+!keep_energy : myEnergy(X) & X >= 30 & X < 50
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","start_economic_mode", [255,255,0,5,0]);
      -+velocity(0.5);
      .wait(myEnergy(E) & E\==X);
      !keep_energy;
      .              

+!keep_energy : myEnergy(X) &  X < 30
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","start_economic_mode", [255,0,0,5,0]);
      -+velocity(0)
      .wait(100);
      -+velocity(0.8);
      !keep_energy;
      .                    

+!keep_energy 
   <- .wait(1000);
      !keep_energy.

+!move: direction(north) 
        & robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y<9.5
        & velocity(V)
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_robot", [[0,V,0],[0,0,0]]);      
      .wait(1000);
      //!move.
      .

+!move: direction(north) 
        & robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>=9.5
   <- -+previous_direction(north);
      -+direction(west);
      -+latest_turn_position(X,Y);
      .send(turtle1,tell,clean_position(X,Y));//send the current position to turtle 1
      //!move.      
      .
      
+!move: direction(west) 
        & robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & latest_turn_position(LX,LY) & X > LX-0.12
        & velocity(V)
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_robot", [[V*-1,0,0],[0,0,0]]);        
      .wait(1000);
      //!move.      
      .


+!move: direction(west) 
        & robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & latest_turn_position(LX,LY) & X <= LX-0.12 & previous_direction(south)
   <- -+previous_direction(west);
      -+direction(north);
      -+latest_turn_position(X,Y);
      //!move.     
      .

+!move: direction(west) 
        & robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & latest_turn_position(LX,LY) & X <= LX-0.12 & previous_direction(north)
   <- -+previous_direction(west);
      -+direction(south);
      -+latest_turn_position(X,Y);
      //!move.  
      .

+!move: direction(south) 
        & robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>1
        & velocity(V)
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_robot", [[0,V*-1,0],[0,0,0]]);  
      .wait(1000);
      //!move. 
      .

+!move: direction(south) 
        & robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y<=1
   <- -+previous_direction(south);
      -+direction(west);
      -+latest_turn_position(X,Y);
      .send(turtle1,tell,clean_position(X,Y));//send the current position to turtle 1
      //!move.       
      .

//testing service request without response
/*+!move: .random(X) & .random(Y)
   <- //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_robot", [X,Y]);      
      .testia([X,Y]);
      .wait(500);
      !move.
*/

/*
+!move: robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y<9.5
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_robot", [[0,1,0],[0,0,0]]);      
      //.testia([X,Y]);
      .wait(500);
      !move.


+!move: robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>=9.5 & not(last_X(LX)) | last_X(LX) & X <= LX +3 
   <- -+last_X(X);
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_robot", [[1,0,0],[0,0,0]]);          
      //.testia([X,Y]);
      .wait(500);
      !move.

+!move: robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>=9.5 & last_X(LX) & X > LX +3 
   <- -last_X(LX);
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_robot", [[0,-1,0],[0,0,0]]);          
      //.testia([X,Y]);
      .wait(500);
      !move.      

*/
+!move <- .wait(500); !move. 

//testing service request with response, which is unified with the variable Response
//this plan works only in ROS 1 because the corresponding service is only available in that version.
///to run in ROS 2, configure a different service in sample_agent.yaml.
/*+!get_loggers   
   <- embedded.mas.bridges.jacamo.requestResponseEmbeddedInternalAction("sample_roscore","get_loggers", [], Response);
      .print("Loggers: ", Response);
      .wait(1000);
      !get_loggers.       

      
-!get_loggers.   

  */    
//react to topic-based belief changes      
/*+robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV))
   <- .print("Position changed. x: ", X, "; y: ", Y, "; theta: ", T, "; linear velocity: ", LV, "; angular velocity: ", AV).  
  */    
     


/* The robot perceives the critical security level*/
+security_level("critical")[source(percept)]
   <- .broadcast(tell,security_level("critical")); // it informs all the other agents about the critical security level
      -+critical(1);
      !stay_safe. //activates the goal stay safe

/* The robot is informed about the critical security level (i.e. the source is not percept) */
+security_level("critical")
   <- -+critical(1);
      !stay_safe. //activates the goal stay safe

+security_level(S)
   <- -+critical(0).


+!stay_safe : security_level("critical")
              & velocity(V)
   <- .print("Acting to stay safe");
      -+velocity(0);
      .wait(security_level(S) & S\=="critical");
      -+velocity(V).     

//remove outdated beliefs about clean positions
+clean_position(X,Y) : clean_position(CX,CY) & (CX\==X | CY\==Y)
   <- .abolish(clean_position(_,_)); //a workaround to deal with float arguments
      +clean_position(X,Y).


//--------------
+!log  : robot_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV))  & positions(L) & .concat(L,[X],NL) 
         & velocity(V) & velocities(Vs) & .concat(Vs,[V],NV)
         & myEnergy(E) & energies(Es) & .concat(Es,[E],NE)
         //& critical(C) & criticals(Cs) & .concat(Cs,[C],NC)
   <- -+positions(NL);
      -+velocities(NV);
      -+energies(NE);
      -+criticals(NC);
      .wait(1000);
      !log.

+!log 
   <- .wait(1000);
      !log.