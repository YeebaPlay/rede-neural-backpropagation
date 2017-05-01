package br.com.app.tcc.config;

public class Config {
	
	//Constantes
	public static final int CICLO = 10000;
	public static final double TAXAAPRENDIZAGEM = 0.5;
	public static final int NUMERODENEURONIOSENTRADA = 5;// + bias
	public static final int NUMERODENEURONIOSESCONDIDA = 60;// + bias
	public static final int[] DOENCAS = { 0, 1, 2, 3 }; // 0 = bom 1 = infarto 2 = hiper 3 = derrame
	public static int NUMERONEURONIOSAIDA = 3;
}
