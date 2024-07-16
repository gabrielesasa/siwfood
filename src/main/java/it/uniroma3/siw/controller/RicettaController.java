package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CuocoRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.validator.RicettaValidator;
import jakarta.validation.Valid;

@Controller
public class RicettaController {

    @Autowired
    private RicettaService ricettaService;
    @Autowired
    private IngredienteService ingredienteService;
    @Autowired
    private CuocoService cuocoService;
    @Autowired 
    private CredentialsService credentialsService;
    @Autowired
    private RicettaValidator ricettaValidator;
    @Autowired
    GlobalController globalController;

    @GetMapping("/generico/paginaricette")
    public String getRicette(Model model) {		
        model.addAttribute("ricette", this.ricettaService.findAll());
        return "/generico/paginaricette.html";
    }

    @GetMapping("/generico/paginaricette2")
    public String getRicette2(Model model) {		
        Credentials credenziali = credentialsService.getCredentials(globalController.getUser());
        User utente = credenziali.getUser();
        Cuoco cuoco = this.cuocoService.findByUser(utente);
        model.addAttribute("ricette", this.ricettaService.findRicetteByCuoco(cuoco));
        return "/generico/paginaricette.html";
    }

    @GetMapping("/admin/sezioneRicette")
    public String getAdminRicette(Model model) {		
        model.addAttribute("ricette", this.ricettaService.findAll());
        return "/admin/sezioneRicette.html";
    }

	@GetMapping("/generico/ricetta/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model) {
		if (this.ricettaService.getIdIngrediente(id) != null)
			model.addAttribute("ingre", this.ingredienteService.findById(this.ricettaService.getIdIngrediente(id)));
		model.addAttribute("ricetta", this.ricettaService.findById(id));
		return "/generico/ricetta";
	}

    @GetMapping("/cuoco/sezioneRicette")
    public String getpaginaCuochi() {
        return "/cuoco/sezioneRicette.html";
    }

    @PostMapping("cuoco/nuovaRicetta")
    public String nuovaRicetta(@Valid @ModelAttribute("ricetta") Ricetta ricetta, BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {
        Credentials credenziali = credentialsService.getCredentials(globalController.getUser());
        User utente = credenziali.getUser();
        this.ricettaValidator.validate(ricetta, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.ricettaService.nuovaRicetta(ricetta, utente, imageFile);
            model.addAttribute("cuochi", this.cuocoService.findAll());
            if (credenziali.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "admin/indexAdmin.html";
            } else {
                return "cuoco/indexCuoco.html";
            }
        } else {
            return "/cuoco/aggiungiRicetta";
        }
    }

    @GetMapping("/cuoco/aggiungiRicetta")
    public String formnuovaRicetta(Model model) {
        model.addAttribute("ricetta", new Ricetta());
        model.addAttribute("cuochi", this.cuocoService.findAll());
        return "/cuoco/aggiungiRicetta.html";
    }

    @GetMapping("/cuoco/setCuocoRicetta/{cuocoId}/{ricettaId}")
    public String setCuocoRicetta(@PathVariable("cuocoId") Long cuocoId, @PathVariable("ricettaId") Long ricettaId, Model model) {
        this.ricettaService.ricettasetCuocoRicetta(cuocoId, ricettaId);
        model.addAttribute("cuochi", this.cuocoService.findAll());
        model.addAttribute("ricetta", this.ricettaService.findById(ricettaId));
        return "cuoco/cuocoDaAggiungere.html";
    }

	

    @GetMapping("cuoco/aggiungiCuocoRicetta/{ricettaid}")
    public String aggiungiCuocoRicetta(@PathVariable("ricettaid") Long id, Model model) {
        model.addAttribute("cuochi", this.cuocoService.findAll());
        model.addAttribute("ricetta", this.ricettaService.findById(id));
        return "cuoco/cuocoDaAggiungere.html";
    }

    @GetMapping("cuoco/aggiungiIngredienteRicetta")
    public String aggiungiRicetta(Model model) {
        Credentials credenziali = credentialsService.getCredentials(globalController.getUser());
        User utente = credenziali.getUser();
        Cuoco cuoco = this.cuocoService.findByUser(utente);
        model.addAttribute("ricette", this.ricettaService.findRicetteByCuoco(cuoco));
        return "cuoco/aggiungiIngredienteRicetta.html";
    }

    @GetMapping("/cuoco/sezioneRicette2")
    public String AggiornaRicette(Model model) {
        Credentials credenziali = credentialsService.getCredentials(globalController.getUser());
        User utente = credenziali.getUser();
        Cuoco cuoco = this.cuocoService.findByUser(utente);
        model.addAttribute("ricette", this.ricettaService.findRicetteByCuoco(cuoco));
        return "cuoco/sezioneRicette2.html";
    }

    @GetMapping("/cuoco/cancellaRicetta")
    public String CancellaRicette(Model model) {
        Credentials credenziali = credentialsService.getCredentials(globalController.getUser());
        User utente = credenziali.getUser();
        Cuoco cuoco = this.cuocoService.findByUser(utente);
        model.addAttribute("ricette", this.ricettaService.findRicetteByCuoco(cuoco));
        return "cuoco/cancellaRicetta.html";
    }

    @GetMapping("cuoco/formAggiornaRicetta/{ricettaid}")
    public String formAggiornaRicetta(@PathVariable("ricettaid") Long id, Model model) {
        model.addAttribute("cuochi", this.cuocoService.findAll());
        model.addAttribute("ricetta", ricettaService.findById(id));
        return "cuoco/formAggiornaRicetta.html";
    }

    @GetMapping("cuoco/cancellaRicetta/{ricettaid}")
    public String formCancellaRicetta(@PathVariable("ricettaid") Long id, Model model) {
        // aggiungere metodo cascate
        Ricetta ricetta = this.ricettaService.findById(id);
        this.ricettaService.delete(ricetta);
        return "cuoco/indexCuoco.html";
    }

    @PostMapping("cuoco/aggiornaRicetta/{id}")
    public String formAggiornaNomeRicetta(@Valid @PathVariable("id") Long id, @ModelAttribute("ricetta") Ricetta ricetta2, @RequestParam("nuovaDescrizione") String nuovaDescrizione, @RequestParam("nuovaImmagine") String nuovaImmagine, BindingResult bindingResult, Model model) {
        this.ricettaService.aggiornaRicetta(id, nuovaDescrizione, nuovaImmagine);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "/generico/index.html";
        } else {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "admin/indexAdmin.html";
            } else if (credentials.getRole().equals(Credentials.CUOCO_ROLE)) {
                return "cuoco/indexCuoco.html";
            }
        }
        return "/generico/index.html";
    }

	
    @GetMapping(value = "/admin/cancellaRicetta/{id}")
    public String cancellaCuoco(@PathVariable("id") Long id, Model model) {
        Ricetta ricetta = this.ricettaService.findById(id);
        this.ricettaService.delete(ricetta);
        return "admin/indexAdmin.html";
    }

    @GetMapping("/admin/aggiungiRicetta")
    public String adminNuovaRicetta(Model model) {
        model.addAttribute("ricetta", new Ricetta());
        model.addAttribute("cuochi", this.cuocoService.findAll());
        return "cuoco/aggiungiRicetta.html";
    }

    @GetMapping("admin/aggiornaRicetta/{ricettaid}")
    public String adminFormAggiornaRicetta(@PathVariable("ricettaid") Long id, Model model) {
        model.addAttribute("cuochi", cuocoService.findAll());
        model.addAttribute("ricetta", ricettaService.findById(id));
        return "cuoco/formAggiornaRicetta.html";
    }
}

/* 
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (authentication != null && authentication.isAuthenticated()) {
    for (GrantedAuthority auth : authentication.getAuthorities()) {
        System.out.println("Current user role: " + auth.getAuthority());
    }
}
*/
