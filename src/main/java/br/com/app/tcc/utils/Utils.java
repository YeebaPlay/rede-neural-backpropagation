package br.com.app.tcc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.app.tcc.model.Saida;
import br.com.app.tcc.model.Entrada;

public class Utils {

	//f’(x) = 0.5 * (1 + f(x)) * (1 – f(x) )
	public static double DerivadaDaFuncaoDeAtivacaoBipolar(double x){
		double funcaoX;
		double derivada;
		funcaoX = (double) FuncaoAtivacaoBipolar(x);
		derivada = (double)(0.5*((1+ funcaoX)*(1- funcaoX)));
		return derivada;
	}
	
	//f(x) = -1 + 2 / (1 + e^-x)
	public static double FuncaoAtivacaoBipolar(double valor){
		double f = Math.exp(-1*valor);
		double resultado = ( 2 / (1 + f)) -1;
		return resultado;
	}
	
	//f(x) = 1/(1+e^-x)
	public static double FuncaoAtivacao(double valor){
		double f = Math.exp(-1*valor);
		return 1 / (1 + f);
	}
	
	//f(x)' = f(x) * (1 - f(x))
	public static double DerivadaDaFuncaoAtivacao(double valor){
		double funcaoX;
		funcaoX = (double) FuncaoAtivacao(valor);
		return (funcaoX * (1 - funcaoX));
	}
	
	/**
     * Gerar os intervalos de nutrientes
     * @param inicio
     * @param fim
     * @return
     */
    private static int GerarValoresIntervaloNutriente(int inicio, int fim){
    	Random gerador = new Random();
    	return gerador.nextInt((fim - inicio) + 1) + inicio;
    }
    
    private static double GerarNumeroAleatorio(){
    	Random gerador = new Random();
    	return gerador.nextDouble();
    }
  
    /**
     * Iniciando valores de entrada de um processo
     * @return
     */
    public static List<Entrada> InicializarEntradas(int doenca){
   
		List<Entrada> listaValores = new ArrayList<>();
    	//saudavel = 0
    	//infarto = 1
    	//hipertigliceridemia = 2
    	//derrame = 3
    	
    
    	if(doenca == 0){ //saudavel
    		//CT ruim acima de 200
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(100, 200), "CT"));
        	//HDL maior que 35
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(35, 120), "HDL"));
        	//LDL limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "LDL"));
        	//glicose limite de 100
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(30, 100), "G"));
        	//TG limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "TG"));
    	}else if(doenca == 1){ //infarto
    		//CT ruim acima de 200
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(201, 280) * 10, "CT")); //201, 280
        	//HDL maior que 35
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(35, 120), "HDL"));
        	//LDL limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "LDL"));
        	//glicose limite de 100
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(30, 100), "G"));
        	//TG limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "TG"));
    	}else if(doenca == 2){ //hipertigliceridemia
    		//CT ruim acima de 200
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(100, 200), "CT"));
        	//HDL maior que 35
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(35, 120), "HDL"));
        	//LDL limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "LDL"));
        	//glicose limite de 100
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(30, 100), "G"));
        	//TG limite de 130
    		listaValores.add(new Entrada((double)  GerarValoresIntervaloNutriente(131, 160) * 10, "TG")); //131, 160
    	}else if(doenca == 3){//derrame
    		//CT ruim acima de 200
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(100, 200), "CT"));
        	//HDL maior que 35
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(35, 120), "HDL"));
        	//LDL limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(131, 180) * 10, "LDL")); //131, 180
        	//glicose limite de 100
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(30, 100), "G"));
        	//TG limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "TG"));
    	}else if(doenca == 4){ //nova doenca -> diabetes
    		//CT ruim acima de 200
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(100, 200), "CT"));
        	//HDL maior que 35
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(35, 120), "HDL"));
        	//LDL limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "LDL")); //131, 180
        	//glicose limite de 100
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(100, 180) * 10, "G"));
        	//TG limite de 130
    		listaValores.add(new Entrada((double) GerarValoresIntervaloNutriente(50, 130), "TG"));
    	}
    	
    	listaValores.add(new Entrada((double) GerarNumeroAleatorio(), "BIAS")); //Bias
    	return listaValores;
    }
    
    public static List<Saida> InicializarValoresEsperados(int doenca){
    	
    	List<Saida> listaValores = new ArrayList<>();
    	
    	if(doenca == 0){ //saudavel
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida(-1.0));
    	}else if(doenca == 1){ //infarto
    		listaValores.add(new Saida(1.0));
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida(-1.0));
    	}else if(doenca == 2){ //hipertigliceridemia
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida( 1.0));
    		listaValores.add(new Saida(-1.0));
    	}else if(doenca == 3){//derrame
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida(1.0));
    	}else if(doenca == 4){//derrame
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida(-1.0));
    		listaValores.add(new Saida(-1.0));
    	}
    	return listaValores;
    }
}
