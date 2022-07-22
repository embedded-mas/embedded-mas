package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;

import embedded.mas.bridges.jacamo.DefaultDevice;
import embedded.mas.bridges.jacamo.IDevice;
import embedded.mas.bridges.jacamo.IPhysicalInterface;
import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class CarDevice extends DefaultDevice implements IDevice{
	
	public CarDevice(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);
	}

	@Override
	public boolean execEmbeddedAction(String arg0, Object[] arg1) {
		// TODO Auto-generated method stub
		return false;
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
	
	// # atuacoes
		public boolean doMotor1Antihorario() {
			((IPhysicalInterface) this.microcontroller).write("Q");
			return true;
		}
		public boolean doMotor1Horario() {
			((IPhysicalInterface) this.microcontroller).write("W");
			return true;
		}
		public boolean doMotor1Para(){
			((IPhysicalInterface) this.microcontroller).write("E");
			return true;
		}
		public boolean doMotor2Antihorario() {
			((IPhysicalInterface) this.microcontroller).write("A");
			return true;
		}
		public boolean doMotor2Horario() {
			((IPhysicalInterface) this.microcontroller).write("S");
			return true;
		}
		public boolean doMotor2Para(){
			((IPhysicalInterface) this.microcontroller).write("D");
			return true;
		}
		
		//TODO implementar essas atuaçoes em alguma ação do agente
		public boolean doLightOn(){
			((IPhysicalInterface) this.microcontroller).write("Z");
			return true;
		}
		public boolean doLightOff(){
			((IPhysicalInterface) this.microcontroller).write("X");
			return true;
		}

}
