package br.com.app.iqoption.controller;

import java.util.ArrayList;

import br.com.app.iqoption.model.Neuronio;

public class TreinamentoController {

	// Inciar variaveis
	private int doencaAtual;
	private double Momento;
	private double SaidaDesejada;

	private double[] nSaida;
	private double[] oSaida;
	// private double ErroParcial;
	private double SigmaSaida;
	private double SigmaEscondida;
	private double[] oEscondida;
	private double[] nEscondida;
	
	//Somatorio dos deltas
	private double[] somaDelta = null;

	//Constantes
	private static final int QUANTIDADEMAXIMA = 0;
	private static final double TAXAAPRENDIZAGEM = 0.5;
	private static final int NUMERODENEURONIOSENTRADA = 5;// + bias
	private static final int NUMERODENEURONIOSESCONDIDA = 5;// + bias
	private static final int[] DOENCAS = { 0, 1, 2, 3 }; // 0 = bom 1 = infarto 2 = hiper 3 =
											// derrame

	// Sigma do erro ao quadrado dividido por 2

	ArrayList<Neuronio> listaNeuroniosEntrada = new ArrayList<>();;
	ArrayList<Neuronio> listaNeuroniosSaida = new ArrayList<>();;
	ArrayList<Neuronio> listaNeuroniosEscondido = new ArrayList<>();;

	// Onde será treinada a rede
	public void TreinamentoRede() {
		
		//Gerando as listas de doecas
		GerarListasDoencas();

		doencaAtual = 0;
		while(doencaAtual < QUANTIDADEMAXIMA){
			while (doencaAtual < DOENCAS.length) {
				
				// ErroQuadraticoParcial = 0;
				doencaAtual++;// incrementar o ciclo
				// ErroParcial = 0.0;//zerar o erro parcial
	
				///////////// PASSO 2///////////////
				for (int VetorAtual = 0; VetorAtual < listaNeuroniosEntrada.size(); VetorAtual++) {
					///////// PASSO 3/////////
				
					//===================================================================
					// calcular cada neuronio da camada de entrada com a escondida
					//===================================================================
					for (int i = 0; i < NUMERODENEURONIOSENTRADA; i++) {
						nEscondida[i] = 0.0;
						//for cada neuronio da camada de entrada
						for(int j = 0; j < NUMERODENEURONIOSESCONDIDA; j++){
							//Soma do valor da multiplicacao camada de entrada sobre a escondida
							nEscondida[i] = nEscondida[i] + (listaNeuroniosEntrada.get(VetorAtual).getValorEntrada()[i] * listaNeuroniosEscondido.get(VetorAtual).getePeso()[j]);
					
							// Agora temos que aplicar a funcao de ativacao 
							// Funcao de ativacao = (2 / (1 + exp(-x)) -1
						   oEscondida[i] = (double) ((2 / (1 + Math.pow(-1 * nEscondida[i], 1))) - 1);
						}
						
						// somar com o valor de cada bias da entrada para escondida bias
						nEscondida[i] = nEscondida[i] + listaNeuroniosEscondido.get(VetorAtual).getePeso()[5];
	
					}
					
					//===================================================================
					// Calcular cada neuronio da camada escondida com a camada de saida
					//===================================================================
					for (int i = 0; i < listaNeuroniosSaida.get(VetorAtual).getValorEsperado().length; i++) {
						nSaida[i] = 0.0;
						//For cada neuronio da camada escondida
						for(int j = 0; j < NUMERODENEURONIOSESCONDIDA; j++){
							//Agora vamos fazer o calculo da entrada do neuronio de saida
							nSaida[i] = nSaida[i] + (oEscondida[j] * listaNeuroniosSaida.get(VetorAtual).getsPeso()[i]);
				
							nSaida[i] = nSaida[i] + listaNeuroniosSaida.get(VetorAtual).getsPeso()[5];// somando o bias
																									
							// Agora temos que aplicar a funcao de ativacao
							// Funcao de ativacao = (2 / (1 + exp(-x)) -1
							double exponencial = Math.pow(-1, nSaida[i]);
							oSaida[i] = (double) ((2 / (1 + exponencial)) - 1);
							
							///////// PASSO 6 - BACKPROGATION ////////////
							
							//Calculando a saida desejada
							SaidaDesejada = listaNeuroniosEntrada.get(VetorAtual).getValorEsperado()[j];
			
							// calcular o sigma
							SigmaSaida = (double) ((SaidaDesejada - oSaida[i]) * Neuronio.DerivadaDaFuncaoDeAtivacao(nSaida[i]));
			
							//ErroParcial += (0.5)*((SaidaDesejada - OutSaida)*(SaidaDesejada - OutSaida));
			
							//Calcular a variacao dos pesos do neuronio de saida
							for (int k = 0; k < NUMERODENEURONIOSENTRADA; k++){
								listaNeuroniosSaida.get(VetorAtual).deltaPesoSaida[i] = (double) ((TAXAAPRENDIZAGEM * SigmaSaida * oEscondida[i]) + (Momento * listaNeuroniosSaida.get(VetorAtual).deltaPesoSaida[i]));
							}
							
							//Atualizar o peso do bias
							double novoBias = (double) ((TAXAAPRENDIZAGEM * SigmaSaida)
									+ (Momento * listaNeuroniosSaida.get(VetorAtual).getsPeso()[5]));
							listaNeuroniosSaida.get(VetorAtual).setNovoBiasSaida(novoBias);
						}
					}
	
					///////// PASSO 7//////////
	
					//===================================================================
					// Atualizar todos os pesos das camadas escondidas e de saida
					//===================================================================
					
					// percorrer todos os neuronios da camada escondida com a cada de entrada
					for (int i = 0; i < listaNeuroniosEscondido.get(VetorAtual).getValorEntrada().length; i++) {
						for(int j = 0; j < NUMERODENEURONIOSENTRADA; j++){
							// calcular o sigma
							SigmaEscondida = SigmaSaida * listaNeuroniosSaida.get(VetorAtual).getsPeso()[j];
							SigmaEscondida = SigmaEscondida * Neuronio.DerivadaDaFuncaoDeAtivacao(nEscondida[j]);
		
							listaNeuroniosEscondido.get(VetorAtual).deltaPesoEscondida[5] = (TAXAAPRENDIZAGEM * SigmaEscondida
									* (listaNeuroniosEntrada.get(VetorAtual).getValorEntrada()[i]))
									+ (Momento * listaNeuroniosEscondido.get(VetorAtual).deltaPesoEscondida[5]);
							
							// variacao do bias
							listaNeuroniosEscondido.get(VetorAtual).deltaPesoEscondida[5] = (TAXAAPRENDIZAGEM * SigmaEscondida)
									+ (Momento * listaNeuroniosEscondido.get(VetorAtual).deltaPesoEscondida[1]);
		
							// Atualizar os pesos
							for (int k = 0; k <= NUMERODENEURONIOSENTRADA; k++){
								somaDelta[i] += listaNeuroniosEscondido.get(VetorAtual).deltaPesoEscondida[k];
							}
						}
						listaNeuroniosEscondido.get(i).setePeso(somaDelta);
					}
	
					///////// PASSO 8////////////
					
					somaDelta = null;
					//Atualizar os pesos da camada de saida para camada escondida
					for (int i = 0; i < listaNeuroniosSaida.get(VetorAtual).getValorEsperado().length; i++) {
						for (int j = 0; j <= NUMERODENEURONIOSESCONDIDA; j++){
							//O bias está no último elemento
							somaDelta[i] = listaNeuroniosSaida.get(VetorAtual).deltaPesoSaida[j];
						}
						listaNeuroniosSaida.get(VetorAtual).setsPeso(somaDelta);
					}
				} // for vetor atual
	
			} // while
		}
	}

	public void PreencherListaNeuronios(int doenca) {
		int NUMERODECASOS = 100; // Para cada doenca criara 100 casos

		Neuronio neuronioEntrada = new Neuronio();
		Neuronio neuronioEscondido = new Neuronio();
		Neuronio neuronioSaida = new Neuronio();
		// Para cada caso de teste
		for (int i = 0; i < NUMERODECASOS; i++) {
			
			//===================================================================
			// Iniciando pesos do NE e no NS
			//===================================================================
			
			// Gerar 5 pesos para cadamada escondida
			double[] pesosEscondida = neuronioEscondido.InicializarPesos(5);
			// Gerar 5 pesos para cadamada de saida
			double[] pesosSaida = neuronioSaida.InicializarPesos(5);
			
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

			neuronioEntrada.setValorEntrada(entradas); // Entradas da rede
			neuronioEntrada.setDoenca(doenca);

			neuronioEscondido.setePeso(pesosEscondida); // Escondido pesos da entrada
			neuronioEscondido.setDoenca(doenca);

			neuronioSaida.setsPeso(pesosSaida); // Saida pesos
			neuronioSaida.setValorEsperado(valoresEsperados); // Saida esperada
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
	
	
}
