package com.personal.backzone.repository;

import com.personal.backzone.domain.Pest;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PestRepository extends JpaRepository<Pest, Long> {
	
	@Query(" SELECT count(*) from Pest p\n"		
			+ "	where p.name = lower(:namepest)")
	int validatePestInsert(@Param("namepest") String namePest);
	
	@Query(" SELECT count(*) from Pest p\n"		
			+ "	where p.name = lower(:namepest)"
			+ " and p.id <> :idpest")
	int validatePestUpdate(@Param("namepest") String namePest, @Param("idpest") Long idPest);

}
