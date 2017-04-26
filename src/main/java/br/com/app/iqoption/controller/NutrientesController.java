package br.com.app.iqoption.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import br.com.app.iqoption.model.Nutriente;
import br.com.app.iqoption.repository.NutrientesRepository;
import br.com.app.iqoption.utils.*;

@RestController
@RequestMapping(Urls.NUTRIENTES)
public class NutrientesController {

	@Autowired
	private NutrientesRepository nutrientesRepository;
	TreinamentoController treinamento = null;
	
	@RequestMapping(value="/find/by/{id}", method = RequestMethod.GET)
	public List<Nutriente> findByNutrientes(@PathVariable("id") Integer id) {
		return nutrientesRepository.findByNutriente(id);
	}
	
	@RequestMapping(value="/treinamento", method = RequestMethod.GET)
	public String TreinarRede() {
		treinamento = new TreinamentoController();
		treinamento.TreinamentoRede();
		return "Treinado com sucesso!!";
	}
	
	@RequestMapping(value="/funcionando", method = RequestMethod.GET)
	public String MostrarMensagem() {
		return "WebService funcionando...";
	}
}
