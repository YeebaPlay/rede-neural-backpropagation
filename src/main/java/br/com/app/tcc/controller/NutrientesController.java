package br.com.app.tcc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.app.tcc.model.Nutriente;
import br.com.app.tcc.repository.NutrientesRepository;
import br.com.app.tcc.utils.*;

@RestController
@RequestMapping(Urls.NUTRIENTES)
public class NutrientesController {

	@Autowired
	private NutrientesRepository nutrientesRepository;
	TreinamentoController treinamento = new TreinamentoController();
	
	@RequestMapping(value="/find/by/{id}", method = RequestMethod.GET)
	public List<Nutriente> findByNutrientes(@PathVariable("id") Integer id) {
		return nutrientesRepository.findByNutriente(id);
	}
	
	@RequestMapping(value="/treinamento", method = RequestMethod.GET)
	public String TreinarRede() {
		String resultado = treinamento.TreinamentoRede();
		return resultado;
	}
	
	@RequestMapping(value="/testerede", method = RequestMethod.GET)
	public String MostrarMensagem() {
		return "";
	}
}
