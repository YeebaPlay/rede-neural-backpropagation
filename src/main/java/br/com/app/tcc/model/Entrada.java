package br.com.app.tcc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="Entrada")
public class Entrada {
	
	public Entrada() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (name="entrada")
	private Double entrada; //Valor esperado da rede
	
	@Column (name="nutriente")
	private String nutriente; //Valor esperado da rede

	public Entrada(Double valor, String nutriente) {
		this.entrada = valor;
		this.nutriente = nutriente;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getEntrada() {
		return entrada;
	}

	public void setEntrada(Double entrada) {
		this.entrada = entrada;
	}

	public String getNutriente() {
		return nutriente;
	}

	public void setNutriente(String nutriente) {
		this.nutriente = nutriente;
	}
	
	
	
}
