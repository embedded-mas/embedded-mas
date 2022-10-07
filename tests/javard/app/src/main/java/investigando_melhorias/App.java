/*
 * This program turns a led on/off using the commands "I" and "O"
 */
package investigando_melhorias;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import embedded.mas.bridges.javard.Arduino4EmbeddedMas;

public class App {

	public static void main(String[] args) {
		Arduino4EmbeddedMas a = new Arduino4EmbeddedMas("/dev/ttyUSB0", 9600);  //serial port: possibly must be changed in different systems.    
		a.openConnection();
		String s = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(!s.equals("X")){
		   System.out.print("Type I (turn on), O (turn off), or X (exit): ");
		try {
			s = br.readLine();
			System.out.println("sending " + s + "...");
			a.serialWrite(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
}