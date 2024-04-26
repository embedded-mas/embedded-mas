import embedded.mas.bridges.ros.RosMaster;


import embedded.mas.bridges.ros.DefaultRos4EmbeddedMas;
import embedded.mas.bridges.ros.ServiceParameters;
import jade.util.leap.ArrayList;
import embedded.mas.bridges.ros.IRosInterface;

import jason.asSyntax.Atom;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTermImpl;
import jason.asSemantics.Unifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



public class MyRosMaster extends RosMaster {

	public MyRosMaster(Atom id, IRosInterface microcontroller) {        
		super(id, microcontroller);
        System.out.println("**** Iniciando MyRosMaster ****");
	}

        /* Translate actions into ros topic publications and service requests.
           args:
             - for topic publications: 0. Topic name
                                       1. Topic type
                                       2. Topic value
                                        
             - for service requests: service arguments are the args elements. 
                                     They must be properly handled in the execEmbeddedAction function                                           
             
           obs: args are Strings. The action arguments are send to the ros as strings.
                Type conversions are handled in the "microcontroller" (DefaultRos4EmbeddedMas)       
        */
    //  rostopic pub /path_topic mrs_msgs/Path "{'header':{'seq':0,'stamp':{'secs':11,'nsecs':22},'frame_id':"\"\""},'input_id':33,'use_heading':true,'fly_now':true,'stop_at_waypoints':false,'loop':false,'max_execution_time':5.0,'max_deviation_from_path':0.0,'dont_prepend_current_state':false,'override_constraints':false,'override_max_velocity_horizontal':0.0,'override_max_acceleration_horizontal':0.0,'override_max_jerk_horizontal':0.0,'relax_heading':false,'points':[{'position':{'x':0.0,'y':0.5,'z':5.0},'heading':0},{'position':{'x':2.0,'y':-0.5,'z':5.0},'heading':0},{'position':{'x':4.0,'y':0.5,'z':5.0},'heading':0}]}"      
	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args, Unifier un) {		
		if(actionName.equals("test_mrs_topic_action")){
            ObjectMapper mapper = new ObjectMapper();
            
            // Nodes that contain nested nodes
            ObjectNode path = mapper.createObjectNode();            
            ObjectNode header = mapper.createObjectNode();
            ObjectNode stamp = mapper.createObjectNode();
            ArrayNode points = mapper.createArrayNode();
            
            
            

            
            int seq = Integer.valueOf(((ListTermImpl)args[0]).get(0).toString());
            int secs = Integer.valueOf(((ListTermImpl) ((ListTermImpl)args[0]).get(1)).get(0).toString());
            int nsecs = Integer.valueOf(((ListTermImpl) ((ListTermImpl)args[0]).get(1)).get(1).toString());
            String frame_id = ((ListTermImpl)args[0]).get(2).toString();
            int input_id = Integer.valueOf(args[1].toString());          
            boolean use_heading = Boolean.parseBoolean(args[2].toString()); 
            boolean fly_now = Boolean.parseBoolean(args[3].toString()); 
            boolean stop_at_waypoints = Boolean.parseBoolean(args[4].toString()); 
            boolean loop = Boolean.parseBoolean(args[5].toString()); 
            float max_execution_time = Float.parseFloat(args[6].toString());
            float  max_deviation_from_path = Float.parseFloat(args[7].toString());
            boolean  dont_prepend_current_state = Boolean.parseBoolean(args[8].toString());
            boolean  override_constraints = Boolean.parseBoolean(args[9].toString());
            float  override_max_velocity_horizontal = Float.parseFloat(args[10].toString());
            float  override_max_acceleration_horizontal = Float.parseFloat(args[11].toString());
            float  override_max_jerk_horizontal = Float.parseFloat(args[12].toString());
            float  override_max_velocity_vertical = Float.parseFloat(args[13].toString());
            float  override_max_acceleration_vertical = Float.parseFloat(args[14].toString());
            float  override_max_jerk_vertical = Float.parseFloat(args[15].toString());
            boolean  relax_heading = Boolean.parseBoolean(args[16].toString());

            //handling of the array of points
            Object[] array_points = ((ListTermImpl)args[17]).toArray();
            for(int i=0;i<array_points.length;i++){
               Object[] point = ((ListTermImpl)array_points[i]).toArray();
               Object[] array_position = ((ListTermImpl)point[0]).toArray();

               System.out.println("POSITION: " +array_position[0]);
               
               //((ListTermImpl)((ListTermImpl)args[17]).get(i)).toArray(); //each point is an array of 2 positions
               float x = Float.parseFloat(array_position[0].toString());
               float y = Float.parseFloat(array_position[1].toString());
               float z = Float.parseFloat(array_position[2].toString());


               ObjectNode pointJson = mapper.createObjectNode();
               ObjectNode position = mapper.createObjectNode();   

               position.put("x", x);
               position.put("y", y);
               position.put("z", z);

               int heading = Integer.valueOf(point[1].toString());

               pointJson.put("position", position);
               pointJson.put("heading", heading);

               points.add(pointJson);

            }
            

            path.put("header",header);
            path.put("input_id",input_id);
            path.put("use_heading", use_heading);
            path.put("fly_now", fly_now);
            path.put("stop_at_waypoints", stop_at_waypoints);
            path.put("loop", loop);
            path.put("max_execution_time", max_execution_time);
            path.put("max_deviation_from_path", max_deviation_from_path);
            path.put("dont_prepend_current_state", dont_prepend_current_state);
            path.put("override_constraints", override_constraints);
            path.put("override_max_velocity_horizontal", override_max_velocity_horizontal);
            path.put("override_max_acceleration_horizontal", override_max_acceleration_horizontal);
            path.put("override_max_jerk_horizontal", override_max_jerk_horizontal);
            path.put("override_max_velocity_horizontal", override_max_velocity_vertical);
            path.put("override_max_acceleration_horizontal", override_max_acceleration_vertical);
            path.put("override_max_jerk_horizontal", override_max_jerk_vertical);            
            path.put("relax_heading", relax_heading);
            path.put("points", points);



            
            
            header.put("seq", seq);
            header.put("stamp",stamp);
            header.put("frame_id",frame_id);
            stamp.put("secs", secs);
            stamp.put("nsecs", nsecs);
            

            try {
                //System.out.println("testando topic action " + args[2]+ " - " +    args[2].getClass().getName()  + " - " + mapper.writeValueAsString(path));
                 ((DefaultRos4EmbeddedMas) microcontroller).rosWrite("/path_topic","mrs_msgs/Path",mapper.writeValueAsString(path));                   
            } catch (Exception e) {
                e.printStackTrace();
            }

           
        }
		return true;
	}
	
}
