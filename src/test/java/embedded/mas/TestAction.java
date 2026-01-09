package embedded.mas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jason.asSyntax.Atom;

import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.action.Action;
import embedded.mas.bridges.jacamo.actuation.Actuation;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSequence;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;

public class TestAction {

	/* 
	 * Action: test
	 *    parameters: param1, param2, param3, param4, param5, param6
	 * 
	 * Actuation sequence:
	 *  [actuation1(p11,p12,p13),actuation1(p31,p32,p33)]
	 *  [actuation2(p21,p22,p23),actuation4(p41,p42,p43)]
	 */
	
	@Test
	public void testSetActuationParameters() {
		LinkedHashMap<Atom, Object> map1 = new LinkedHashMap<>();
		map1.put(createAtom("p11"), null); map1.put(createAtom("p12"), null); map1.put(createAtom("p13"), null);		
		Actuation act1 = new Actuation(createAtom("actuation1"), map1);
		
		LinkedHashMap<Atom, Object> map2 = new LinkedHashMap<>();
		map2.put(createAtom("p21"), null); map1.put(createAtom("p22"), null); map1.put(createAtom("p23"), null);	
		Actuation act2 = new Actuation(createAtom("actuation2"), map2);
		
		LinkedHashMap<Atom, Object> map3 = new LinkedHashMap<>();
		map3.put(createAtom("p31"), null); map1.put(createAtom("p32"), null); map1.put(createAtom("p33"), null);	
		Actuation act3 = new Actuation(createAtom("actuation3"), map3);
		
		LinkedHashMap<Atom, Object> map4 = new LinkedHashMap<>();
		map4.put(createAtom("p41"), null); map1.put(createAtom("p42"), null); map1.put(createAtom("p43"), null);	
		Actuation act4 = new Actuation(createAtom("actuation4"), map4);

		DemoDevice d1 = new DemoDevice(createAtom("myDevice1"));

		ActuationDevice a11 = new ActuationDevice(d1, new Actuator(createAtom("a11")), act1);
		ActuationDevice a12 = new ActuationDevice(d1, new Actuator(createAtom("a12")), act2);
		ActuationDevice a21 = new ActuationDevice(d1, new Actuator(createAtom("a21")), act3);
		ActuationDevice a22 = new ActuationDevice(d1, new Actuator(createAtom("a22")), act4);

		//------ actuation set
		ActuationSet aset1 = new ActuationSet();
		aset1.add(a11); aset1.add(a21);
		ActuationSet aset2 = new ActuationSet();
		aset2.add(a12); aset2.add(a22);

		ActuationSequence seq = new ActuationSequence();
		seq.addLast(aset1); seq.addLast(aset2);
		
		System.out.println(seq.toString());

		Action action = new Action(createAtom("test"));
		action.setSequence(seq);
		for(int i=1;i<=6;i++)
			action.getParams().put(createAtom("param"+i), null);


		System.out.println(action);
	}

}
