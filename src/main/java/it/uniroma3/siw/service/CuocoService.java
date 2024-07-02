package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.repository.CuocoRepository;

@Service
public class CuocoService {
	@Autowired
	private CuocoRepository cuocoRepository;
	public Iterable<Cuoco>findAll(){
		return cuocoRepository.findAll();
	}
	@Transactional()
	public Cuoco findById(Long id) {
		return cuocoRepository.findById(id).get();
	}
	@Transactional()
	public Cuoco save(Cuoco cuoco) {
		return cuocoRepository.save(cuoco);
		
	}
}

