package br.com.app.iqoption.model;

import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Neuronio {
	
	public Neuronio (){
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private double[] valorSaida; //Neuronio da saída
	private double[] sPeso; //Peso da saida
	public double[] deltaPesoSaida; //Variacao dos pesos de saida
	
	private double[] valorEntrada; //Entrada da rede
	private double[] ePeso; //Peso da entrada
	public double[] deltaPesoEscondida; //Variacao da camada escondida

	private double[] valorEsperado; //Valor esperado da rede
	private int doenca; //qual doenca o neuronio esta treinando
	
	//Variaveis auxiliares
	private double[] pesos; //pesos que serao retornados
	private double[] entradas; //Todas as entradas de um processamento
	private double[] saidas; //Todas as saídas de um processamento

	

	public double[] getValorSaida() {
		return valorSaida;
	}
	public void setValorSaida(double[] valorSaida) {
		this.valorSaida = valorSaida;
	}
	public double[] getDeltaPesoSaida() {
		return deltaPesoSaida;
	}
	public void setDeltaPesoSaida(double[] deltaPesoSaida) {
		this.deltaPesoSaida = deltaPesoSaida;
	}
	public double[] getDeltaPesoEscondida() {
		return deltaPesoEscondida;
	}
	public void setDeltaPesoEscondida(double[] deltaPesoEscondida) {
		this.deltaPesoEscondida = deltaPesoEscondida;
	}
	public double[] getPesos() {
		return pesos;
	}
	public void setPesos(double[] pesos) {
		this.pesos = pesos;
	}
	public double[] getEntradas() {
		return entradas;
	}
	public void setEntradas(double[] entradas) {
		this.entradas = entradas;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDoenca() {
		return doenca;
	}
	public void setDoenca(int doenca) {
		this.doenca = doenca;
	}
	public double[] getsPeso() {
		return sPeso;
	}
	public void setsPeso(double[] sPeso) {
		this.sPeso = sPeso;
	}
	public double[] getnSaida() {
		return valorSaida;
	}
	public void setnSaida(double[] nSaida) {
		this.valorSaida = nSaida;
	}
	
	public double[] getePeso() {
		return ePeso;
	}
	public void setePeso(double[] ePeso) {
		this.ePeso = ePeso;
	}
	
	public double[] getValorEntrada() {
		return valorEntrada;
	}
	public void setValorEntrada(double[] valorEntrada) {
		this.valorEntrada = valorEntrada;
	}
	
	public double[] getValorEsperado() {
		return valorEsperado;
	}
	
	public void setValorEsperado(double[] valorEsperado) {
		this.valorEsperado = valorEsperado;
	}
	
	public void setNovoBiasSaida(double bias){
		this.sPeso[5] = bias;
	}
	
	public void setNovoBiasEntrada(double bias){
		this.ePeso[5] = bias;
	}
	
	public double[] getSaidas() {
		return saidas;
	}
	public void setSaidas(double[] saidas) {
		this.saidas = saidas;
	}
	
	//=================================================================
	//============================Funcoes==============================
	//=================================================================
	
	public static double DerivadaDaFuncaoDeAtivacao(double x){
		double funcaoX;
		double derivada;

		funcaoX = (double)((2 / (1 + Math.pow(-1, x))) -1);
		derivada = (double)(0.5*((1+ funcaoX)*(1- funcaoX)));
		return derivada;
	}
	
    public double[] InicializarPesos(int numeroPesos){
    	pesos = null;
    	int i = 0;
    	for(i = 1; i < numeroPesos; i++){
    		Random gerador = new Random();
    		pesos[i] = gerador.nextDouble();
    	}
    	pesos[numeroPesos] = 1; //Bias
    	return pesos;
    }
    
    /**
     * Gerar os intervalos de nutrientes
     * @param inicio
     * @param fim
     * @return
     */
    public static int GerarValoresIntervaloNutriente(int inicio, int fim){
    	Random gerador = new Random();
    	return gerador.nextInt((fim - inicio) + 1) + inicio;
    }
  
    /**
     * Iniciando valores de entrada de um processo
     * @return
     */
    public double[] InicializarEntradas(int doenca){
    	entradas = null;
    	
    	//saudavel = 0
    	//infarto = 1
    	//hipertigliceridemia = 2
    	//derrame = 3
    	
    	if(doenca == 0){ //saudavel
    		//CT ruim acima de 200
        	entradas[0] = GerarValoresIntervaloNutriente(100, 200);
        	//HDL maior que 35
        	entradas[1] = GerarValoresIntervaloNutriente(35, 120);
        	//LDL limite de 130
        	entradas[2] = GerarValoresIntervaloNutriente(50, 130);
        	//glicose limite de 100
        	entradas[3] = GerarValoresIntervaloNutriente(30, 100);
        	//TG limite de 130
        	entradas[4] = GerarValoresIntervaloNutriente(50, 130);
    	}else if(doenca == 1){ //infarto
    		//CT ruim acima de 200
        	entradas[0] = GerarValoresIntervaloNutriente(200, 280);
        	//HDL maior que 35
        	entradas[1] = GerarValoresIntervaloNutriente(35, 120);
        	//LDL limite de 130
        	entradas[2] = GerarValoresIntervaloNutriente(50, 130);
        	//glicose limite de 100
        	entradas[3] = GerarValoresIntervaloNutriente(30, 100);
        	//TG limite de 130
        	entradas[4] = GerarValoresIntervaloNutriente(50, 130);
    	}else if(doenca == 2){ //hipertigliceridemia
    		//CT ruim acima de 200
        	entradas[0] = GerarValoresIntervaloNutriente(100, 200);
        	//HDL maior que 35
        	entradas[1] = GerarValoresIntervaloNutriente(35, 120);
        	//LDL limite de 130
        	entradas[2] = GerarValoresIntervaloNutriente(50, 130);
        	//glicose limite de 100
        	entradas[3] = GerarValoresIntervaloNutriente(30, 100);
        	//TG limite de 130
        	entradas[4] = GerarValoresIntervaloNutriente(130, 160);
    	}else if(doenca == 3){//derrame
    		//CT ruim acima de 200
        	entradas[0] = GerarValoresIntervaloNutriente(100, 200);
        	//HDL maior que 35
        	entradas[1] = GerarValoresIntervaloNutriente(35, 120);
        	//LDL limite de 130
        	entradas[2] = GerarValoresIntervaloNutriente(130, 180);
        	//glicose limite de 100
        	entradas[3] = GerarValoresIntervaloNutriente(30, 100);
        	//TG limite de 130
        	entradas[4] = GerarValoresIntervaloNutriente(50, 130);
    	}
    	
    	return entradas;
    }
    
    public double[] InicializarValoresEsperados(int doenca){
    	
    	if(doenca == 0){ //saudavel
    		saidas[0] = 0;
    		saidas[1] = 0;
    		saidas[2] = 0;
    	}else if(doenca == 1){ //infarto
    		saidas[0] = 1;
    		saidas[1] = 0;
    		saidas[2] = 0;
    	}else if(doenca == 2){ //hipertigliceridemia
    		saidas[0] = 0;
    		saidas[1] = 1;
    		saidas[2] = 0;
    	}else if(doenca == 3){//derrame
    		saidas[0] = 0;
    		saidas[1] = 0;
    		saidas[2] = 1;
    	}
    	return saidas;
    }
   
}
