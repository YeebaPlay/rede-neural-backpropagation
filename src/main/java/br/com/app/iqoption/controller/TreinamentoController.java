package br.com.app.iqoption.controller;

import java.util.ArrayList;

import br.com.app.iqoption.model.Neuronio;

public class TreinamentoController {
	
	/**
	 * Entrada de -0.9407975103162227 na funcao de ativacao saiu um numero -32.78236305065465
	 */

	// Inciar variaveis
	private static int cicloAtual = 0;
	private double Momento = 0.05;
	private double saidaDesejada;

	
	//Resultados dos neuronios
	private double[] neuronioSaida;
	private double[] resultadoSomaSaida;
	private double[] resultadoSomaEscondida;
	private double[] neuronioEscondida;
	
	//Erro da rede
	private double sigmaSaida; //Erro da rede
	private double sigmaEscondida; //Erro da rede
	
	//Somatorio dos deltas
	private double[] somaDeltaEscondida;
	private double[] somaDeltaSaida;

	//Constantes
	private final int CICLO = 10;
	private static final double TAXAAPRENDIZAGEM = 0.5;
	private static final int NUMERODENEURONIOSENTRADA = 5;// + bias
	private static final int NUMERODENEURONIOSESCONDIDA = 10;// + bias
	private static final int[] DOENCAS = { 0, 1, 2, 3 }; // 0 = bom 1 = infarto 2 = hiper 3 = derrame

	//Variaveis auxiliares
	private double erroQuadraticoParcial;
	ArrayList<Double> listaErrosQuadradicos = new ArrayList<Double>();
	double pesoRede;
	double valorEntradaRede;
	double valorSaidaRede;
	double resultadoDerivadaFuncaoAtivacao;
	
	ArrayList<Neuronio> listaNeuroniosEntrada = new ArrayList<>();
	ArrayList<Neuronio> listaNeuroniosSaida = new ArrayList<>();
	ArrayList<Neuronio> listaNeuroniosEscondido = new ArrayList<>();
	
	int tamanhoLista = 0;

	// Onde será treinada a rede
	public void TreinamentoRede() {

		//Gerando as listas de doecas
		GerarListasDoencas();
		
		//Iniciando todos os arrays
		iniciarVariaveis();
		tamanhoLista = listaNeuroniosEntrada.size();
		
		while(CICLO > cicloAtual){
			erroQuadraticoParcial = 0;
			///////////// PASSO 2///////////////
			for (int posicaoAtualLista = 0; posicaoAtualLista < tamanhoLista; posicaoAtualLista++) {
				///////// PASSO 3/////////
			
				//===================================================================
				// calcular cada neuronio da camada de entrada com a escondida OK
				//===================================================================
				for (int i = 0; i < NUMERODENEURONIOSESCONDIDA; i++) {
					neuronioEscondida[i] = 0.0;
					//for cada neuronio da camada de entrada
					for(int j = 0; j < NUMERODENEURONIOSENTRADA; j++){
						//Soma do valor da multiplicacao camada de entrada sobre a escondida
						valorEntradaRede = listaNeuroniosEntrada.get(posicaoAtualLista).getValor()[j];
						pesoRede = listaNeuroniosEscondido.get(posicaoAtualLista).getPesos()[i];
						neuronioEscondida[i] += (valorEntradaRede * pesoRede);
					}
					//Somar o bias
					pesoRede = listaNeuroniosEscondido.get(posicaoAtualLista).getPesos()[NUMERODENEURONIOSESCONDIDA];
					neuronioEscondida[i] = neuronioEscondida[i] + pesoRede;
					
					// Agora temos que aplicar a funcao de ativacao 
					// Funcao de ativacao = (2 / (1 + exp(-x)) -1
				    resultadoSomaEscondida[i] = FuncaoAtivacao(neuronioEscondida[i]);
				}
				
				//===================================================================
				// Calcular cada neuronio da camada escondida com a camada de saida
				//===================================================================
				for (int i = 0; i < listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado().length; i++) {
					neuronioSaida[i] = 0.0;
					//For cada neuronio da camada escondida
					for(int j = 0; j < NUMERODENEURONIOSESCONDIDA; j++){
						//Agora vamos fazer o calculo da entrada do neuronio de saida
						pesoRede = listaNeuroniosSaida.get(posicaoAtualLista).getPesos()[j];
						neuronioSaida[i] += (resultadoSomaEscondida[j] * pesoRede);
					}
					pesoRede = listaNeuroniosSaida.get(posicaoAtualLista).getPesos()[NUMERODENEURONIOSESCONDIDA];
					neuronioSaida[i] +=  pesoRede;// somando o bias
					
					// Agora temos que aplicar a funcao de ativacao
					// Funcao de ativacao = (2 / (1 + exp(-x)) -1
					resultadoSomaSaida[i] = FuncaoAtivacao(neuronioSaida[i]);
					
					///////// PASSO 6 - BACKPROGATION ////////////
					
					//Obtendo a saida desejada
					saidaDesejada = listaNeuroniosSaida.get(posicaoAtualLista).getValorEsperado()[i];
	
					resultadoDerivadaFuncaoAtivacao = Neuronio.DerivadaDaFuncaoDeAtivacao(neuronioSaida[i]);
					// calcular o sigma (variacao do erro)
					sigmaSaida = (double) ((saidaDesejada - resultadoSomaSaida[i]) * resultadoDerivadaFuncaoAtivacao);
	
					erroQuadraticoParcial += (0.5)*((saidaDesejada - resultadoSomaSaida[i])*(saidaDesejada - resultadoSomaSaida[i]));
					
					//Calcular a variacao dos pesos do neuronio de saida
					for (int k = 0; k < NUMERODENEURONIOSESCONDIDA; k++){
						listaNeuroniosSaida.get(posicaoAtualLista).deltaPeso[k] = (double) ((TAXAAPRENDIZAGEM * sigmaSaida * resultadoSomaEscondida[k]) + (Momento * listaNeuroniosSaida.get(posicaoAtualLista).deltaPeso[k]));
					}
					//Bias não tem o resultadoEscondida, por isso feito separadamente
					listaNeuroniosSaida.get(posicaoAtualLista).deltaPeso[NUMERODENEURONIOSESCONDIDA] = (double) ((TAXAAPRENDIZAGEM * sigmaSaida) + (Momento * listaNeuroniosSaida.get(posicaoAtualLista).deltaPeso[NUMERODENEURONIOSESCONDIDA]));
				}

				///////// PASSO 7//////////

				//===================================================================
				// Atualizar todos os pesos das camadas escondidas e de saida
				//===================================================================
				
				// percorrer todos os neuronios da camada escondida com a cada de entrada para calcular os novos pesos
				for (int i = 0; i < NUMERODENEURONIOSESCONDIDA; i++) {
					for(int j = 0; j < NUMERODENEURONIOSENTRADA; j++){
						// calcular o sigma
						sigmaEscondida = sigmaSaida * listaNeuroniosSaida.get(posicaoAtualLista).getPesos()[j];
						sigmaEscondida = sigmaEscondida * Neuronio.DerivadaDaFuncaoDeAtivacao(neuronioEscondida[j]);
						//variacao dos pesos
						listaNeuroniosEscondido.get(posicaoAtualLista).deltaPeso[i] = (TAXAAPRENDIZAGEM * sigmaEscondida * (listaNeuroniosEntrada.get(posicaoAtualLista).getValor()[j])) + (Momento * listaNeuroniosEscondido.get(posicaoAtualLista).deltaPeso[i]);
					}
					// variacao do bias
					listaNeuroniosEscondido.get(posicaoAtualLista).deltaPeso[NUMERODENEURONIOSESCONDIDA] = (TAXAAPRENDIZAGEM * sigmaEscondida) + (Momento * listaNeuroniosEscondido.get(posicaoAtualLista).deltaPeso[NUMERODENEURONIOSESCONDIDA]);
					
					// Atualizar os pesos atualizardos no laco de cima
					for (int k = 0; k <= NUMERODENEURONIOSENTRADA; k++){
						somaDeltaEscondida[i] += listaNeuroniosEscondido.get(posicaoAtualLista).deltaPeso[k];
					}
				}
				
				listaNeuroniosEscondido.get(posicaoAtualLista).setPesos(somaDeltaEscondida);

				///////// PASSO 8////////////
				
				//Atualizar os pesos da camada de saida para camada escondida
				for (int j = 0; j <= NUMERODENEURONIOSESCONDIDA; j++){
					//O bias está no último elemento
					somaDeltaSaida[j] = listaNeuroniosSaida.get(posicaoAtualLista).deltaPeso[j];
				}
				listaNeuroniosSaida.get(posicaoAtualLista).setPesos(somaDeltaSaida);
			} // for vetor atual
			listaErrosQuadradicos.add(erroQuadraticoParcial);
			cicloAtual++;
		}//while ciclos
		System.out.println("Sucesso!!");
		
		for(int i = 0; i < listaErrosQuadradicos.size(); i++){
			System.out.println("Ciclo "+i+" = "+listaErrosQuadradicos.get(i));
			
		}
		
	}//Fim do metodo

	private void iniciarVariaveis() {
		// TODO Auto-generated method stub
		erroQuadraticoParcial = 0;
		neuronioSaida = new double[listaNeuroniosSaida.get(0).getValorEsperado().length];
		resultadoSomaSaida = new double[listaNeuroniosSaida.get(0).getValorEsperado().length];
		resultadoSomaEscondida = new double[NUMERODENEURONIOSESCONDIDA];
		neuronioEscondida = new double[NUMERODENEURONIOSESCONDIDA+1];
		somaDeltaEscondida = new double[NUMERODENEURONIOSESCONDIDA+1];
		somaDeltaSaida = new double[NUMERODENEURONIOSESCONDIDA+1];
	}

	public void PreencherListaNeuronios(int doenca) {
		int NUMERODECASOS = 10; // Para cada doenca criara 100 casos

		Neuronio neuronioEntrada = new Neuronio();
		Neuronio neuronioEscondido = new Neuronio();
		Neuronio neuronioSaida = new Neuronio();
		// Para cada caso de teste
		for (int i = 0; i < NUMERODECASOS; i++) {
			
			//===================================================================
			// Iniciando pesos do NE e no NS
			//===================================================================
			
			// Gerar n pesos para cadamada escondida
			double[] pesosEscondida = neuronioEscondido.InicializarPesos(NUMERODENEURONIOSESCONDIDA);
			// Gerar n pesos para cadamada de saida
			double[] pesosSaida = neuronioSaida.InicializarPesos(NUMERODENEURONIOSESCONDIDA);
			
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

			neuronioEscondido.setPesos(pesosEscondida); // Pesos da escondida com a entrada
			neuronioEscondido.setDoenca(doenca);

			neuronioSaida.setValorEsperado(valoresEsperados); // Saida esperada
			neuronioSaida.setPesos(pesosSaida); //Pesos da saida com a escondida
			neuronioSaida.setDoenca(doenca);

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
		for(int i =0; i < DOENCAS.length; i++){
			PreencherListaNeuronios(DOENCAS[i]);
		}
	}
	
	private double FuncaoAtivacao(double valor){
		double v = Math.exp(-valor);
		return 1.0 / (1.0 +  (v));
	}
	
	
}
