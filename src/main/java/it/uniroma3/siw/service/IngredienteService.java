package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.IngredientePerRicettaRepository;
import it.uniroma3.siw.repository.IngredienteRepository;
import jakarta.transaction.Transactional;

@Service
public class IngredienteService {
	@Autowired
	private IngredienteRepository ingredienteRepository;
	@Autowired
	private RicettaService ricettaService;
	@Autowired
	private IngredientePerRicettaRepository ingredientePerRicettaRepository;
	@Transactional
	public Iterable<Ingrediente>findAll(){
		return ingredienteRepository.findAll();
	}
	@Transactional
	public Ingrediente findById(Long id) {
		return ingredienteRepository.findById(id).get();
	}
	@Transactional
	public void save(Ingrediente ingrediente) {
		ingredienteRepository.save(ingrediente);
		
	}
	@Transactional
	public void delete(Ingrediente ingrediente) {
		this.ingredienteRepository.delete(ingrediente);
		
	}
	@Transactional
	public void aggiungiIngre(Long id,String nuovoNome) {
		Ingrediente ingrediente = this.findById(id);
		ingrediente.setNome(nuovoNome);
		this.ingredienteRepository.save(ingrediente);
		
	}
	@Transactional
	public void aggiungiI( IngredientePerRicetta ingredientePerRicetta, Long ricettaid, Long ingredienteid) {
		Ingrediente ingrediente=this.findById(ingredienteid);
		Ricetta ricetta=this.ricettaService.findById(ricettaid);
		ingredientePerRicetta.setIngrediente(ingrediente);
		List<IngredientePerRicetta> ingredientiPerRicetta = ricetta.getIngredienteRicetta();
		ingredientiPerRicetta.add(ingredientePerRicetta);
		this.ingredientePerRicettaRepository.save(ingredientePerRicetta);
		
	}
}

