/**
 * Este é um exemplo de Produtor-Consumidor usando threads.
 * 
 * Esta classe (App.java) é o consumidor que consome os dados contidos na lista listaPrincipal.
 * Os dados dessa lista são produzidas pela classe Produtor, que estende Thread e, quando iniciada, 
 * permanece constantemente inserindo números inteiros na lista.
 * 
 */

package Exemplo_Produtor_Consumidor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {

	public static void main(String[] args) {

		//Cria-se um arrayList sinchronized (isto é, ele bloqueia o acesso de uma thread quando outra está utilizando-o)
		//Importante: se for realizar a operação list.iterator().next(), deve-se fazer sincronização manual (não é o caso deste exemplo)
		List<Integer> listaPrincipal =  Collections.synchronizedList(new ArrayList<Integer>());  


		Produtor produtor = new Produtor(listaPrincipal);
		produtor.start(); //produtor estende uma thread, então essa linha coloca-o em execução (começa colocar elementos na lista)


		while(true) {
			if(listaPrincipal.size()>0) {
				System.out.println("Consumidor consumindo " + listaPrincipal.get(listaPrincipal.size()-1));
				synchronized (listaPrincipal) {
					listaPrincipal.remove(listaPrincipal.size()-1);
				}
			}
			try {Thread.sleep((long)(Math.random() * 1000)); } catch (InterruptedException e) { } //espera um tempo aleatório antes de continuar
		}
	}

}
