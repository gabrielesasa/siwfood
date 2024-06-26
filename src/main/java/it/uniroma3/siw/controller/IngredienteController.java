
package it.uniroma3.siw.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.IngredientePerRicettaRepository;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.RicettaService;

@Controller
public class IngredienteController {
	@Autowired
	private IngredienteRepository ingredienteRepository;
	@Autowired
	private IngredientePerRicettaRepository ingredientePerRicettaRepository;
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private RicettaService ricettaService;
	
	@GetMapping("/cuoco/ingredienti")
	public String getIngredienti() {		
		return "/cuoco/ingredienti.html";
		}
	@GetMapping("/cuoco/visualizzaIngrediente")
	public String visualizzaIngrediente(Model model) {		
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "/cuoco/visualizzaIngrediente.html";
		}
	@GetMapping("/cuoco/aggiungiIngrediente")
	public String aggiungiIngrediente(Model model) {		
		model.addAttribute("ingrediente", new Ingrediente());
		return "/cuoco/aggiungiIngrediente.html";}
	
	@PostMapping("cuoco/nuovoIngrediente")
	public String nuovaIngrediente(@ModelAttribute("ingrediente") Ingrediente ingrediente, Model model) {
		this.ingredienteService.save(ingrediente);
		model.addAttribute("ingrediente", new Ingrediente());
		return "cuoco/aggiungiIngrediente";
}
	@GetMapping("cuoco/aggiungiIngredienteRicetta/{ricettaid}")
	public String aggiungiIngredienteRicetta(@PathVariable("ricettaid") Long id, Model model) {
		model.addAttribute("ingredienti", this.ingredienteRepository.findAll());
		model.addAttribute("ricetta",this.ricettaService.findById(id));
		return "cuoco/setIngredientiRicetta.html";
	}
	@GetMapping("cuoco/aggiungiIngredienteRicetta/{ricettaid}/{ingredienteid}")
	public String aggiungiIngredienteRicetta2(@ModelAttribute("ingredientePerRicetta") IngredientePerRicetta ingredientePerRicetta,@PathVariable("ricettaid") Long ricettaid,@PathVariable("ingredienteid") Long ingredienteid, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(ingredienteid));
		model.addAttribute("ricetta",this.ricettaService.findById(ricettaid));
		model.addAttribute("ingredientePerRicetta", new IngredientePerRicetta());
		return "cuoco/aggiungiIngredientePerRicetta.html";
	}
	@PostMapping("cuoco/aggiungiIngredienteRicetta/{ricettaid}/{ingredienteid}")
	public String IngredientePerRicetta(@ModelAttribute("ingredientePerRicetta") IngredientePerRicetta ingredientePerRicetta,@PathVariable("ricettaid") Long ricettaid,@PathVariable("ingredienteid") Long ingredienteid, Model model) {
		Ingrediente ingrediente=this.ingredienteService.findById(ingredienteid);
		Ricetta ricetta=this.ricettaService.findById(ricettaid);
		ingredientePerRicetta.setIngrediente(ingrediente);
		List<IngredientePerRicetta> ingredientiPerRicetta = ricetta.getIngredienteRicetta();
		ingredientiPerRicetta.add(ingredientePerRicetta);
		this.ingredientePerRicettaRepository.save(ingredientePerRicetta);
		return "/cuoco/indexCuoco.html";
	}
}


