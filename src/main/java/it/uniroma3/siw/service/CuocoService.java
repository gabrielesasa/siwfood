package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.GlobalController;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CuocoRepository;

@Service
public class CuocoService {
	@Autowired
	private CuocoRepository cuocoRepository;
	
	@Autowired
	GlobalController globalController;
	public Iterable<Cuoco>findAll(){
		return cuocoRepository.findAll();
	}
	@Transactional
	public Cuoco findById(Long id) {
		return cuocoRepository.findById(id).get();
	}
	@Transactional
	public void save(Cuoco cuoco) {
        cuocoRepository.save(cuoco);
    }
	@Transactional
	public Cuoco findByUser(User user) {
        return cuocoRepository.findByUser(user);
    }
	@Transactional
	public void delete(Long id) {
		Cuoco cuoco = this.findById(id);
         cuocoRepository.delete(cuoco);
    }
	@Transactional
	public void save2(Cuoco azienda,MultipartFile file) throws IOException {
		azienda.setImmagine(Base64.getEncoder().encodeToString(file.getBytes()));
		cuocoRepository.save(azienda);		
	}
	@Transactional
	public void creaCuoco(@ModelAttribute("cuoco") Cuoco cuoco, BindingResult bindingResult,
			@RequestParam("imageFile") MultipartFile imageFile,User user) throws IOException {
		
		cuoco.setUser(user);
		this.save2(cuoco, imageFile);	
	}
	@Transactional
	public void aggiornaCuoco(Long id, Integer nuovoAnno, String nuovoImmagine, Model model) {
		model.addAttribute("cuoco", this.cuocoRepository.findById(id).get());
		Cuoco cuoco = this.cuocoRepository.findById(id).get();
		cuoco.setYear(nuovoAnno);
		cuoco.setImmagine(nuovoImmagine);
		this.cuocoRepository.save(cuoco);
	}
}

