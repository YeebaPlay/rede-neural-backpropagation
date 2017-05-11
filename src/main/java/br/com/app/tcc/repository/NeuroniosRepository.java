package br.com.app.tcc.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.app.tcc.model.Neuronio;

public interface NeuroniosRepository extends CrudRepository<Neuronio, Integer>{
	
	List<Neuronio> findAll();

	@Query("SELECT a FROM #{#entityName} a WHERE a.status = :status ORDER BY a.numProntuario")
	ArrayList<Neuronio> findByNeuronio(@Param("status") String status);		
}
