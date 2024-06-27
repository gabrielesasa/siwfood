
package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.CuocoRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.RicettaService;


@Controller
public class RicettaController {
	@Autowired
	private RicettaRepository ricettaRepository;
	@Autowired
	private RicettaService ricettaService;
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private CuocoService cuocoService;
	@Autowired
	private CuocoRepository cuocoRepository;
	@Autowired 
	private CredentialsService credentialsService;
	@GetMapping("/generico/paginaricette")
	public String getRicette(Model model) {		
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "/generico/paginaricette.html";
		}
	@GetMapping("/admin/sezioneRicette")
	public String getAdminRicette(Model model) {		
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "/admin/sezioneRicette.html";
		}
	@GetMapping("/generico/ricetta/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta=this.ricettaService.findById(id);
		List<IngredientePerRicetta> ingredienteRicetta=ricetta.getIngredienteRicetta();
		if(!ingredienteRicetta.isEmpty()) {
		IngredientePerRicetta ingre=ingredienteRicetta.get(0);									
		Ingrediente in=ingre.getIngrediente();
		Long idingrediente=in.getId();
		model.addAttribute("ingre", this.ingredienteService.findById(idingrediente));}
		model.addAttribute("ricetta", this.ricettaService.findById(id));
		return "/generico/ricetta";																		
	}
	@PostMapping("cuoco/nuovaRicetta")
	public String nuovaRicetta(@ModelAttribute("ricetta") Ricetta ricetta, Model model) {
		this.ricettaRepository.save(ricetta);
		return "/admin/indexAdmin";
	}
	@GetMapping("/cuoco/aggiungiRicetta")
	public String formnuovaRicetta(Model model) {
		model.addAttribute("ricetta", new Ricetta());
		model.addAttribute("cuochi",this.cuocoRepository.findAll());
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated()) {
	        for (GrantedAuthority auth : authentication.getAuthorities()) {
	            System.out.println("Current user role: " + auth.getAuthority());
	        }
	    }
	    return "/cuoco/aggiungiRicetta.html";
	}
	@GetMapping("/cuoco/setCuocoRicetta/{cuocoId}/{ricettaId}")
	public String setCuocoRicetta(@PathVariable("cuocoId") Long cuocoId, @PathVariable("ricettaId") Long ricettaId, Model model) {
		
		Cuoco cuoco = this.cuocoRepository.findById(cuocoId).get();
		Ricetta ricetta = this.ricettaRepository.findById(ricettaId).get();
		ricetta.setCuoco(cuoco);
		this.ricettaRepository.save(ricetta);
		model.addAttribute("cuochi", this.cuocoRepository.findAll());
		model.addAttribute("ricetta", this.ricettaRepository.findById(ricettaId).get());
		return "cuoco/cuocoDaAggiungere.html";
	}
	
	
	@GetMapping("cuoco/aggiungiCuocoRicetta/{ricettaid}")
	public String aggiungiCuocoRicetta(@PathVariable("ricettaid") Long id, Model model) {
		model.addAttribute("cuochi", this.cuocoRepository.findAll());
		model.addAttribute("ricetta", this.ricettaRepository.findById(id).get());
		return "cuoco/cuocoDaAggiungere.html";
	}
	@GetMapping("cuoco/aggiungiIngredienteRicetta")
	public String aggiungiRicetta(Model model) {
		model.addAttribute("ricette", this.ricettaRepository.findAll());
	    
	    return "cuoco/aggiungiIngredienteRicetta.html";
	}
	@GetMapping("/cuoco/AggiornaRicette")
	public String AggiornaRicette(Model model) {
		model.addAttribute("ricette",this.ricettaRepository.findAll());
	    return "cuoco/aggiornaRicette.html";
	}
	@GetMapping("/cuoco/cancellaRicetta")
	public String CancellaRicette(Model model) {
		model.addAttribute("ricette",this.ricettaRepository.findAll());
	    return "cuoco/cancellaRicetta.html";
	}
	@GetMapping("cuoco/formAggiornaRicetta/{ricettaid}")
	public String formAggiornaRicetta(@PathVariable("ricettaid") Long id, Model model) {
		model.addAttribute("cuochi", cuocoRepository.findAll());
		model.addAttribute("ricetta", ricettaRepository.findById(id).get());
		return "cuoco/formAggiornaRicetta.html";
	}
	@GetMapping("cuoco/CancellaRicetta/{ricettaid}")
	public String formCancellaRicetta(@PathVariable("ricettaid") Long id, Model model) {			//aggiungere metodo cascate
		Ricetta ricetta=this.ricettaRepository.findById(id).get();
		this.ricettaRepository.delete(ricetta);
		return "cuoco/cancellaRicetta.html";
	}
	
	@PostMapping("cuoco/aggiornaRicetta/{id}")
	public String formAggiornaNomeRicetta(@PathVariable("id") Long id, @RequestParam("nuovaDescrizione") String nuovaDescrizione,@RequestParam("nuovaImmagine") String nuovaImmagine, Model model) {
		Ricetta ricetta=this.ricettaRepository.findById(id).get();
		ricetta.setDescrizione(nuovaDescrizione);
		ricetta.setImmagine(nuovaImmagine);
		this.ricettaRepository.save(ricetta);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "/generico/index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			}
			else if(credentials.getRole().equals(Credentials.CUOCO_ROLE)) {
                return "cuoco/indexCuoco.html";
    	}
		}
        return "/generico/index.html";
	}
	@GetMapping(value="/admin/cancellaRicetta/{id}")
	public String cancellaCuoco(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta=this.ricettaService.findById(id);
		this.ricettaRepository.delete(ricetta);
		return "admin/indexAdmin.html";
	}
	@GetMapping("/admin/aggiungiRicetta")
	public String adminNuovaRicetta(Model model) {
		model.addAttribute("ricetta", new Ricetta());
		model.addAttribute("cuochi",this.cuocoRepository.findAll());
	    return "cuoco/aggiungiRicetta.html";
	}
	@GetMapping("admin/aggiornaRicetta/{ricettaid}")
	public String adminFormAggiornaRicetta(@PathVariable("ricettaid") Long id, Model model) {
		model.addAttribute("cuochi", cuocoRepository.findAll());
		model.addAttribute("ricetta", ricettaRepository.findById(id).get());
		return "cuoco/formAggiornaRicetta.html";
	}
}

