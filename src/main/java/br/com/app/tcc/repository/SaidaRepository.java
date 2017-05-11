package br.com.app.tcc.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import br.com.app.tcc.model.Saida;

public interface SaidaRepository extends CrudRepository<Saida, Integer>{
	
	List<Saida> findAll();


}
