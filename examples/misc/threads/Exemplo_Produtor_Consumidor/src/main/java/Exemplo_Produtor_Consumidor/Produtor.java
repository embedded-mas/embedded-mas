package Exemplo_Produtor_Consumidor;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe ilustra um "produtor" em um exemplo de produtor-consumidor.
 * O produtor, nesse caso, alimenta uma lista com números inteiros em sequência.
 * 
 * 
 * 
 * @author maiquel
 *
 */


public class Produtor extends Thread {

	private List<Integer> lista;

	public Produtor(List<Integer> lista) {
		super();
		this.lista = lista;
	}


	@Override
	public void run() {
		int i = 0;
		while(true) {
			i++;
			lista.add(i);
			System.out.println("Produtor inserindo " + i);

			try {
				Thread.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}


		}
	}


}
