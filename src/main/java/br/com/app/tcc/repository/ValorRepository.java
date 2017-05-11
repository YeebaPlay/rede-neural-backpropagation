package br.com.app.tcc.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import br.com.app.tcc.model.Entrada;

public interface ValorRepository extends CrudRepository<Entrada, Integer>{
	
	List<Entrada> findAll();


}
