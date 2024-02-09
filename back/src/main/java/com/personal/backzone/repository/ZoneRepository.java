package com.personal.backzone.repository;

import com.personal.backzone.domain.Zone;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Zone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
	

	@Query(" SELECT p.name from ZonePest zp\n"
			+ "	join Pest p\n"
			+ "	on zp.pestId.id  = p.id\n"
			+ "	where zp.zoneId.id = :idzone")
	String[] getDataAboutZone(@Param("idzone") Long id);
	
	
	@Query(" SELECT count(*) from ZonePest zp\n"
			+ "	join Pest p\n"
			+ "	on zp.pestId.id  = p.id\n"
			+ "	where zp.zoneId.id = :idzone")
	int getDataAboutZoneQauntity(@Param("idzone") Long id);
	
}
