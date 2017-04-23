package br.com.app.iqoption.controller;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.app.iqoption.model.Nutriente;
import br.com.app.iqoption.repository.NutrientesRepository;
import br.com.app.iqoption.utils.*;

@RestController
@RequestMapping(Urls.NUTRIENTES)
public class NutrientesController {

	private NutrientesRepository nutrientesRepository;
	
	@RequestMapping(value="/find/by/{id}")
	public List<Nutriente> findByNutrientes(@PathVariable("id") Integer id) {
		return nutrientesRepository.findByNutriente(id);
	}
	
	@RequestMapping(value="/treinamento")
	public String findByLocacao() {
		TreinamentoController treinamento = new TreinamentoController();
		treinamento.TreinamentoRede();
		return "Treinado com sucesso!!";
	}
}
