package br.com.app.tcc.controller;

import java.util.ArrayList;
import java.util.Random;

import br.com.app.tcc.config.Config;
import br.com.app.tcc.model.Neuronio;
import br.com.app.tcc.utils.Utils;

public class TreinamentoController {
	
	/**
	 * Dúvidas...
	 */

	// Inciar variaveis
	private static int cicloAtual = 0;
	private double saidaDesejada;

	
	//Resultados dos neuronios
	private double[] neuronioSaida;
	private double[] resultadoSomaSaida;
	private double[] resultadoSomaEscondida;
	private double[] neuronioEscondida;
	
	//Erro da rede
	private double[] sigmaSaida; //Erro da rede
	private double[] sigmaEscondida; //Erro da rede

	//Inicializacao dos pesos
	private double[][] pesosEscondida;
	private double[][] pesosSaida;
	private double[][] deltaPesoEscondida; //Variacao dos pesos
	private double[][] deltaPesoSaida; //Variacao dos pesos
	
	//Variaveis auxiliares
	private double erroQuadraticoParcial;
	ArrayList<Double> listaErrosQuadradicos = new ArrayList<Double>();
	double pesoRede;
	double valorEntradaRede;
	double valorSaidaRede;
	double resultadoDerivadaFuncaoAtivacao;
	double resultadoNovosPesoCamadaEscondida;
	double resultadoNovosPesoCamadaSaida;
	
	ArrayList<Neuronio> listaNeuroniosEntrada = new ArrayList<>();
	ArrayList<Neuronio> listaNeuroniosSaida = new ArrayList<>();
	ArrayList<Neuronio> listaNeuroniosEscondido = new ArrayList<>();
	
	int tamanhoLista = 0;

	// Onde será treinada a rede
	public String TreinamentoRede() {

		//Gerando as listas de doecas
		GerarListasDoencas();
		
		//Iniciando todos os arrays
		iniciarVariaveis();
		tamanhoLista = listaNeuroniosEntrada.size(); 
		//////////////// PASSO 1 ////////////
		while(Config.CICLO > cicloAtual){
			erroQuadraticoParcial = 0;
			///////////// PASSO 2///////////////
			for (int posicaoAtualLista = 0; posicaoAtualLista < tamanhoLista; posicaoAtualLista++) {
				///////// PASSO 3/////////
				//===================================================================
				// calcular cada neuronio da camada de entrada com a escondida OK
				//===================================================================
				for (int i = 0; i < Config.NUMERODENEURONIOSESCONDIDA; i++) {
					neuronioEscondida[i] = 0.0;
					//for cada neuronio da camada de entrada
					for(int j = 0; j < Config.NUMERODENEURONIOSENTRADA; j++){
						//Soma do valor da multiplicacao camada de entrada sobre a escondida
						valorEntradaRede = listaNeuroniosEntrada.get(posicaoAtualLista).getValor()[j];
						pesoRede = pesosEscondida[i][j];
						neuronioEscondida[i] += (valorEntradaRede * pesoRede);
					}
					//Somar o bias
					pesoRede = pesosEscondida[i][Config.NUMERODENEURONIOSENTRADA];
					neuronioEscondida[i] += pesoRede;
					
					// Agora temos que aplicar a funcao de ativacao 
					// Funcao de ativacao = (2 / (1 + exp(-x)) -1
				    resultadoSomaEscondida[i] = Utils.FuncaoAtivacaoBipolar(neuronioEscondida[i]);
				}
				
				//===================================================================
				// Calcular cada neuronio da camada escondida com a camada de saida
				//===================================================================
				for (int i = 0; i < Config.NUMERONEURONIOSAIDA; i++) {
					neuronioSaida[i] = 0.0;
					//For cada neuronio da camada escondida
					for(int j = 0; j < Config.NUMERODENEURONIOSESCONDIDA; j++){
						//Agora vamos fazer o calculo da entrada do neuronio de saida
						pesoRede = pesosSaida[i][j];
						neuronioSaida[i] += (resultadoSomaEscondida[j] * pesoRede);
					}
					pesoRede = pesosSaida[i][Config.NUMERODENEURONIOSESCONDIDA];
					neuronioSaida[i] +=  pesoRede;// somando o bias
					
					// Agora temos que aplicar a funcao de ativacao
					// Funcao de ativacao = (2 / (1 + exp(-x)) -1
					resultadoSomaSaida[i] = Utils.FuncaoAtivacaoBipolar(neuronioSaida[i]);
					//System.out.println("Saida: "+resultadoSomaSaida[i]+" Esperado: "+listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[i]);
				}
				
				///////// PASSO 6 - BACKPROGATION ////////////
				
				for (int i = 0; i < Config.NUMERONEURONIOSAIDA; i++) {
					//Obtendo a saida desejada
					saidaDesejada = listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[i];
	
					resultadoDerivadaFuncaoAtivacao = Utils.DerivadaDaFuncaoDeAtivacaoBipolar(neuronioSaida[i]);
					//System.out.println("Derivada de: "+ neuronioSaida[i] +" = "+resultadoDerivadaFuncaoAtivacao);
					// calcular o sigma (variacao do erro)
					sigmaSaida[i] = (double) ((saidaDesejada - resultadoSomaSaida[i]) * resultadoDerivadaFuncaoAtivacao);
	
					erroQuadraticoParcial = (0.5)*((saidaDesejada - resultadoSomaSaida[i])*(saidaDesejada - resultadoSomaSaida[i]));
					
					//Calcular a variacao dos pesos do neuronio de saida
					for (int k = 0; k < Config.NUMERODENEURONIOSESCONDIDA; k++){
						deltaPesoSaida[i][k] = (double) ((Config.TAXAAPRENDIZAGEM * sigmaSaida[i] * resultadoSomaEscondida[k]));
					}
					//Bias não tem o resultadoEscondida, por isso feito separadamente
					deltaPesoSaida[i][Config.NUMERODENEURONIOSESCONDIDA] = (double) ((Config.TAXAAPRENDIZAGEM * sigmaSaida[i]));
				
				}
		

				///////// PASSO 7//////////

				//===================================================================
				// Atualizar todos os pesos das camadas escondidas e de saida
				//===================================================================
				
				// percorrer todos os neuronios da camada escondida com a cada de entrada para calcular os novos pesos
				for (int i = 0; i < Config.NUMERODENEURONIOSESCONDIDA; i++) {
					sigmaEscondida[i] = 0;
					// calcular o sigma
					for(int k =0; k < Config.NUMERONEURONIOSAIDA; k++){
						sigmaEscondida[i] += sigmaSaida[k] * pesosSaida[k][i];
					}
					resultadoDerivadaFuncaoAtivacao = Utils.DerivadaDaFuncaoDeAtivacaoBipolar(neuronioEscondida[i]);
					//System.out.println("Derivada de: "+ neuronioEscondida[i] +" = "+resultadoDerivadaFuncaoAtivacao);
					sigmaEscondida[i] = sigmaEscondida[i] * resultadoDerivadaFuncaoAtivacao;
					
					// variacao do bias
					deltaPesoEscondida[i][Config.NUMERODENEURONIOSENTRADA] = (Config.TAXAAPRENDIZAGEM * sigmaEscondida[i]);
				}
				
				// Calcular o delta da escondida
				for (int i = 0; i < Config.NUMERODENEURONIOSESCONDIDA; i++) {
					for(int j = 0; j < Config.NUMERODENEURONIOSENTRADA; j++){
						//variacao dos pesos
						deltaPesoEscondida[i][j] = (Config.TAXAAPRENDIZAGEM * sigmaEscondida[i] * (listaNeuroniosEntrada.get(posicaoAtualLista).getValor()[j]));
					}
					// variacao do bias
					deltaPesoEscondida[i][Config.NUMERODENEURONIOSENTRADA] = (Config.TAXAAPRENDIZAGEM * sigmaEscondida[i]);
				}

				///////// PASSO 8////////////
				
				// Atualizar os pesos escondidos  para entrada
				for (int i = 0; i < Config.NUMERODENEURONIOSESCONDIDA; i++) {
					for (int k = 0; k <= Config.NUMERODENEURONIOSENTRADA; k++){
						//System.out.println("Peso escondida: "+pesosEscondida[i][k]+" Peso atualizado: " +deltaPesoEscondida[i][k]);
						resultadoNovosPesoCamadaEscondida = deltaPesoEscondida[i][k];
						pesosEscondida[i][k] += resultadoNovosPesoCamadaEscondida;
					}
				}
				
				//Atualizar os pesos da camada de saida para camada escondida
				for (int i = 0; i < Config.NUMERONEURONIOSAIDA; i++) {
					for (int j = 0; j <= Config.NUMERODENEURONIOSESCONDIDA; j++){
						//O bias está no último elemento
						resultadoNovosPesoCamadaSaida = deltaPesoSaida[i][j];
						pesosSaida[i][j] += resultadoNovosPesoCamadaSaida;
					}
				}
			} // for vetor atual
			//listaErrosQuadradicos.add(erroQuadraticoParcial);
			cicloAtual++;
		}//while ciclos
		System.out.println("Sucesso!!");
		
		/*String res = "";
		
		for(int i = 0; i < listaErrosQuadradicos.size(); i++){
			res += "N: "+i+" Erro: "+listaErrosQuadradicos.get(i)+"<br />";
			
		}*/
		
		return TesteRede();
		
	}//Fim do metodo
	
	public String TesteRede(){
	
		String res = null;
		double[] neuronioEscondidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA+1];
		double[] neuronioSaidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA+1];
		double[] resultadoSomaEscondidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA];
		double[] resultadoSomaSaidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA];
		int acerto = 0;
		
		
		for (int posicaoAtualLista = 0; posicaoAtualLista < tamanhoLista; posicaoAtualLista++) {
			for (int i = 0; i < Config.NUMERODENEURONIOSESCONDIDA; i++) {
				neuronioEscondidaTeste[i] = 0.0;
				//for cada neuronio da camada de entrada
				for(int j = 0; j < Config.NUMERODENEURONIOSENTRADA; j++){
					//Soma do valor da multiplicacao camada de entrada sobre a escondida
					valorEntradaRede = listaNeuroniosEntrada.get(posicaoAtualLista).getValor()[j];
					pesoRede = pesosEscondida[i][j];
					neuronioEscondidaTeste[i] += (valorEntradaRede * pesoRede);
				}
				//Somar o bias
				pesoRede = pesosEscondida[i][Config.NUMERODENEURONIOSENTRADA];
				neuronioEscondidaTeste[i] += pesoRede;
				
				// Agora temos que aplicar a funcao de ativacao 
				// Funcao de ativacao = (2 / (1 + exp(-x)) -1
			    resultadoSomaEscondidaTeste[i] = Utils.FuncaoAtivacao(neuronioEscondidaTeste[i]);
			}
			
			//===================================================================
			// Calcular cada neuronio da camada escondida com a camada de saida
			//===================================================================
			for (int i = 0; i < Config.NUMERONEURONIOSAIDA; i++) {
				neuronioSaidaTeste[i] = 0.0;
				//For cada neuronio da camada escondida
				for(int j = 0; j < Config.NUMERODENEURONIOSESCONDIDA; j++){
					//Agora vamos fazer o calculo da entrada do neuronio de saida
					pesoRede = pesosSaida[i][j];
					neuronioSaidaTeste[i] += (resultadoSomaEscondidaTeste[j] * pesoRede);
				}
				pesoRede = pesosSaida[i][Config.NUMERODENEURONIOSESCONDIDA];
				neuronioSaidaTeste[i] +=  pesoRede;// somando o bias
				
				// Agora temos que aplicar a funcao de ativacao
				// Funcao de ativacao = (2 / (1 + exp(-x)) -1
				resultadoSomaSaidaTeste[i] = Utils.FuncaoAtivacaoBipolar(neuronioSaidaTeste[i]);
				listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] = resultadoSomaSaidaTeste[i];
				
				if(listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] > 0){
					
					if((listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] + 0.2) >= listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[i]){
						//listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] = 1; 
					}
				}else{
					if((listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] - 0.2) <= listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[i]){
						//listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] = -1; 
					}
				}
			}
		
			
			if(listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[0] == listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[0] && listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[1] == listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[1]){
				acerto++;
			}
			
			res += "========= Doenca: "+listaNeuroniosSaida.get(posicaoAtualLista).getNomeDoenca()+" =========<br />";
			
			for(int j = 0; j < Config.NUMERONEURONIOSAIDA; j++){
				
				res += "Resultado esperado:  "+ listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[j]+" Resultado obtido " + listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[j]+"<br />";
				
			}
			
			res += "<br />";
			
			
		}
		
		return res+acerto;
	}

	private void iniciarVariaveis() {
		// TODO Auto-generated method stub
		erroQuadraticoParcial = 0;
		neuronioSaida = new double[Config.NUMERONEURONIOSAIDA];
		resultadoSomaSaida = new double[Config.NUMERONEURONIOSAIDA];
		resultadoSomaEscondida = new double[Config.NUMERODENEURONIOSESCONDIDA];
		neuronioEscondida = new double[Config.NUMERODENEURONIOSESCONDIDA+1];
		sigmaEscondida = new double[Config.NUMERODENEURONIOSESCONDIDA];
		sigmaSaida = new double[Config.NUMERONEURONIOSAIDA];
		deltaPesoSaida = new double[Config.NUMERONEURONIOSAIDA+1][Config.NUMERODENEURONIOSESCONDIDA+1];
		deltaPesoEscondida = new double[Config.NUMERODENEURONIOSESCONDIDA+1][Config.NUMERODENEURONIOSENTRADA+1];
		
		/*for(int i = 0; i <= NUMERONEURONIOSAIDA; i++){
			for(int j = 0; j <= NUMERODENEURONIOSESCONDIDA; j++){
				deltaPesoSaida[i][j] = 0;
			}	
		}
		
		for(int i = 0; i <= NUMERODENEURONIOSESCONDIDA; i++){
			for(int j = 0; j <= NUMERODENEURONIOSENTRADA; j++){
				deltaPesoEscondida[i][j] = 0;
			}	
		}*/
	}

	public void PreencherListaNeuronios(int doenca) {
		int NUMERODECASOS = 200; // Para cada doenca criara n casos
		System.out.println("Doenca: "+doenca+" sendo gerada");
		pesosEscondida = new double[Config.NUMERODENEURONIOSESCONDIDA+1][Config.NUMERODENEURONIOSENTRADA+1]; //Bias +1
		pesosSaida = new double[Config.NUMERONEURONIOSAIDA+1][Config.NUMERODENEURONIOSESCONDIDA+1]; //Bias +1
		String nomeDoenca = null;
		//===================================================================
		// Iniciando pesos do NE e no NS
		//===================================================================
		
		// Gerar n pesos para cadamada escondida
		pesosEscondida = InicializarPesos(Config.NUMERODENEURONIOSESCONDIDA, Config.NUMERODENEURONIOSENTRADA);
		// Gerar n pesos para cadamada de saida
		pesosSaida = InicializarPesos(3, Config.NUMERODENEURONIOSESCONDIDA);
		
		if(doenca == 0){
			nomeDoenca = "Saudável";
		}else if(doenca == 1){
			nomeDoenca = "infarto";
		}else if(doenca == 2){
			nomeDoenca = "Hipertigliceridemia";
		}else if(doenca == 3){
			nomeDoenca = "Derrame";
		}
		
		// Para cada caso de teste
		for (int i = 0; i < NUMERODECASOS; i++) {
			
			Neuronio neuronioEntrada = new Neuronio();
			Neuronio neuronioEscondido = new Neuronio();
			Neuronio neuronioSaida = new Neuronio();
			
			//===================================================================
			// Iniciando valores de entrada e valores esperados pelo NS
			//===================================================================
			
			// Gerar NUMERODECASOS entrada para 
			double[] entradas = neuronioEntrada.InicializarEntradas(doenca); // Saudavel
			// Gerar 5 saidas esperadas
			double[] valoresEsperados = neuronioSaida.InicializarValoresEsperados(doenca); // Saudavel
			
			//===================================================================
			// Armazenando valores produzidos de forma aleatória
			//===================================================================

			neuronioEntrada.setValor(entradas); // Entradas da rede
			neuronioEntrada.setDoenca(doenca);
			neuronioEntrada.setNomeDoenca(nomeDoenca);

			neuronioEscondido.setDoenca(doenca);
			neuronioEscondido.setNomeDoenca(nomeDoenca);

			neuronioSaida.setValorEsperado(valoresEsperados); // Saida esperada
			neuronioSaida.setDoenca(doenca);
			neuronioSaida.setNomeDoenca(nomeDoenca);

			//===================================================================
			// Repassando valores as listas que serão trabalhadas no algoritmo
			//===================================================================
			
			listaNeuroniosEntrada.add(neuronioEntrada);
			listaNeuroniosEscondido.add(neuronioEscondido);
			listaNeuroniosSaida.add(neuronioSaida);
		}
	}
	
	/**
	 * Gerar a lista de todas as doencas que estao sendo estudadas pela rede
	 */
	private void GerarListasDoencas(){
		for(int i = 0; i < Config.DOENCAS.length; i++){
			PreencherListaNeuronios(Config.DOENCAS[i]);
		}
	}

	
	/**
	 * Metodo que inicializa os pesos
	 * @param numeroPesos
	 * @return
	 */
	 public double[][] InicializarPesos(int linha, int coluna){
	    	double[][] pesos = new double[linha+1][coluna+1]; //+ 1 do Bias
	    	double numeroRandomico = 0;
	    	int i = 0, j = 0;
	    	//Bias está no último item do array
	    	for(i = 0; i <= linha; i++){
	    		for(j = 0; j <= coluna; j++){
	    		Random gerador = new Random();
	    		numeroRandomico = gerador.nextDouble() * Math.pow(-1, gerador.nextInt());
	    		pesos[i][j] = numeroRandomico/2;
	    		}
	    	}
	    	return pesos;
	    }
	
}
