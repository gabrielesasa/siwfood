
package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.IngredientePerRicetta;
import it.uniroma3.siw.model.Ricetta;

@Repository
public interface IngredientePerRicettaRepository extends CrudRepository<IngredientePerRicetta, Long> {
	@Query(value = "SELECT r.* FROM ingrediente_per_ricetta r JOIN ricetta_ingrediente_ricetta i ON r.id = i.ingrediente_ricetta_id WHERE ricetta_id=:id", nativeQuery = true)
	List<IngredientePerRicetta> findRicettaIngredientiByRicetta(@Param("id") Long id);
	
}

