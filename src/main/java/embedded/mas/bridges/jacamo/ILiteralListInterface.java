/**
 * Interface with the general purposes (possibly third-party implementations) that access microcontrollers.
 * 
 * Used for basic data reading from external devices that return list of literals. 
 * 
 */

package embedded.mas.bridges.jacamo;

import java.util.List;

import jason.asSyntax.Literal;

public interface ILiteralListInterface extends IExternalInterface {
	
	public List<Literal> read();

}
