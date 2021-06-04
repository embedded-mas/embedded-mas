package embedded.mas.bridges.RxTxSerial;

import java.util.ArrayList;
import java.util.List;

public class Protocolo {
	
	//dados que serao recebidos pela serial
	private String tipoDado;
	private int distanciaFrente;
	private int distanciaEsq;
	private int distanciaDir;
	private String movimentacao; //status da movimentacao do carro
	private String stsLed; //status do led
	
	private String leituraComando;
	private String separador = ",";
	private List<String> dados = new ArrayList<String>();
	
	public Protocolo() {
		// TODO Auto-generated constructor stub
	}

	private void interpretaComando() {
		//$STS,s1200,2,100
		String aux[]= leituraComando.split(separador);
		if(aux.length == 6) {
			dados.clear();
			tipoDado 	 = 	aux[0];
			dados.add("TipoDado("+tipoDado+")");
			
			distanciaFrente = Integer.parseInt(aux[1]);
			dados.add("distanciaFrente("+distanciaFrente+")");
			
			distanciaEsq = Integer.parseInt(aux[2]);
			dados.add("distanciaEsq("+distanciaEsq+")");
			
			distanciaDir = Integer.parseInt(aux[3]);
			dados.add("distanciaDir("+distanciaDir+")");
			
			movimentacao = aux[4]; //status da movimentacao do carro
			dados.add("movimentacao("+movimentacao+")");
			
			stsLed = aux[5]; //status do led
			dados.add("stsLed("+stsLed+")");
			
		}
	}

	public List<String> getDados() {
		return dados;
	}

	public String getTipoDado() {
		return tipoDado;
	}

	public void setTipoDado(String tipoDado) {
		this.tipoDado = tipoDado;
	}

	public int getDistanciaFrente() {
		return distanciaFrente;
	}

	public void setDistanciaFrente(int distanciaFrente) {
		this.distanciaFrente = distanciaFrente;
	}

	public int getDistanciaEsq() {
		return distanciaEsq;
	}

	public void setDistanciaEsq(int distanciaEsq) {
		this.distanciaEsq = distanciaEsq;
	}

	public int getDistanciaDir() {
		return distanciaDir;
	}

	public void setDistanciaDir(int distanciaDir) {
		this.distanciaDir = distanciaDir;
	}

	public String getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(String movimentacao) {
		this.movimentacao = movimentacao;
	}

	public String getStsLed() {
		return stsLed;
	}

	public void setStsLed(String stsLed) {
		this.stsLed = stsLed;
	}

	public String getLeituraComando() {
		return leituraComando;
	}

	public void setLeituraComando(String leituraComando) {
		this.leituraComando = leituraComando; // temos a string de dados
		this.interpretaComando(); // interpretamos a string
	}

	
}
