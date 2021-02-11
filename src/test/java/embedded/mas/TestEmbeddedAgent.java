package embedded.mas;

import static org.junit.Assert.*;

import static jason.asSyntax.ASSyntax.parseLiteral;
import static jason.asSyntax.ASSyntax.parseFormula;

import org.junit.Test;

import embedded.mas.bridges.jacamo.DefaultEmbeddedAgArch;
import embedded.mas.bridges.jacamo.EmbeddedAgent;
import jason.RevisionFailedException;
import jason.asSemantics.Unifier;
import jason.asSyntax.parser.ParseException;
import jason.asSyntax.parser.TokenMgrError;

public class TestEmbeddedAgent {

	
	private EmbeddedAgent getAgent() {
		return new EmbeddedAgent() {
			
			@Override
			protected void setupSensors() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	/**
	 * Test removing all beliefs
	 */
	@Test
	public void testAbolishByFunctorAndSource_allBels() {
		EmbeddedAgent agent = getAgent();
		agent.initAg();
		
		try {
			agent.addBel(parseLiteral("mybel(x)[source(mysensor)]"));
			agent.addBel(parseLiteral("mybel(y)[source(mysensor)]"));									
			
			agent.abolishByFunctorAndSource(parseLiteral("mybel(Var)[source(mysensor)]"), agent); //del the first bel
			agent.abolishByFunctorAndSource(parseLiteral("mybel(Var)[source(mysensor)]"), agent); //del the second bel 
			
			assertFalse("agent should not believe mybel(Var)", agent.believes(parseFormula("mybel(Var)"), new Unifier()));
	
			
		} catch (RevisionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TokenMgrError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Remove the beliefs of a single source
	 */
	@Test
	public void testAbolishByFunctorAndSource_bySource() {
		EmbeddedAgent agent = getAgent();
		agent.initAg();
		
		try {
			agent.addBel(parseLiteral("mybel(x)[source(mysensor1)]"));
			agent.addBel(parseLiteral("mybel(x)[source(mysensor2)]"));									
			
			agent.abolishByFunctorAndSource(parseLiteral("mybel(Var)[source(mysensor1)]"), agent); //del the first bel
			
			
			assertFalse("agent should not believe mybel(Var)[source(mysensor1)]", agent.believes(parseFormula("mybel(Var)[source(mysensor1)]"), new Unifier()));
			assertTrue("agent should believe mybel(Var)[source(mysensor2)]", agent.believes(parseFormula("mybel(Var)[source(mysensor2)]"), new Unifier()));
	
			
		} catch (RevisionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TokenMgrError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
