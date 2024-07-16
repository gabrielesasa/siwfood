
package it.uniroma3.siw.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.validator.CuocoValidator;
import jakarta.validation.Valid;

@Controller
public class CuocoController {
	@Autowired
	private CuocoService cuocoService;
	@Autowired
	private CuocoValidator cuocoValidator;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	GlobalController globalController;

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

	@GetMapping(value = "/admin/cancellaCuoco/{id}")
	public String cancellaCuoco(@PathVariable("id") Long id, Model model) {
		this.cuocoService.delete(id);
		return "admin/indexAdmin.html";
	}

	@GetMapping(value = "/admin/aggiungiCuoco")
	public String aggiungiCuoco(Model model) {
		model.addAttribute("cuoco", new Cuoco());
		return "cuoco/formCreaCuoco.html";
	}

	@GetMapping("/cuoco/profilo")
	public String profiloCuoco(Model model) {
		Credentials credenziali = credentialsService.getCredentials(globalController.getUser());
		User utente = credenziali.getUser();
		Cuoco cuoco = this.cuocoService.findByUser(utente);
		if (cuoco != null) {
			model.addAttribute("cuoco", cuoco);
			return "cuoco/profilo.html";
		} else {
			model.addAttribute("cuoco", new Cuoco());
			return "cuoco/formCreaCuoco.html";

		}
	}

	@PostMapping("admin/aggiungiCuoco")
	public String nuovaCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco, BindingResult bindingResult,
			@RequestParam("imageFile") MultipartFile imageFile, Model model) {
		this.cuocoValidator.validate(cuoco, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.cuocoService.save(cuoco);
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			} else
				return "cuoco/indexCuoco.html";
		} else
			return "admin/aggiungiCuoco";

	}

	@PostMapping("cuoco/formCreaCuoco")
	public String genericoNuovaCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco, BindingResult bindingResult,
			@RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {
		Credentials credenziali = credentialsService.getCredentials(globalController.getUser());

		User user = credenziali.getUser();
		this.cuocoValidator.validate(cuoco, bindingResult);

		if (!bindingResult.hasErrors()) {

			this.cuocoService.creaCuoco(cuoco, bindingResult, imageFile, user);
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			} else
				return "cuoco/indexCuoco.html";
		} else
			return "cuoco/formCreaCuoco";
	}

	@PostMapping("admin/aggiornaCuoco/{id}")
	public String formAggiornaNomeCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco2, BindingResult bindingResult,
			@PathVariable("id") Long id, @RequestParam("nuovoAnno") Integer nuovoAnno,
			@RequestParam("nuovoImmagine") String nuovoImmagine, Model model) {

		this.cuocoService.aggiornaCuoco(id, nuovoAnno, nuovoImmagine, model);
		return "admin/indexAdmin.html";
	}

	
}
