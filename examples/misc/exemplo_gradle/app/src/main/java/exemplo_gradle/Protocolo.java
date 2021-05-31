package exemplo_gradle;

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
	
	public Protocolo() {
		// TODO Auto-generated constructor stub
	}

	private void interpretaComando() {
		//$STS,s1200,2,100
		String aux[] = leituraComando.split(separador);
		if(aux.length == 6) {
			tipoDado 	 = 	aux[0];
			distanciaFrente = Integer.parseInt(aux[1]);
			distanciaEsq = Integer.parseInt(aux[2]);
			distanciaDir = Integer.parseInt(aux[3]);
			movimentacao = aux[4]; //status da movimentacao do carro
			stsLed = aux[5]; //status do led
		}
		System.out.print("Dados: ");
		System.out.print("TipoDado("+tipoDado+"), ");
		System.out.print("distanciaFrente("+distanciaFrente+"), ");
		System.out.print("distanciaEsq("+distanciaEsq+"), ");
		System.out.print("distanciaDir("+distanciaDir+"), ");
		System.out.print("movimentacao("+movimentacao+"), ");
		System.out.print("stsLed("+stsLed+"), ");
		System.out.println();
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
