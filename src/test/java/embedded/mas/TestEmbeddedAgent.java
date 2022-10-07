package embedded.mas;

import static org.junit.Assert.*;

import static jason.asSyntax.ASSyntax.parseLiteral;
import static jason.asSyntax.ASSyntax.parseFormula;
import static jason.asSyntax.ASSyntax.createAtom;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DefaultEmbeddedAgArch;
import embedded.mas.bridges.jacamo.DemoDevice;
import embedded.mas.bridges.jacamo.EmbeddedAgent;
import jason.RevisionFailedException;
import jason.asSemantics.Unifier;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;

public class TestEmbeddedAgent {

	
	private EmbeddedAgent getAgent() {
		return new EmbeddedAgent() {
			
			@Override
			protected void setupDevices() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	

	@Test
	public void testGetSetDevice() {
		EmbeddedAgent agent = getAgent();
		agent.initAg();
		
		assertTrue(agent.getDevices().size()==0); //ensure that there is no device at the beginning
		
		//test deprecated methods (addSensor and removeSensor)
		DemoDevice d = new DemoDevice(createAtom("device1"));
		agent.addSensor(d);
		assertTrue(agent.getDevices().size()==1);
		assertTrue(agent.getDevices().get(0).getId().toString().equals("device1"));
		agent.removeSensor(d);
		assertTrue(agent.getDevices().size()==0);
		
		agent.addDevice(d);
		assertTrue(agent.getDevices().size()==1);
		assertTrue(agent.getDevices().get(0).getId().toString().equals("device1"));
		agent.removeDevice(d);
		assertTrue(agent.getDevices().size()==0);
	}

}
