/*
   This example has two possible behaviours: proactive and reactive.
   Uncomment the desired version and comment the other one to test the different approaches.  
*/



// ***************************************************************************************************************************************
// Reactive version - the agent reacts to perceptions about the light state.
// ***************************************************************************************************************************************
+light_state(0)
   <- .print("The light A is off. I must turn it on");
       embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","lightOn",[]).


+light_state(1) <- .print("The light A is on. It is fine").



/*

// ***************************************************************************************************************************************
// Proactive version - the agent constantly pursues the goal to keep the lights on, with different plans for different circumstances
// ***************************************************************************************************************************************

!light_on. //the agent has the goal to keep the light on

//plan to satisfy the goal light_on when the light is already on
+!light_on : light_state(1) 
   <- .print("The light A is on. It is fine");
      .wait(light_state(0));
      !light_on.

//plan to satisfy the goal light_on when the light is off
+!light_on : light_state(0) 
   <- .print("The light A is off. I must turn it on");
      embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("arduino1","lightOn",[]);
      .wait(light_state(1));
      !light_on.       


//plan to satisfy the goal light_on when the light is off
-!light_on 
    <-.wait(500);
      !light_on.
*/