
package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ricetta;

public interface RicettaRepository extends CrudRepository<Ricetta, Long> {
	public boolean existsByNomeAndCuoco(String nome,Cuoco cuoco);
	@Query("SELECT r FROM Ricetta r WHERE r.cuoco = :cuoco")
	List<Ricetta> findRicetteByCuoco(@Param("cuoco") Cuoco cuoco);


}

