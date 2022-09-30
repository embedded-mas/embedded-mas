/**
 * Interface with the general purposes (possibly third-party implementations) that access external devices.
 * 
 * Extending interfaces must specify the a method to read data from external devices.
 * 
 */

package embedded.mas.bridges.jacamo;

public abstract interface IExternalInterface {
	
	/* Read a string from the microcontroller */
	public <T> T read();
	
	public void execEmbeddedAction(EmbeddedAction action);

}
