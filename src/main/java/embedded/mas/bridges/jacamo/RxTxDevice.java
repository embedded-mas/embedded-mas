package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;

import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;


public class RxTxDevice extends DefaultDevice implements IDevice{

	
	public RxTxDevice(Atom id, IPhysicalInterface microcontroller) {
		super(id, microcontroller);	
	}
	
	@Override
	public Collection<Literal> getPercepts() throws PerceivingException {
		ArrayList<Literal> percepts = new ArrayList<Literal>(); //adicionar os valores lidos arduino na lista percepts (dúvidas - olhar DemoDevice)
		String beliefs[] = ((IPhysicalInterface) microcontroller).read().split(",");
		percepts.add(Literal.parseLiteral(beliefs[0]));
		percepts.add(Literal.parseLiteral(beliefs[1]));
		percepts.add(Literal.parseLiteral(beliefs[2]));
		percepts.add(Literal.parseLiteral(beliefs[3]));
		percepts.add(Literal.parseLiteral(beliefs[4]));
		return percepts;
	}
	
	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPhysicalInterface getMicrocontroller() {
		return (IPhysicalInterface) this.microcontroller;
	}

}
