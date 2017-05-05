package br.com.app.tcc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.app.tcc.model.Neuronio;
import br.com.app.tcc.model.Saida;
import br.com.app.tcc.model.Valor;

public interface SaidaRepository extends CrudRepository<Saida, Integer>{
	
	List<Saida> findAll();


}
