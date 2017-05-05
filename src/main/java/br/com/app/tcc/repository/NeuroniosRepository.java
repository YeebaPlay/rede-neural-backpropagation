package br.com.app.tcc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.app.tcc.model.Neuronio;

public interface NeuroniosRepository extends CrudRepository<Neuronio, Integer>{
	
	List<Neuronio> findAll();

	@Query("SELECT a FROM #{#entityName} a WHERE a.id = :id")
	List<Neuronio> findByNeuronio(@Param("id") Integer id);
	
	@Query(nativeQuery = true, value = "INSERT INTO Neuronios (nome_doenca, doenca, num_prontuario, camada_neuronio) VALUES (?, ?, ?, 1)")
	void SalvarEntradas(double entrada, String nomeDoenca, int numDoenca, int numProntuario);
	
	@Query(nativeQuery = true, value = "INSERT INTO Neuronios (entradas, nome_doenca, doenca, num_prontuario, camada_neuronio) VALUES (?, ?, ?, 2)")
	void SalvarEscondida(String nomeDoenca, int numDoenca, int numProntuario);
	
	@Query(nativeQuery = true, value = "INSERT INTO Neuronios (valor_esperado, doenca, nome_doenca, num_prontuario, camada_neuronio) VALUES (?, ?, ?, ?, 3)")
	void SalvarSaidas(double valorEsperado, int numDoenca, String nomeDoenca, int numProntuario);
	
}
