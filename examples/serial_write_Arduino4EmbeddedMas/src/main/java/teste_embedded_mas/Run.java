/*
 * To run, type <gradle run -q --console=plain> to hide gradle logs in the terminal
 * 
 */

package teste_embedded_mas;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import embedded.mas.bridges.javard.Arduino4EmbeddedMas;


public class Run {

	public static void main(String[] args) {		
		System.out.println("testing embedded-mas libraries");
		Arduino4EmbeddedMas d = new Arduino4EmbeddedMas("/dev/ttyUSB0", 9600);
                d.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.print("Type <a> or <b>: ");
			try {
				String s = br.readLine();
				System.out.println("sending " + s + "...");
				d.write(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
