
package it.uniroma3.siw.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.IngredientePerRicettaRepository;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.validator.IngredienteValidator;
import jakarta.validation.Valid;

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
	@Autowired
	private IngredienteValidator ingredienteValidator;
	
	
	@GetMapping("/cuoco/sezioneIngredienti")
	public String getpaginaCuochi() {
		return "/cuoco/sezioneIngredienti.html";
		}
	@GetMapping("/cuoco/visualizzaIngrediente")
	public String visualizzaIngrediente(Model model) {		
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "/cuoco/visualizzaIngrediente.html";
		}
	@GetMapping("/admin/sezioneIngredienti")
	public String visualizzaAdminIngrediente(Model model) {		
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "/admin/sezioneIngredienti.html";
		}
	@GetMapping("/cuoco/aggiungiIngrediente")
	public String aggiungiIngrediente(Model model) {		
		model.addAttribute("ingrediente", new Ingrediente());
		return "/cuoco/aggiungiIngrediente.html";}
	
	@PostMapping("cuoco/nuovoIngrediente")
	public String nuovaIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente,BindingResult bindingResult, Model model) {
		this.ingredienteValidator.validate(ingrediente, bindingResult);
		if (!bindingResult.hasErrors()) {
		this.ingredienteService.save(ingrediente);
		
		return "cuoco/indexCuoco";}
		else {
			return "cuoco/aggiungiIngrediente";
		}
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
	public String IngredientePerRicetta(@Valid @ModelAttribute("ingredientePerRicetta") IngredientePerRicetta ingredientePerRicetta,BindingResult bindingResult,@PathVariable("ricettaid") Long ricettaid,@PathVariable("ingredienteid") Long ingredienteid, Model model) {
		if (!bindingResult.hasErrors()) {
		Ingrediente ingrediente=this.ingredienteService.findById(ingredienteid);
		Ricetta ricetta=this.ricettaService.findById(ricettaid);
		ingredientePerRicetta.setIngrediente(ingrediente);
		List<IngredientePerRicetta> ingredientiPerRicetta = ricetta.getIngredienteRicetta();
		ingredientiPerRicetta.add(ingredientePerRicetta);
		this.ingredientePerRicettaRepository.save(ingredientePerRicetta);
		return "/cuoco/indexCuoco.html";}else {
			model.addAttribute("ingrediente", this.ingredienteService.findById(ingredienteid));
			model.addAttribute("ricetta",this.ricettaService.findById(ricettaid));
			return "/cuoco/aggiungiIngredientePerRicetta.html";
		}
	}
	@GetMapping(value="/admin/cancellaIngrediente/{id}")						//da fare quando viene cancellato un ingrediente devono essere cancellatte tutte le ricette con quel ingrediente!!!!!!!
	public String cancellaIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente=this.ingredienteService.findById(id);
		this.ingredienteRepository.delete(ingrediente);
		return "admin/indexAdmin.html";
	}
	@GetMapping("/admin/aggiungiIngrediente")
	public String adminAggiungiIngrediente(Model model) {		
		model.addAttribute("ingrediente", new Ingrediente());
		return "/cuoco/aggiungiIngrediente.html";}

@GetMapping("/admin/aggiornaIngrediente/{id}")
public String adminAggiungiIngrediente(@PathVariable("id") Long id,Model model) {		
	model.addAttribute("ingrediente", this.ingredienteRepository.findById(id).get());
	return "/admin/aggiornaIngrediente.html";}

@PostMapping("/admin/aggiornaIngrediente")
public String adminAggiungiIngrediente2(@Valid @RequestParam("id") Long id,BindingResult bindingResult,@RequestParam("nuovoNome") String nuovoNome,Model model) {		
	Ingrediente ingrediente = this.ingredienteRepository.findById(id).get();
	ingrediente.setNome(nuovoNome);
	this.ingredienteRepository.save(ingrediente);
	return "/admin/aggiornaIngrediente.html";}
}



