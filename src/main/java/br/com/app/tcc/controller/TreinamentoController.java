package br.com.app.tcc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.app.tcc.config.Config;
import br.com.app.tcc.model.Neuronio;
import br.com.app.tcc.model.Saida;
import br.com.app.tcc.model.Entrada;
import br.com.app.tcc.repository.NeuroniosRepository;
import br.com.app.tcc.repository.SaidaRepository;
import br.com.app.tcc.repository.ValorRepository;
import br.com.app.tcc.utils.Utils;

@Component
public class TreinamentoController {
	
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
	public double[] respostaRede = new double[3];
	
	private ArrayList<Neuronio> listaNeuroniosEntrada = new ArrayList<>();
	private ArrayList<Neuronio> listaNeuroniosSaida = new ArrayList<>();
	private ArrayList<Neuronio> listaNeuroniosEscondido = new ArrayList<>();
	private ArrayList<Integer> listaProntuariosAlerta = new ArrayList<>(); //Lista de prontuarios com probabilidade de problemas
	private ArrayList<Integer> listaProntuarioDetaque = new ArrayList<>(); //Prontuarios a serem verificados
	
	@Autowired
	private NeuroniosRepository bancoNeuronio;
	
	@Autowired
	private ValorRepository bancoValor;
	
	@Autowired
	private SaidaRepository bancoSaida;
	

	int numProntuario = 0;
	int tamanhoLista = 0;

	// Onde será treinada a rede
	public String TreinamentoRede(int teste) {
		
		//Iniciando todos os arrays
		iniciarVariaveis();
		//Preencher listas
		PreencherListas();
		
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
						valorEntradaRede = listaNeuroniosEntrada.get(posicaoAtualLista).getEntradas().get(j).getEntrada();
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
					saidaDesejada = listaNeuroniosSaida.get(posicaoAtualLista).getSaidas().get(i).getValor();
	
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
						deltaPesoEscondida[i][j] = (Config.TAXAAPRENDIZAGEM * sigmaEscondida[i] * (listaNeuroniosEntrada.get(posicaoAtualLista).getEntradas().get(j).getEntrada()));
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
		
		if(teste == 0){
			return TesteRede();
		}else{
			return TesteRedeNovaDoenca();
		}
	}//Fim do metodo
	
	private void PreencherListas() {
		
		pesosEscondida = new double[Config.NUMERODENEURONIOSESCONDIDA+1][Config.NUMERODENEURONIOSENTRADA+1]; //Bias +1
		pesosSaida = new double[Config.NUMERONEURONIOSAIDA+1][Config.NUMERODENEURONIOSESCONDIDA+1]; //Bias +1
		
		listaNeuroniosEntrada = bancoNeuronio.findByNeuronio("entrada");
		listaNeuroniosSaida = bancoNeuronio.findByNeuronio("saida");
		
		// Gerar n pesos para cadamada escondida
		pesosEscondida = InicializarPesos(Config.NUMERODENEURONIOSESCONDIDA, Config.NUMERODENEURONIOSENTRADA);
		// Gerar n pesos para cadamada de saida
		pesosSaida = InicializarPesos(3, Config.NUMERODENEURONIOSESCONDIDA);
	}

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
					valorEntradaRede = listaNeuroniosEntrada.get(posicaoAtualLista).getEntradas().get(j).getEntrada();
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
				respostaRede[i] = resultadoSomaSaidaTeste[i];
				
				VerificarPossivelDoenca(respostaRede, listaNeuroniosSaida.get(posicaoAtualLista).getNumProntuario());
				
				//Veriricar %
				if(respostaRede[i] > 0){
					
					if((respostaRede[i] + 0.2) >= listaNeuroniosSaida.get(posicaoAtualLista).getSaidas().get(i).getValor()){
						//listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] = 1; 
					}
				}else{
					if((respostaRede[i] - 0.2) <= listaNeuroniosSaida.get(posicaoAtualLista).getSaidas().get(i).getValor()){
						//listaNeuroniosSaida.get(posicaoAtualLista).respostaRede[i] = -1; 
					}
				}
			}
		
			
			if(listaNeuroniosSaida.get(posicaoAtualLista).getSaidas().get(0).getValor() == respostaRede[0] && listaNeuroniosSaida.get(posicaoAtualLista).getSaidas().get(1).getValor() == respostaRede[1]){
				acerto++;
			}
			
			res += "========= Doenca: "+listaNeuroniosSaida.get(posicaoAtualLista).getNomeDoenca()+" =========<br />";
			
			for(int j = 0; j < Config.NUMERONEURONIOSAIDA; j++){
				res += "R. esperado:  "+ listaNeuroniosSaida.get(posicaoAtualLista).getSaidas().get(j).getValor() +" R. obtido " + respostaRede[j]+"<br />";	
			}
			res += "<br />";
		}
		
		return res+acerto;
	}
	
	public String TesteRedeNovaDoenca(){
		
		listaNeuroniosEntrada = new ArrayList<>();
		listaNeuroniosSaida = new ArrayList<>();
		listaNeuroniosEscondido = new ArrayList<>();
		
		GerarListaNovaDoenca();
		
		String res = "=========== Número dos prontuários que precisam ser analisados ===========<br /><br />";
		double[] neuronioEscondidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA+1];
		double[] neuronioSaidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA+1];
		double[] resultadoSomaEscondidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA];
		double[] resultadoSomaSaidaTeste = new double[Config.NUMERODENEURONIOSESCONDIDA];
		int num = 0;
		tamanhoLista = listaNeuroniosEntrada.size(); 
		
		for (int posicaoAtualLista = 0; posicaoAtualLista < tamanhoLista; posicaoAtualLista++) {
			for (int i = 0; i < Config.NUMERODENEURONIOSESCONDIDA; i++) {
				neuronioEscondidaTeste[i] = 0.0;
				//for cada neuronio da camada de entrada
				for(int j = 0; j < Config.NUMERODENEURONIOSENTRADA; j++){
					//Soma do valor da multiplicacao camada de entrada sobre a escondida
					valorEntradaRede = listaNeuroniosEntrada.get(posicaoAtualLista).getEntradas().get(j).getEntrada();
					
					if(VerificarLimiteNutrientes(valorEntradaRede, listaNeuroniosEntrada.get(posicaoAtualLista).getEntradas().get(j).getNutriente())){
						if(!listaProntuariosAlerta.contains(listaNeuroniosEntrada.get(posicaoAtualLista).getNumProntuario())){
							listaProntuariosAlerta.add(listaNeuroniosEntrada.get(posicaoAtualLista).getNumProntuario());
						}
					}
					
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
				respostaRede[i] = resultadoSomaSaidaTeste[i];
			}
			
			num = listaNeuroniosSaida.get(posicaoAtualLista).getNumProntuario();
			VerificarPossivelDoenca(respostaRede, num);
		}
		
		for(int j = 0; j < listaProntuarioDetaque.size(); j++){
			res += "Prontuário: "+listaProntuarioDetaque.get(j)+"<br />";
		}
		
		return res;
	}

	private void VerificarPossivelDoenca(double[] respostaRede2, int numProntuario) {
		if(respostaRede2[0] < 0 && respostaRede2[1] < 0 && respostaRede2[2] < 0){
			if(listaProntuariosAlerta.contains(numProntuario)){
				if(!listaProntuarioDetaque.contains(numProntuario)){
					listaProntuarioDetaque.add(numProntuario);	
				}
			}
		}
	}

	private Boolean VerificarLimiteNutrientes(Double nutriente, String nomeNutriente) {
		if(nomeNutriente.equals("CT")){
			if(nutriente > 200){
				System.out.println("CT "+nutriente);
				return true;
			}else{
				return false;
			}
		}else if(nomeNutriente.equals("HDL")){
			if(nutriente < 35){
				System.out.println("HDL "+nutriente);
				return true;
			}else{
				return false;
			}
		}else if(nomeNutriente.equals("LDL")){
			if(nutriente > 130){
				System.out.println("LDL "+nutriente);
				return true;
			}else{
				return false;
			}
		}else if(nomeNutriente.equals("G")){
			if(nutriente > 100){
				System.out.println("G "+nutriente);
				return true;
			}else{
				return false;
			}
		}else if(nomeNutriente.equals("TG")){
			if(nutriente > 130){
				System.out.println("TG "+nutriente);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
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
	}

	public void PreencherListaNeuronios(int doenca) {
		int NUMERODECASOS = Config.NUMERODECASOS; // Para cada doenca criara n casos
		String nomeDoenca = null;
		
		List<Entrada> listaValores = new ArrayList<>();
		List<Saida> listaSaidas = new ArrayList<>();
		
		//===================================================================
		// Iniciando pesos do NE e no NS
		//===================================================================
		
	
		if(doenca == 0){
			nomeDoenca = "Saudável";
		}else if(doenca == 1){
			nomeDoenca = "infarto";
		}else if(doenca == 2){
			nomeDoenca = "Hipertigliceridemia";
		}else if(doenca == 3){
			nomeDoenca = "Derrame";
		}else{
			nomeDoenca = "Indefinido";
		}
		
		// Para cada caso de teste
		for (int i = 0; i < NUMERODECASOS; i++) {
			
			Neuronio neuronioEntrada = new Neuronio();
			Neuronio neuronioSaida = new Neuronio();
			
			//===================================================================
			// Iniciando valores de entrada e valores esperados pelo NS
			//===================================================================
		
			// Gerar NUMERODECASOS entrada para 
			listaValores = Utils.InicializarEntradas(doenca); // Saudavel
			// Gerar 5 saidas esperadas
			listaSaidas = Utils.InicializarValoresEsperados(doenca); // Saudavel
			
			//===================================================================
			// Armazenando valores produzidos de forma aleatória
			//===================================================================

			neuronioEntrada.setEntradas(listaValores); // Entradas da rede
			neuronioEntrada.setDoenca(doenca);
			neuronioEntrada.setNomeDoenca(nomeDoenca);
			neuronioEntrada.setCamadaNeuronio(1);
			neuronioEntrada.setStatus("entrada");
			neuronioEntrada.setNumProntuario(numProntuario);

			neuronioSaida.setSaidas(listaSaidas); // Saida esperada
			neuronioSaida.setDoenca(doenca);
			neuronioSaida.setNomeDoenca(nomeDoenca);
			neuronioSaida.setCamadaNeuronio(3);
			neuronioSaida.setStatus("saida");
			neuronioSaida.setNumProntuario(numProntuario);

			//===================================================================
			// Repassando valores as listas que serão trabalhadas no algoritmo
			//===================================================================
			
			listaNeuroniosEntrada.add(neuronioEntrada);
			listaNeuroniosSaida.add(neuronioSaida);
			numProntuario++;
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
	
	private void GerarListaNovaDoenca(){
		PreencherListaNeuronios(0);
		PreencherListaNeuronios(1);
		PreencherListaNeuronios(2);
		PreencherListaNeuronios(3);
		PreencherListaNeuronios(4); //Alto nível de glicose
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
	
	 public void SalvarListaNeuronios(){
		 
		//Gerando as listas de doecas
		GerarListasDoencas();
		 
		 for (Neuronio n : listaNeuroniosEntrada) {
				for(Entrada v : n.getEntradas()) {
					bancoValor.save(v);
				}
				bancoNeuronio.save(n);
			}
			
			for (Neuronio n : listaNeuroniosSaida) {
				for(Saida s : n.getSaidas()) {
					bancoSaida.save(s);
				}
				bancoNeuronio.save(n);
			}
	 }
}
