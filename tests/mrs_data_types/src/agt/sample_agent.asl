!test.

+!test
   <- 
   
   // the action test_mrs_topic_action_full, executed below, considers all the parameters of the ros topic
   embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","test_mrs_topic_action_full",[[0,[11,22],""],33,true,true,false,false,5.0,0.0,false,false,0.0,0.0,0.0,0.0,0.0,0.0,false,[[[0,0.5,5],0],[[2,-0.5,5],0],[[4,0.5,55],0]]] );
   
   // the action test_mrs_topic_action_light, executed below, considers a subset of the parameters of the ros topic. In this case, these parameters are coordinates
   //embedded.mas.bridges.jacamo.defaultEmbeddedInternalAction("sample_roscore","test_mrs_topic_action_light",[[0,0.5,5],[2,-0.5,5],[4,0.5,5],[9,8.5,7]] );
   .wait(1000);
   !test.
