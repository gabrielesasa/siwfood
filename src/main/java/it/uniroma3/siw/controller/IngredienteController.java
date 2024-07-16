package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.IngredientePerRicettaService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.validator.IngredienteValidator;
import jakarta.validation.Valid;

@Controller
public class IngredienteController {
    @Autowired
    private IngredientePerRicettaService ingredientePerRicettaService;

    @Autowired
    private IngredienteService ingredienteService;
    @Autowired
    private RicettaService ricettaService;
    @Autowired
    private IngredienteValidator ingredienteValidator;
    @Autowired 
    private CredentialsService credentialsService;
    @Autowired
    GlobalController globalController;

    @GetMapping("/cuoco/sezioneIngredienti")
    public String getpaginaCuochi() {
        return "/cuoco/sezioneIngredienti.html";
    }

    @GetMapping("/generico/visualizzaIngredienteRicetta/{id}")
    public String getpaginaIngre(@PathVariable("id") Long id, Model model) {
        Ricetta ricetta = this.ricettaService.findById(id);
        List<IngredientePerRicetta> ingredienteRicetta = this.ingredientePerRicettaService.findRicettaIngredientiByRicetta(ricetta.getId());
        model.addAttribute("ingredienti", ingredienteRicetta);
        return "generico/visualizzaIngredienteRicetta.html";
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
        return "/cuoco/aggiungiIngrediente.html";
    }

    @PostMapping("cuoco/nuovoIngrediente")
    public String nuovaIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        this.ingredienteValidator.validate(ingrediente, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.ingredienteService.save(ingrediente);
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "admin/indexAdmin.html";
            } else if (credentials.getRole().equals(Credentials.CUOCO_ROLE)) {
                return "cuoco/indexCuoco.html";
            }
        } else {
            return "cuoco/aggiungiIngrediente";
        }
        return "cuoco/aggiungiIngrediente";
    }

    @GetMapping("cuoco/aggiungiIngredienteRicetta/{ricettaid}")
    public String aggiungiIngredienteRicetta(@PathVariable("ricettaid") Long id, Model model) {
        model.addAttribute("ingredienti", this.ingredienteService.findAll());
        model.addAttribute("ricetta", this.ricettaService.findById(id));
        return "cuoco/setIngredientiRicetta.html";
    }

    @GetMapping("cuoco/aggiungiIngredienteRicetta/{ricettaid}/{ingredienteid}")
    public String aggiungiIngredienteRicetta2(@ModelAttribute("ingredientePerRicetta") IngredientePerRicetta ingredientePerRicetta, @PathVariable("ricettaid") Long ricettaid, @PathVariable("ingredienteid") Long ingredienteid, Model model) {
        model.addAttribute("ingrediente", this.ingredienteService.findById(ingredienteid));
        model.addAttribute("ricetta", this.ricettaService.findById(ricettaid));
        model.addAttribute("ingredientePerRicetta", new IngredientePerRicetta());
        return "cuoco/aggiungiIngredientePerRicetta.html";
    }

    @PostMapping("cuoco/aggiungiIngredienteRicetta/{ricettaid}/{ingredienteid}")
    public String IngredientePerRicetta(@Valid @ModelAttribute("ingredientePerRicetta") IngredientePerRicetta ingredientePerRicetta, BindingResult bindingResult, @PathVariable("ricettaid") Long ricettaid, @PathVariable("ingredienteid") Long ingredienteid, Model model) {
        if (!bindingResult.hasErrors()) {
            this.ingredienteService.aggiungiI(ingredientePerRicetta, ricettaid, ingredienteid);
            return "/cuoco/indexCuoco.html";
        } else {
            model.addAttribute("ingrediente", this.ingredienteService.findById(ingredienteid));
            model.addAttribute("ricetta", this.ricettaService.findById(ricettaid));
            return "/cuoco/aggiungiIngredientePerRicetta.html";
        }
    }

    @GetMapping(value = "/admin/cancellaIngrediente/{id}")
    public String cancellaIngrediente(@PathVariable("id") Long id, Model model) {
        Ingrediente ingrediente = this.ingredienteService.findById(id);
        this.ingredienteService.delete(ingrediente);
        return "admin/indexAdmin.html";
    }

    @GetMapping("/admin/aggiungiIngrediente")
    public String adminAggiungiIngrediente(Model model) {        
        model.addAttribute("ingrediente", new Ingrediente());
        return "/cuoco/aggiungiIngrediente.html";
    }

    @GetMapping("/admin/aggiornaIngrediente/{id}")
    public String adminAggiungiIngrediente(@PathVariable("id") Long id, Model model) {        
        model.addAttribute("ingrediente", this.ingredienteService.findById(id));
        return "/admin/aggiornaIngrediente.html";
    }

    @PostMapping("/admin/aggiornaIngrediente")
    public String adminAggiungiIngrediente2(@RequestParam("id") Long id, @RequestParam("nuovoNome") String nuovoNome, Model model) {        
        this.ingredienteService.aggiungiIngre(id, nuovoNome);
        return "/admin/indexAdmin.html";
    }
}
