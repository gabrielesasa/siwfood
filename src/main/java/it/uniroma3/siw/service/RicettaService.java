package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.RicettaRepository;
import jakarta.validation.Valid;

@Service
public class RicettaService {
	@Autowired
	private RicettaRepository ricettaRepository;
	@Autowired
	private CuocoService cuocoService;
	public Iterable<Ricetta>findAll(){
		return ricettaRepository.findAll();
	}
	public Ricetta findById(Long id) {
		return ricettaRepository.findById(id).get();
	}
	@Transactional
	public List<Ricetta> findRicetteByCuoco(Cuoco cuoco) {
		return ricettaRepository.findByCuoco(cuoco);
	}
	@Transactional
	public void save2(Ricetta ricetta,MultipartFile file) throws IOException {
		ricetta.setImmagine(Base64.getEncoder().encodeToString(file.getBytes()));
		ricettaRepository.save(ricetta);		
	}
	@Transactional
	public void save(Ricetta ricetta)  {
		ricettaRepository.save(ricetta);		
	}
	
	
	public void delete(Ricetta ricetta) {
		this.ricettaRepository.delete(ricetta);
	}
	@Transactional
	public void nuovaRicetta(Ricetta ricetta, User utente,MultipartFile imageFile) throws IOException {
		Cuoco cuoco=this.cuocoService.findByUser(utente);
		ricetta.setCuoco(cuoco);
		this.save2(ricetta,imageFile);
	}
	@Transactional
	public Long getIdIngrediente(Long id) {
		Ricetta ricetta = this.findById(id);
        List<IngredientePerRicetta> ingredienteRicetta = ricetta.getIngredienteRicetta();
        if (!ingredienteRicetta.isEmpty()) {
            IngredientePerRicetta ingre = ingredienteRicetta.get(0);									
            Ingrediente in = ingre.getIngrediente();
            Long idingrediente = in.getId();
            return idingrediente;
	}
        return null;
        
}
	@Transactional
	public void ricettasetCuocoRicetta(Long cuocoId, Long ricettaId) {
		 
			Cuoco cuoco = this.cuocoService.findById(cuocoId);
	        Ricetta ricetta = this.findById(ricettaId);
	        ricetta.setCuoco(cuoco);
	        this.save(ricetta);
		
		}
	@Transactional
	public void aggiornaRicetta(@Valid Long id, String nuovaDescrizione, String nuovaImmagine) {
		Ricetta ricetta = this.findById(id);
        ricetta.setDescrizione(nuovaDescrizione);
        ricetta.setImmagine(nuovaImmagine);
        this.save(ricetta);
		
	}}

