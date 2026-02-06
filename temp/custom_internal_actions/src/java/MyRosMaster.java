import embedded.mas.bridges.ros.IRosInterface;
import embedded.mas.bridges.ros.RosMaster;
import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;

import jason.asSyntax.Atom;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.asSemantics.Unifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MyRosMaster extends RosMaster{

    public MyRosMaster(Atom id, IRosInterface microcontroller) {
        super(id, microcontroller);
    }
    

    @Override
   public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un) {		
	//execute the actions configured in the yaml file
        super.execEmbeddedAction(actionName, args, un);  // <- do not delete this line - it executes the actions configured in the yaml file

	//*** Handle customized actions after this point ****
          
	
	
	
	// Handling the action "move_turtle", which is realized by requesting the ROS service /turtle1/teleport_relative 		
	
	if(actionName.equals("move_turtle")){ // <- move_turtle is the name of the internal action used in the .asl code		  
	   Term linear = (Term)args[0]; //assign the first parameter to the variable "linear"
	   Term angular = (Term)args[1]; //assign the second parameter to the variable "angular"
	   
	   //build a JSON object with the parameters required by the ROS service
   	   String jsonString = "{\"linear\":"+linear.toString()+",\"angular\":"+angular.toString()+"}";
   	   ObjectMapper mapper = new ObjectMapper();
   	   JsonNode jsonNode = null;
           try {
              // Convert a String into JsonNode
	      jsonNode = mapper.readTree(jsonString);
	   }catch(Exception e) {
	     return false;
	   }

           //Requesting the service
	   ((DefaultRos4EmbeddedMas) this.getMicrocontroller()).serviceRequest("/turtle1/teleport_relative",jsonNode);
		
       }

		

       return true;
}

}
