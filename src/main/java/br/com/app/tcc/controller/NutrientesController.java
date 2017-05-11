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
	
	@Autowired
	TreinamentoController treinamento;
	
	@RequestMapping(value="/treinamento", method = RequestMethod.GET)
	public String TreinarRede() {
		String resultado = treinamento.TreinamentoRede(0);
		return resultado;
	}
	
	@RequestMapping(value="/gerar-valores", method = RequestMethod.GET)
	public String MostrarMensagem() {
		treinamento.SalvarListaNeuronios();
		return "Criado com sucesso!!";
	}
	
	@RequestMapping(value="/encontrar-nova-doenca", method = RequestMethod.GET)
	public String EncontrarNovaDoenca() {
		String resultado = treinamento.TreinamentoRede(1);
		return resultado;
	}
}
