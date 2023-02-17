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

import embedded.mas.DefaultJava2Serial;


public class Run {

	public static void main(String[] args) {		
		System.out.println("testing embedded-mas libraries");
		DefaultJava2Serial d = new DefaultJava2Serial("/dev/ttyUSB0", 9600);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.print("Type <a> or <b>: ");
			try {
				String s = br.readLine();
				System.out.println("sending " + s + "...");
				d.send(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
