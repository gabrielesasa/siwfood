package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.repository.IngredientePerRicettaRepository;
import it.uniroma3.siw.repository.IngredienteRepository;
import jakarta.transaction.Transactional;

@Service
public class IngredientePerRicettaService {
	@Autowired
	private IngredienteRepository ingredienteRepository;
	@Autowired
	private IngredientePerRicettaRepository ingredientePerRicettaRepository;
	@Transactional
	public List<IngredientePerRicetta> findRicettaIngredientiByRicetta(Long id) {
	return this.ingredientePerRicettaRepository.findRicettaIngredientiByRicetta(id);
	}
	
}

