
package it.uniroma3.siw.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.CuocoRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.validator.CuocoValidator;
import jakarta.validation.Valid;

@Controller
public class CuocoController {
	@Autowired
	private CuocoRepository cuocoRepository;
	@Autowired
	private CuocoService cuocoService;
	@Autowired
	private CuocoValidator cuocoValidator;
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private UserService userService;
	@Autowired 
	private CredentialsService credentialsService;
	@Autowired 
	private CredentialsRepository credentialsRepository;
	
	@GetMapping("/generico/paginacuochi")
	public String getCuoco(Model model) {		
		model.addAttribute("cuochi", this.cuocoService.findAll());
		return "/generico/paginacuochi.html";
		}
	
	@GetMapping("/generico/menu")
	public String getmenu() {		
		return "/generico/menu.html";
		}
	@GetMapping("/generico/cuoco/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuoco", this.cuocoService.findById(id));
		return "/generico/cuoco.html";
	}
	@GetMapping("/admin/sezioneCuochi")
	public String getAdminCuoco(Model model) {		
		model.addAttribute("cuochi", this.cuocoService.findAll());
		return "/admin/sezioneCuochi.html";
		}
	@GetMapping("/admin/aggiornaCuoco/{id}")
	public String aggiornaCuoco(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuoco", this.cuocoService.findById(id));
		return "admin/aggiornaCuoco.html";
	}
	@GetMapping(value="/admin/cancellaCuoco/{id}")
	public String cancellaCuoco(@PathVariable("id") Long id, Model model) {
		Cuoco cuoco=this.cuocoService.findById(id);
		this.cuocoRepository.delete(cuoco);
		return "admin/indexAdmin.html";
	}
	@GetMapping(value="/admin/aggiungiCuoco")
	public String aggiungiCuoco( Model model) {
		model.addAttribute("cuoco", new Cuoco());
	    return "admin/aggiungiCuoco.html";
	}
	@PostMapping("admin/aggiungiCuoco")
	public String nuovaCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco,BindingResult bindingResult, Model model) {
		this.cuocoValidator.validate(cuoco, bindingResult);
		 if (!bindingResult.hasErrors()) {
		this.cuocoRepository.save(cuoco);
		return "admin/indexAdmin";
		 }else
		return "admin/aggiungiCuoco";
	}
	@PostMapping("generico/aggiungiCuoco")
	public String genericoNuovaCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco,@RequestParam("utenteid") Long utenteId,
            @RequestParam("id") Long credentialsId,BindingResult bindingResult, Model model) {
		this.cuocoValidator.validate(cuoco, bindingResult);
		Credentials credentials=this.credentialsService.getCredentials(credentialsId);
		User user=this.userService.getUser(utenteId);
		 if (!bindingResult.hasErrors()) {
	    
		Cuoco cuoco2=this.cuocoService.save(cuoco);
	   
	    user.setCuoco(cuoco2);
	    this.userService.saveUser(user);
	    this.credentialsService.saveCredentials(credentials);
	    System.out.println("ccccccccccccccccccccccccccccccccccccccccccccccccccc");
	    System.out.println(credentials.getPassword());
		return "cuoco/indexCuoco";
		 }else {
		return "generico/index";
		 }
	}
	
	@PostMapping("admin/aggiornaCuoco/{id}")
	 public String formAggiornaNomeCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco2,BindingResult bindingResult, @PathVariable("id") Long id, @RequestParam("nuovoAnno") Integer nuovoAnno,@RequestParam("nuovoImmagine") String nuovoImmagine, Model model) {
		model.addAttribute("cuoco",this.cuocoRepository.findById(id).get());
		Cuoco cuoco=this.cuocoRepository.findById(id).get();
		
		if (!bindingResult.hasErrors()) {
		cuoco.setYear(nuovoAnno);
		cuoco.setImmagine(nuovoImmagine);
		this.cuocoRepository.save(cuoco);
		return "admin/indexAdmin.html";
	}else {
		
		return "admin/aggiornaCuoco.html";

		}}
}
	


