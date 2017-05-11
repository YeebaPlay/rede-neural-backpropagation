package br.com.app.tcc.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name ="Neuronio")
public class Neuronio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="num_prontuario")
//	  @JoinTable(name="neuronio_valores",
//	             joinColumns={@JoinColumn(name="id_neuronio")},
//	             inverseJoinColumns={@JoinColumn(name="id_valor")})
	  private List<Entrada> entradas;


	@Column (name="doenca")
	private int doenca; //qual doenca o neuronio esta treinando
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="num_prontuario")
//	  @JoinTable(name="neuronio_saida",
//	             joinColumns={@JoinColumn(name="id_neuronio_saida")},
//	             inverseJoinColumns={@JoinColumn(name="id_saida")})
	private List<Saida> saidas; //Todas as saídas de um processamento
	
	
	@Column (name="nome_doenca")
	private String nomeDoenca;
	
	@Column (name="status")
	private String status;
	
	//Mostrar se ele é entrada, escondida ou saída
	@Column (name="camada_neuronio")
	private int camadaNeuronio;
	
	//Numero do prontuario
	@Column (name="num_prontuario")
	private int numProntuario;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Entrada> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<Entrada> entradas) {
		this.entradas = entradas;
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
}
