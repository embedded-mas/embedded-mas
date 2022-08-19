// Este Device é feito para ser o mais simples possivel
//ele deve ser capaz de fazer apenas o minimo possivel no quesito ação/percepção
//suas funções incluem apenas:
	//ser capaz de atuar acendendo e apagando um led
	//ser capaz de perceber as crenças que vem do arduino

package embedded.mas.bridges.jacamo;

import java.util.ArrayList;
import java.util.Collection;

import embedded.mas.exception.EmbeddedActionException;
import embedded.mas.exception.EmbeddedActionNotFoundException;
import embedded.mas.exception.PerceivingException;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;

public class SampleDevice extends DefaultDevice implements IDevice{

	public SampleDevice(Atom id, IExternalInterface microcontroller) {
		super(id, microcontroller);
		// TODO Auto-generated constructor stub
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
	
	//Atuações
	public boolean doLightOn(){
		((IPhysicalInterface) this.microcontroller).write("Z");
		return true;
	}
	public boolean doLightOff(){
		((IPhysicalInterface) this.microcontroller).write("X");
		return true;
	}

	@Override
	public boolean execEmbeddedAction(String actionName, Object[] args)
			throws EmbeddedActionNotFoundException, EmbeddedActionException {
		// TODO Auto-generated method stub
		return false;
	}

}
