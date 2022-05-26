/**
 * Interface with the general purposes (possibly third-party implementations) that access microcontrollers.
 * 
 * Used for basic data exchange with external devices based on string reading and writing.
 * 
 */
package embedded.mas.bridges.jacamo;

public interface IPhysicalInterface extends IExternalInterface{
	
	/* Read a string from the microcontroller */
	public String read();
	
	/* Write a string upon the microcontroller. Returns false if it fails.*/
	public boolean write(String s);

}
