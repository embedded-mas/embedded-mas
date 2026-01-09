package embedded.mas;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.IDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationDevice;
import embedded.mas.bridges.jacamo.actuation.ActuationSet;
import embedded.mas.bridges.jacamo.actuation.Actuator;
import jason.asSemantics.Unifier;
import jason.asSyntax.VarTerm;
import embedded.mas.bridges.jacamo.actuation.Actuation;

import static jason.asSyntax.ASSyntax.createAtom;

public class TestActuationSet {

	@Test
	public void testToActuationSets() {
		ActuationSet set = new ActuationSet();

		DefaultDevice d1 = new DemoDevice(createAtom("d1"));
		Actuator a1 = new Actuator(createAtom("a1"));
		Actuation act1 = new Actuation(createAtom("act1"));
		ActuationDevice ad1 = new ActuationDevice(d1, a1, act1);

		DefaultDevice d2 = new DemoDevice(createAtom("d2"));
		Actuator a2 = new Actuator(createAtom("a2"));
		Actuation act2 = new Actuation(createAtom("act2"));
		ActuationDevice ad2 = new ActuationDevice(d2, a2, act2);

		Actuator a3 = new Actuator(createAtom("a3"));
		Actuation act3 = new Actuation(createAtom("act3"));
		ActuationDevice ad3 = new ActuationDevice(d2, a3, act3);

		set.add(ad1);
		set.add(ad2);
		set.add(ad3);


		HashMap<IDevice, ArrayList<ActuationDevice>> subsets = set.toActuationSetsByDevice(); 

		assertEquals(subsets.get(d1).size(),1);
		assertEquals(subsets.get(d2).size(),2);
		assertTrue(subsets.get(d1).contains(ad1));
		assertTrue(subsets.get(d2).contains(ad2));
		assertTrue(subsets.get(d2).contains(ad3));

	}


	@Test
	public void test_ExecActuationSet() {		
		DefaultDevice d1 = new DemoDevice(createAtom("d1")); //create a device
		Actuator a1 = new Actuator(createAtom("a1")); //create an actuator - to be attached to the device

		Actuation act1 = new Actuation(createAtom("print"),new LinkedHashMap<>()); //create an actuation - to be attached to the actuator
		act1.getParameters().put(createAtom("text"), null);

		Actuation act2 = new Actuation(createAtom("double"),new LinkedHashMap<>()); //create an actuation - to be attached to the actuator
		act2.getParameters().put(createAtom("value"), null);
		act2.getParameters().put(createAtom("result"), null);

		act1.setParameterValue(createAtom("text"), "testando");
		act2.setParameterValue(createAtom("value"), 3);
		act2.setParameterValue(createAtom("result"), new VarTerm("X"));

		ActuationDevice ad1 = new ActuationDevice(d1, a1, act1);
		ActuationDevice ad2 = new ActuationDevice(d1, a1, act2);


		ArrayList<ActuationDevice> actuationSet = new ArrayList<>();
		actuationSet.add(ad1);
		actuationSet.add(ad2);

		Unifier un = new Unifier();

		// Captura original do System.out
		PrintStream originalOut = System.out;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));


		d1.execActuationSet(actuationSet,  un);


		// Restaura System.out
		System.setOut(originalOut);

		// Verifica se a saída contém o texto esperado
		String output = outputStream.toString();
		assertTrue(output.contains("[d1 - print] testando"));
		assertEquals(un.get("X").toString(), "6");


	}

	@Test
	public void test_ExecActuationSet_withDefaltParameters() {		
		DefaultDevice d1 = new DemoDevice(createAtom("d1")); //create a device
		Actuator a1 = new Actuator(createAtom("a1")); //create an actuator - to be attached to the device

		Actuation act1 = new Actuation(createAtom("print"),new LinkedHashMap<>()); //create an actuation - to be attached to the actuator
		act1.getParameters().put(createAtom("text"), null);

		Actuation act2 = new Actuation(createAtom("double"),new LinkedHashMap<>()); //create an actuation - to be attached to the actuator
		act2.getParameters().put(createAtom("value"), null);
		act2.getParameters().put(createAtom("result"), null);

		act2.setDefaultParameterValue("value", 15);

		act1.setParameterValue(createAtom("text"), "testando");

		act2.setParameterValue(createAtom("result"), new VarTerm("X"));

		ActuationDevice ad1 = new ActuationDevice(d1, a1, act1);
		ActuationDevice ad2 = new ActuationDevice(d1, a1, act2);


		ArrayList<ActuationDevice> actuationSet = new ArrayList<>();
		actuationSet.add(ad1);
		actuationSet.add(ad2);

		Unifier un = new Unifier();

		// Captura original do System.out
		PrintStream originalOut = System.out;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));


		d1.execActuationSet(actuationSet,  un);


		// Restaura System.out
		System.setOut(originalOut);

		// Verifica se a saída contém o texto esperado
		String output = outputStream.toString();
		assertTrue(output.contains("[d1 - print] testando"));
		assertEquals(un.get("X").toString(), "30");


	}

	@Test
	public void testToActuationSetsNoDouble() {
		ActuationSet set = new ActuationSet();

		//testar inclusão normal (sem duplicata) e  com duplicata

		DefaultDevice d1 = new DemoDevice(createAtom("d1"));
		Actuator a1 = new Actuator(createAtom("a1"));
		Actuation act1 = new Actuation(createAtom("act1"));
		ActuationDevice ad1 = new ActuationDevice(d1, a1, act1);

		DefaultDevice d2 = new DemoDevice(createAtom("d1"));
		Actuator a2 = new Actuator(createAtom("a1"));
		Actuation act2 = new Actuation(createAtom("act1"));
		ActuationDevice ad2 = new ActuationDevice(d2, a2, act2);


		set.add(ad1);
		set.add(ad2);

		assertEquals(set.size(), 1); //test whether the second insertion fails

		DefaultDevice d3 = new DemoDevice(createAtom("d1"));
		Actuator a3 = new Actuator(createAtom("a1"));
		Actuation act3 = new Actuation(createAtom("act2"));
		ActuationDevice ad3 = new ActuationDevice(d3, a3, act3);

		set.add(ad3);

		assertEquals(set.size(), 2); //test whether the new insertion succeeds

	}

//	/*
//	 * Mappings:
//	 *    d1.a1.act1.a1p1 -> ap1
//	 *    d2.a1.act1.a1p1 -> ap2
//	 *    d2.a2.act2.a2p1 -> ap1
//	 *    d2.a2.act2.a2p2 -> ap3
//	 */
//	@Test 
//	public void testSetParameterMapping() {
//		ActuationSet set = new ActuationSet();
//
//		DefaultDevice d1 = new DemoDevice(createAtom("d1"));
//		Actuator a1 = new Actuator(createAtom("a1"));
//		Actuation act1 = new Actuation(createAtom("act1"));
//		act1.getParameters().put(createAtom("a1p1"), null);
//		ActuationDevice ad1 = new ActuationDevice(d1, a1, act1);
//
//		DefaultDevice d2 = new DemoDevice(createAtom("d2"));
//		Actuator a2 = new Actuator(createAtom("a1"));
//		Actuation act2 = new Actuation(createAtom("act1"));
//		act2.getParameters().put(createAtom("a1p1"), null);
//		ActuationDevice ad2 = new ActuationDevice(d2, a2, act2);
//
//		Actuator a3 = new Actuator(createAtom("a2"));
//		Actuation act3 = new Actuation(createAtom("act2"));
//		act3.getParameters().put(createAtom("a2p1"), null);
//		act3.getParameters().put(createAtom("a2p2"), null);
//		ActuationDevice ad3 = new ActuationDevice(d2, a3, act3);
//
//		set.add(ad1);
//		set.add(ad2);
//		set.add(ad3);
//
//		set.setParameterMapping(d1, a1, act1, createAtom("a1p1"),createAtom("ap1"));
//		set.setParameterMapping(d2, a2, act3, createAtom("a1p1"),createAtom("ap2"));
//		set.setParameterMapping(d2, a3, act3, createAtom("a2p1"),createAtom("ap1"));
//		set.setParameterMapping(d2, a3, act3, createAtom("a2p2"),createAtom("ap3"));
//		
//		assertEquals(set.toString(), "{(d1.a1.act1(a1p1/null(ap1))),(d2.a1.act1(a1p1/null)),(d2.a2.act2(a2p1/null(ap1), a2p2/null(ap3)))}");
//
//	}



}
