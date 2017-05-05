package br.com.app.tcc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.junit.Ignore;

@Entity
@Table(name ="Neuronio")
public class Neuronio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToMany(cascade = CascadeType.ALL)
	  @JoinTable(name="neuronio_valores",
	             joinColumns={@JoinColumn(name="id_neuronio")},
	             inverseJoinColumns={@JoinColumn(name="id_valor")})
	  private List<Valor> valores;


	@Column (name="doenca")
	private int doenca; //qual doenca o neuronio esta treinando
	
	@ManyToMany(cascade = CascadeType.ALL)
	  @JoinTable(name="neuronio_saida",
	             joinColumns={@JoinColumn(name="id_neuronio_saida")},
	             inverseJoinColumns={@JoinColumn(name="id_saida")})
	private List<Saida> saidas; //Todas as saídas de um processamento
	
	
	@Column (name="nome_doenca")
	private String nomeDoenca;
	
	//Mostrar se ele é entrada, escondida ou saída
	@Column (name="camada_neuronio")
	private int camadaNeuronio;
	
	//Numero do prontuario
	@Column (name="num_prontuario")
	private int numProntuario;
	
	
	public List<Valor> getValores() {
		return valores;
	}

	public void setValores(List<Valor> valores) {
		this.valores = valores;
	}

	public int getNumProntuario() {
		return numProntuario;
	}

	public void setNumProntuario(int numProntuario) {
		this.numProntuario = numProntuario;
	}

	public int getCamadaNeuronio() {
		return camadaNeuronio;
	}

	public void setCamadaNeuronio(int camadaNeuronio) {
		this.camadaNeuronio = camadaNeuronio;
	}

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


	public int getDoenca() {
		return doenca;
	}

	public void setDoenca(int doenca) {
		this.doenca = doenca;
	}

	 public List<Saida> getSaidas() {
		return saidas;
	}

	public void setSaidas(List<Saida> saidas) {
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
    public List<Valor> InicializarEntradas(int doenca){
   
		List<Valor> listaValores = new ArrayList<>();
    	//saudavel = 0
    	//infarto = 1
    	//hipertigliceridemia = 2
    	//derrame = 3
    	
    
    	if(doenca == 0){ //saudavel
    		//CT ruim acima de 200
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(100, 200)));
        	//HDL maior que 35
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(35, 120)));
        	//LDL limite de 130
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(50, 130)));
        	//glicose limite de 100
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(30, 100)));
        	//TG limite de 130
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(50, 130)));
    	}else if(doenca == 1){ //infarto
    		//CT ruim acima de 200
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(201, 280) * 10)); //201, 280
        	//HDL maior que 35
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(35, 120)));
        	//LDL limite de 130
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(50, 130)));
        	//glicose limite de 100
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(30, 100)));
        	//TG limite de 130
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(50, 130)));
    	}else if(doenca == 2){ //hipertigliceridemia
    		//CT ruim acima de 200
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(100, 200)));
        	//HDL maior que 35
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(35, 120)));
        	//LDL limite de 130
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(50, 130)));
        	//glicose limite de 100
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(30, 100)));
        	//TG limite de 130
    		listaValores.add(new Valor((double)  GerarValoresIntervaloNutriente(131, 160) * 10)); //131, 160
    	}else if(doenca == 3){//derrame
    		//CT ruim acima de 200
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(100, 200)));
        	//HDL maior que 35
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(35, 120)));
        	//LDL limite de 130
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(131, 180) * 10)); //131, 180
        	//glicose limite de 100
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(30, 100)));
        	//TG limite de 130
    		listaValores.add(new Valor((double) GerarValoresIntervaloNutriente(50, 130)));
    	}
    	
    	listaValores.add(new Valor((double) GerarNumeroAleatorio())); //Bias
    	return listaValores;
    }
    
    public List<Saida> InicializarValoresEsperados(int doenca){
    	
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
    	}
    	return listaValores;
    }
   
}
