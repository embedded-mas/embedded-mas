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
          
	
	
	
	/*  Handling the action "move_turtle", which is realized by writing in the ROS topic /turtle1/cmd_vel
	
            The action "move_turtle" has the arguments X, Y, and Z (cf. asl file): 		
               - X is the linear velocity in the x axis;
               - Y is the linear velocity in the y axis;
               - Z is the angular velocity in the z axis.
               
            The topic cmd_vel has the following fields:
               - linear.x: linear velocity in the x axis;
               - linear.y: linear velocity in the y axis;
               - linear.z: not used;
               - angular.x: not used;
               - angular.y: not used;
               - angular.z: angular velocity in the z axis.
	
	*/
	
	if(actionName.equals("move_turtle")){ // <- move_turtle is the name of the internal action used in the .asl code		  
	
	   
	   Term linear_x = (Term)args[0]; //assign the first parameter to the field "linear.x"
	   Term linear_y = (Term)args[1]; //assign the second parameter to the variable "linear.y"
	   Term angular_z = (Term)args[2]; //assign the third parameter to the variable "angular.z"

	   
	   //build a JSON object with the fields required by the ROS topic
   	   String jsonString = "{\"linear\": {\"x\":"+linear_x.toString()+",\"y\": " + linear_y.toString() + ",\"z\": 0.0},\"angular\":{ \"x\":0.0, \"y\":0.0, \"z\":"+angular_z.toString()+"}}";

           //Requesting write in the topic
	   ((DefaultRos4EmbeddedMas) this.getMicrocontroller()).rosWrite("/turtle1/cmd_vel","geometry˙msgs/Twist",jsonString);	   
		
       }

		

       return true;
}

}
