package br.com.app.tcc.model;

import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Neuronio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private double[] valor; //valor da rede

	private double[] valorEsperado; //Valor esperado da rede
	private int doenca; //qual doenca o neuronio esta treinando
	
	//Variaveis auxiliares
	private double[] entradas; //Todas as entradas de um processamento
	private double[] saidas; //Todas as saídas de um processamento
	
	public double[] respostaRede = new double[3];
	private String nomeDoenca;
	

	public String getNomeDoenca() {
		return nomeDoenca;
	}

	public void setNomeDoenca(String nomeDoenca) {
		this.nomeDoenca = nomeDoenca;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double[] getValor() {
		return valor;
	}

	public void setValor(double[] valor) {
		this.valor = valor;
	}

	public double[] getValorEsperado() {
		return valorEsperado;
	}

	public void setValorEsperado(double[] valorEsperado) {
		this.valorEsperado = valorEsperado;
	}

	public int getDoenca() {
		return doenca;
	}

	public void setDoenca(int doenca) {
		this.doenca = doenca;
	}

	public double[] getEntradas() {
		return entradas;
	}

	public void setEntradas(double[] entradas) {
		this.entradas = entradas;
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
    
    private double GerarNumeroAleatorio(){
    	Random gerador = new Random();
    	return gerador.nextDouble();
    }
  
    /**
     * Iniciando valores de entrada de um processo
     * @return
     */
    public double[] InicializarEntradas(int doenca){
    	entradas = new double[6];
    	
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
        	entradas[0] = GerarValoresIntervaloNutriente(201, 280) * 10; //201, 280
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
        	entradas[4] = GerarValoresIntervaloNutriente(131, 160) * 10; //131, 160
    	}else if(doenca == 3){//derrame
    		//CT ruim acima de 200
        	entradas[0] = GerarValoresIntervaloNutriente(100, 200);
        	//HDL maior que 35
        	entradas[1] = GerarValoresIntervaloNutriente(35, 120);
        	//LDL limite de 130
        	entradas[2] = GerarValoresIntervaloNutriente(131, 180) * 10; //131, 180
        	//glicose limite de 100
        	entradas[3] = GerarValoresIntervaloNutriente(30, 100);
        	//TG limite de 130
        	entradas[4] = GerarValoresIntervaloNutriente(50, 130);
    	}
    	
    	entradas[5] = GerarNumeroAleatorio(); //Bias
    	return entradas;
    }
    
    public double[] InicializarValoresEsperados(int doenca){
    	
    	saidas = new double[3];
    	
    	if(doenca == 0){ //saudavel
    		saidas[0] = -1;
    		saidas[1] = -1;
    		saidas[2] = -1;
    	}else if(doenca == 1){ //infarto
    		saidas[0] = 1;
    		saidas[1] = -1;
    		saidas[2] = -1;
    	}else if(doenca == 2){ //hipertigliceridemia
    		saidas[0] = -1;
    		saidas[1] = 1;
    		saidas[2] = -1;
    	}else if(doenca == 3){//derrame
    		saidas[0] = -1;
    		saidas[1] = -1;
    		saidas[2] = 1;
    	}
    	return saidas;
    }
   
}
