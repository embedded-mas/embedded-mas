package embedded.mas;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;
import embedded.mas.bridges.jacamo.actuation.ros.TopicWritingActuation;
import embedded.mas.bridges.ros.RosMaster;
import embedded.mas.bridges.ros.ServiceParam;
import embedded.mas.bridges.ros.ServiceParameters;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.asSyntax.Atom;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestActuationSequence {

	@Test
	public void testaddFirst() {

		DemoDevice d1 = new DemoDevice(createAtom("myDevice1"));		

		ActuationDevice a11 = new ActuationDevice(d1, new Actuator(createAtom("a11")), new Actuation(createAtom("actuation11")));
		ActuationDevice a12 = new ActuationDevice(d1, new Actuator(createAtom("a12")), new Actuation(createAtom("actuation12")));

		ActuationDevice a21 = new ActuationDevice(d1, new Actuator(createAtom("a21")), new Actuation(createAtom("actuation21")));
		ActuationDevice a22 = new ActuationDevice(d1, new Actuator(createAtom("a22")), new Actuation(createAtom("actuation22")));

		ActuationSet aset1 = new ActuationSet();
		aset1.add(a11);
		aset1.add(a21);

		ActuationSet aset2 = new ActuationSet();
		aset2.add(a12);
		aset2.add(a22);



		ActuationSequence aseq = new ActuationSequence();
		assertTrue(aseq.size()==0);

		aseq.addLast(aset1);
		assertTrue(aseq.size()==1);
		assertTrue(aseq.get(aseq.size()-1).size()==2);
		assertTrue(aseq.get(aseq.size()-1).contains(a11));
		assertTrue(aseq.get(aseq.size()-1).contains(a21));

		aseq.addLast(aset2);
		assertTrue(aseq.size()==2);
		assertTrue(aseq.get(aseq.size()-1).contains(a12));
		assertTrue(aseq.get(aseq.size()-1).contains(a22));



	}

	@Test
	public void test_setParameters() {
		LinkedHashMap<Atom, Object> mapParams1 = new LinkedHashMap<>();
		mapParams1.put(createAtom("p11"), null);
		mapParams1.put(createAtom("p12"), null);
		mapParams1.put(createAtom("p13"), null);
		Actuation act1 = new Actuation(createAtom("actuation1"), mapParams1);
		
		LinkedHashMap<Atom, Object> mapParams2 = new LinkedHashMap<>();
		mapParams2.put(createAtom("p21"), null);
		mapParams2.put(createAtom("p22"), null);
		mapParams2.put(createAtom("p23"), null);
		Actuation act2 = new Actuation(createAtom("actuation2"), mapParams2);


		DemoDevice d1 = new DemoDevice(createAtom("myDevice1"));

		ActuationDevice a11 = new ActuationDevice(d1, new Actuator(createAtom("a11")), act1);
		ActuationDevice a12 = new ActuationDevice(d1, new Actuator(createAtom("a12")), act2);

		//ROS Topic Writing actuation

		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", null); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", null); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);

		TopicWritingActuation topicActuation1 = new TopicWritingActuation(createAtom("topicActuation1"), "topicName", "topicType", serviceParameters);
		TopicWritingActuation topicActuation2 = new TopicWritingActuation(createAtom("topicActuation2"), "topicName", "topicType", serviceParameters.clone());

		RosMaster d2 = new RosMaster(createAtom("myDevice2"), null);
		ActuationDevice a21 = new ActuationDevice(d2, new Actuator(createAtom("a21")), topicActuation1);
		ActuationDevice a22 = new ActuationDevice(d2, new Actuator(createAtom("a22")), topicActuation2);


		//------ actuation set
		ActuationSet aset1 = new ActuationSet();
		aset1.add(a11); aset1.add(a21);
		ActuationSet aset2 = new ActuationSet();
		aset2.add(a12); aset2.add(a22);

		ActuationSequence seq = new ActuationSequence();
		seq.addLast(aset1); seq.addLast(aset2);


		Term[] t = new Term[26];
		for (int i = 0; i < 26; i++) {
			t[i] = new NumberTermImpl(i + 1);
		}

		seq.setParameters(t);



		assertEquals(seq.get(0).get(0).getActuation().getParametersAsArray()[0].toString(),"1");
		assertEquals(seq.get(0).get(0).getActuation().getParametersAsArray()[1].toString(),"2");
		assertEquals(seq.get(0).get(0).getActuation().getParametersAsArray()[2].toString(),"3");

		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[0].toString(),"4");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[1].toString(),"5");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[2].toString(),"6");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[3].toString(),"7");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[4].toString(),"8");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[5].toString(),"9");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[6].toString(),"10");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[7].toString(),"11");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[8].toString(),"12");
		assertEquals(seq.get(0).get(1).getActuation().getParametersAsArray()[9].toString(),"13");

		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[0].toString(),"14");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[1].toString(),"15");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[2].toString(),"16");

		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[0].toString(),"17");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[1].toString(),"18");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[2].toString(),"19");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[3].toString(),"20");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[4].toString(),"21");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[5].toString(),"22");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[6].toString(),"23");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[7].toString(),"24");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[8].toString(),"25");
		assertEquals(seq.get(1).get(1).getActuation().getParametersAsArray()[9].toString(),"26");


	}


	@Test
	public void test_setParametersWithDefaultValues() {
		LinkedHashMap<Atom, Object> mapParams = new LinkedHashMap<>();
		mapParams.put(createAtom("p11"), null);
		mapParams.put(createAtom("p12"), null);
		mapParams.put(createAtom("p13"), null);
		Actuation act1 = new Actuation(createAtom("actuation1"), mapParams);

		act1.setDefaultParameterValue("p12", Integer.valueOf(12));

		DemoDevice d1 = new DemoDevice(createAtom("myDevice1"));

		ActuationDevice a11 = new ActuationDevice(d1, new Actuator(createAtom("a11")), act1);

		//ROS Topic Writing actuation

		//build the parameters of the topic actuation
		ServiceParameters spa = new ServiceParameters();
		ServiceParam spa1 = new ServiceParam("a1", null); spa.add(spa1);
		ServiceParam spa2 = new ServiceParam("a2", null); spa.add(spa2);

		ServiceParameters spw = new ServiceParameters();
		ServiceParam spw1 = new ServiceParam("w1", null); spw.add(spw1);
		ServiceParam spw2 = new ServiceParam("w2", null); spw.add(spw2);
		ServiceParam spw3 = new ServiceParam("w3", spa); spw.add(spw3);

		ServiceParameters splinear = new ServiceParameters();
		ServiceParam splinear1 = new ServiceParam("x", null); splinear.add(splinear1); 
		ServiceParam splinear2 = new ServiceParam("y", null); splinear.add(splinear2); 
		ServiceParam splinear3 = new ServiceParam("z", null); splinear.add(splinear3);
		ServiceParam splinear4 = new ServiceParam("w", spw); splinear.add(splinear4);

		ServiceParameters spangular = new ServiceParameters();
		ServiceParam spangular1 = new ServiceParam("x", null); spangular.add(spangular1); 
		ServiceParam spangular2 = new ServiceParam("y", null); spangular.add(spangular2); 
		ServiceParam spangular3 = new ServiceParam("z", null); spangular.add(spangular3);

		ServiceParameters serviceParameters = new ServiceParameters();
		ServiceParam ssplinear = new ServiceParam("linear", splinear); serviceParameters.add(ssplinear);
		ServiceParam sspangular = new ServiceParam("angular", spangular); serviceParameters.add(sspangular);

		TopicWritingActuation topicActuation1 = new TopicWritingActuation(createAtom("topicActuation1"), "topicName", "topicType", serviceParameters);


		//build the hashmap which represents the default parameters

		HashMap<String, Object> linear = new HashMap<>();
		linear.put("y", 0.3);

		HashMap<String, Object> defaultParams = new HashMap<>();
		defaultParams.put("linear", linear);

		topicActuation1.setDefaultParameterValues(defaultParams);

		RosMaster d2 = new RosMaster(createAtom("myDevice2"), null);
		ActuationDevice a21 = new ActuationDevice(d2, new Actuator(createAtom("a21")), topicActuation1);


		//------ actuation set
		ActuationSet aset1 = new ActuationSet();
		aset1.add(a11);
		ActuationSet aset2 = new ActuationSet();
		aset2.add(a21); 

		ActuationSequence seq = new ActuationSequence();
		seq.addLast(aset1); seq.addLast(aset2);

		Term[] t = new Term[11];
		for (int i = 0; i < 11; i++) {
			t[i] = new NumberTermImpl(i + 1);
		}

		seq.setParameters(t);

		assertEquals(seq.get(0).get(0).getActuation().getParametersAsArray()[0].toString(),"1");
		assertEquals(seq.get(0).get(0).getActuation().getParametersAsArray()[1].toString(),"12");
		assertEquals(seq.get(0).get(0).getActuation().getParametersAsArray()[2].toString(),"2");

		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[0].toString(),"3");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[1].toString(),"0.3");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[2].toString(),"4");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[3].toString(),"5");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[4].toString(),"6");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[5].toString(),"7");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[6].toString(),"8");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[7].toString(),"9");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[8].toString(),"10");
		assertEquals(seq.get(1).get(0).getActuation().getParametersAsArray()[9].toString(),"11");
	}

}
