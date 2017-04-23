package br.com.app.iqoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.app.iqoption.model.Nutriente;

public interface NutrientesRepository extends JpaRepository<Nutriente, Integer>{
	
	List<Nutriente> findAll();

	@Query("SELECT a FROM #{#entityName} WHERE id = :id")
	List<Nutriente> findByNutriente(@Param("id") Integer id);
	
}
