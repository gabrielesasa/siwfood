package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.repository.CuocoRepository;

@Component
public class CuocoValidator implements Validator {
	@Autowired
	private CuocoRepository cuocoRepository;
	@Override
	public void validate(Object o, Errors errors) {
	    Cuoco cuoco = (Cuoco)o;
	    if (cuoco.getNome() != null && cuoco.getYear() != null
	        && cuocoRepository.existsByNomeAndYear(cuoco.getNome(), cuoco.getYear())) {
	        errors.reject("cuoco.duplicate");
	    }
	}

	@Override
	public boolean supports(Class<?> aClass) {
	    return Cuoco.class.equals(aClass);
	}

}
