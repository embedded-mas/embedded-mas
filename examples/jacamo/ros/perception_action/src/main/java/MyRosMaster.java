/**

**/

import java.util.Collection;

import embedded.mas.bridges.jacamo.JSONWatcherDevice;
import embedded.mas.bridges.jacamo.IPhysicalInterface;
import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.LiteralDevice;
import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;
import embedded.mas.bridges.ros.RosMaster;
import embedded.mas.bridges.ros.IRosInterface; 

import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSemantics.Unifier;



public class MyRosMaster extends RosMaster {
     
       //protected IPhysicalInterface  microcontroller;

	
	public MyRosMaster(Atom id, IRosInterface  microcontroller) {
		super(id, microcontroller);
		this.microcontroller = microcontroller;
	}

        /* Translate actions into ros topic publications 
           args: 0. Topic name
                 1. Topic type
                 2. Topic value
           obs: args are Strings. The action arguments are send to the ros as strings.
                Type conversions are handled in the "microcontroller" (DefaultRos4EmbeddedMas)       
        */
	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un) {
		System.out.println("[MyRosMaster] executing " + actionName);		
		if(actionName.equals("update_value1"))		   
		   ((DefaultRos4EmbeddedMas) microcontroller).rosWrite("/value1","std_msgs/Int32",(String)args[0]);
		else
		if(actionName.equals("update_value2"))		   
		   ((DefaultRos4EmbeddedMas) microcontroller).rosWrite("/value2","std_msgs/Int32",(String)args[0]);
		else	
		if(actionName.equals("update_time"))		   
		   ((DefaultRos4EmbeddedMas) microcontroller).rosWrite("/current_time","std_msgs/String",(String)args[0]);	   	   		   
		return true;
	}
	
	
	
	
}
