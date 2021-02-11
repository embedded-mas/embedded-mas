/**
 * Interface with the general purposes (possibly third-party implementations) that access microcontrollers
 * 
 */
package embedded.mas.bridges.jacamo;

public interface IPhysicalInterface {
	
	/* Read a string from the microcontroller */
	public String read();
	
	/* Write a string upon the microcontroller. Returns false if it fails.*/
	public boolean write(String s);

}
