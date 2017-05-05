package br.com.app.tcc.config;

public class Config {
	
	//Controles da rede
	public static final int CICLO = 10000;
	public static final double TAXAAPRENDIZAGEM = 0.5;
	public static final int NUMERODENEURONIOSESCONDIDA = 60;// + bias
	public static int NUMERODECASOS = 100;
	
	//Variaveis que não podem ser alteradas (apenas se adicionar novas doencas)
	public static int NUMERONEURONIOSAIDA = 3;
	public static final int[] DOENCAS = { 0, 1, 2, 3 }; // 0 = bom 1 = infarto 2 = hiper 3 = derrame
	public static final int NUMERODENEURONIOSENTRADA = 5;// + bias
}
