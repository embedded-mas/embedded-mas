package src.main.java;

import java.util.ArrayList;
import java.util.Collection;

import embedded.mas.bridges.jacamo.JSONWatcherDevice;
import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.JSONDevice;
import embedded.mas.bridges.jacamo.IPhysicalInterface;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import embedded.mas.exception.PerceivingException;


/**
 * This device enables two actions: lightA and lightB, that are meant to turn on different lights in the physical device.
 * 
 * The actions lightA and lightB send to the microcontroller the messages "light_a" and "light_b". The microcontroller is expected to properly handle them,
 * turning on the light. A or B accordingly.
 * 
 * The names of the actions (lightA and lightB) are different from the messages sent to microcontroller ("light_a" and "light_b") to illustrate the 
 * decoupling between the agent level (the actions) and the physical level (the handling of messages). Though actions names and messages could be equal.
 *
 */

public class MyDemoDevice extends DefaultDevice {

	public MyDemoDevice(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);
	}


	
	@Override
	public Collection<Literal> getPercepts() throws PerceivingException {
		ArrayList<Literal> percepts = new ArrayList<Literal>();
		String beliefs = microcontroller.read();
		if(beliefs == null)
				return null;
		
		for (String belief : beliefs.split("/")) {
			percepts.add(Literal.parseLiteral(belief));
		}
		return percepts;
	}
	
	@Override
	public boolean execEmbeddedAction(String arg0, Object[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public IPhysicalInterface getMicrocontroller() {
		return (IPhysicalInterface) this.microcontroller;
	}


	// # atuacoes
	public boolean motorFrontLeftForward() {
		this.getMicrocontroller().write("A");
		return true;
	}
	public boolean motorFrontLeftBackward() {
		this.getMicrocontroller().write("B");
		return true;
	}
	public boolean motorFrontLeftStop(){
		this.getMicrocontroller().write("C");
		return true;
	}
	
	public boolean motorFrontRightForward() {
		this.getMicrocontroller().write("D");
		return true;
	}
	public boolean motorFrontRightBackward() {
		this.getMicrocontroller().write("E");
		return true;
	}
	public boolean motorFrontRightStop(){
		this.getMicrocontroller().write("F");
		return true;
	}
	
	public boolean motorBackLeftForward() {
		this.getMicrocontroller().write("G");
		return true;
	}
	public boolean motorBackLeftBackward() {
		this.getMicrocontroller().write("H");
		return true;
	}
	public boolean motorBackLeftStop(){
		this.getMicrocontroller().write("I");
		return true;
	}
	
	public boolean motorBackRightForward() {
		this.getMicrocontroller().write("J");
		return true;
	}
	public boolean motorBackRightBackward() {
		this.getMicrocontroller().write("K");
		return true;
	}
	public boolean motorBackRightStop(){
		this.getMicrocontroller().write("L");
		return true;
	}
}
