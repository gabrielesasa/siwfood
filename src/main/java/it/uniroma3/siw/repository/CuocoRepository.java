
package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Cuoco;

@Repository
public interface CuocoRepository extends CrudRepository<Cuoco, Long> {

	public boolean existsByNomeAndYear(String nome, Integer year);

}

