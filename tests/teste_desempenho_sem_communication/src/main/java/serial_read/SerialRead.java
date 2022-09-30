/*
   Este programa conta quanto tempo é necessário para ligar uma lâmpada N vezes (onde N é uma variávei definida no início do programa).
   O resultado é impresso ao final.
*/

package serial_read;

import arduino.Arduino;

import com.fazecast.jSerialComm.*;

public class SerialRead {

    public static void main(String[] args) {
    
        int N = 10;
        
        Arduino a = new Arduino("/dev/ttyUSB0", 9600);      
        a.openConnection();
        
        String s = "";	
        int contador = 0;
        long start = 0;
        while(contador<10){ //leitura constante
           while(!s.equals("==19::{\"light_state\":[0]}--\r")){ //lê constantemente a serial até encontrar o indicativo de luz apagada
              s = a.serialRead(); //read from serial until getting a linebreak (\n)
	      System.out.println("leu " + s );                            
           }           
           s = "";
           contador++;
           System.out.println("ligando...("+contador+")");  
           a.serialWrite("light_on"); //envia o comando para ligar a luz
           if(contador==1) start = System.currentTimeMillis(); //registra o horário da primeira vez que ligou a luz           
	} 
	long finish = System.currentTimeMillis(); //registra o horário em que terminou a execução
        long timeElapsed = finish - start; //calcula a diferença entre o horario de início e de fim
	System.out.println("tempo decorrido: " + timeElapsed);  
    }
}

