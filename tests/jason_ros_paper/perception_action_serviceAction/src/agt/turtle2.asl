direction(south).

//initial goals
//!move.
//!get_loggers.

!clean.

+!clean :  turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
           & clean_position(CX,CY)
           & CX < (X-0.12) 
   <- !move;
      !clean;
      .       

+!clean :  turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
           & clean_position(CX,CY)
           & CX >= (X-0.12).

+!clean: not(clean_position(CX,CY)) | not(turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) )
   <- !move; 
      !clean;
      .




+!move: direction(north) 
        & turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y<9.5
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [[0,1,0],[0,0,0]]);      
      .wait(1000);
      //!move.
      .

+!move: direction(north) 
        & turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>=9.5
   <- -+previous_direction(north);
      -+direction(west);
      -+latest_turn_position(X,Y);
      .send(turtle1,tell,clean_position(X,Y));//send the current position to turtle 1
      //!move.      
      .
      
+!move: direction(west) 
        & turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & latest_turn_position(LX,LY) & X > LX-0.12
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [[-1,0,0],[0,0,0]]);        
      .wait(1000);
      //!move.      
      .


+!move: direction(west) 
        & turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & latest_turn_position(LX,LY) & X <= LX-0.12 & previous_direction(south)
   <- -+previous_direction(west);
      -+direction(north);
      -+latest_turn_position(X,Y);
      //!move.     
      .

+!move: direction(west) 
        & turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & latest_turn_position(LX,LY) & X <= LX-0.12 & previous_direction(north)
   <- -+previous_direction(west);
      -+direction(south);
      -+latest_turn_position(X,Y);
      //!move.  
      .

+!move: direction(south) 
        & turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>1
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [[0,-1,0],[0,0,0]]);  
      .wait(1000);
      //!move. 
      .

+!move: direction(south) 
        & turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y<=1
   <- -+previous_direction(south);
      -+direction(west);
      -+latest_turn_position(X,Y);
      .send(turtle1,tell,clean_position(X,Y));//send the current position to turtle 1
      //!move.       
      .

//testing service request without response
/*+!move: .random(X) & .random(Y)
   <- //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [X,Y]);      
      .testia([X,Y]);
      .wait(500);
      !move.
*/

/*
+!move: turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y<9.5
   <- embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [[0,1,0],[0,0,0]]);      
      //.testia([X,Y]);
      .wait(500);
      !move.


+!move: turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>=9.5 & not(last_X(LX)) | last_X(LX) & X <= LX +3 
   <- -+last_X(X);
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [[1,0,0],[0,0,0]]);          
      //.testia([X,Y]);
      .wait(500);
      !move.

+!move: turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV)) 
        & Y>=9.5 & last_X(LX) & X > LX +3 
   <- -last_X(LX);
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","move_turtle", [[0,-1,0],[0,0,0]]);          
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
/*+turtle_position(x(X),y(Y),theta(T),linear_velocity(LV),angular_velocity(AV))
   <- .print("Position changed. x: ", X, "; y: ", Y, "; theta: ", T, "; linear velocity: ", LV, "; angular velocity: ", AV).  
  */    
     

//remove outdated beliefs about clean positions
+clean_position(X,Y) : clean_position(CX,CY) & (CX\==X | CY\==Y)
   <- .abolish(clean_position(_,_)); //a workaround to deal with float arguments
      +clean_position(X,Y).