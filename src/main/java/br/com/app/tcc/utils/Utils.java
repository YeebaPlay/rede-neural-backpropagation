package br.com.app.tcc.utils;

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
}
