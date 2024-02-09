package com.personal.backzone.repository;

import com.personal.backzone.domain.ZonePest;
import com.personal.backzone.service.dto.ZonePestDTO;
import com.personal.backzone.service.dto.ZonePestWithNameDetailDTO;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ZonePest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZonePestRepository extends JpaRepository<ZonePest, Long> {
	
	
	@Query(" SELECT count(*) from ZonePest z WHERE z.zoneId.id = :idzone")
	int getQuantityPestByZone(@Param("idzone") Long id);
	
	
	@Query("SELECT zp.id,zp.zoneId.id,z.name,zp.pestId.id,p.name "
			+ "FROM ZonePest zp\n"
			+ "	join Zone z\n"
			+ "	on zp.zoneId.id = z.id\n"
			+ "	join Pest p\n"
			+ "	on zp.pestId.id = p.id")
	List<Object[]> getZonePestWithZoneName();
	
	
	@Query("SELECT count(*) "
			+ "FROM ZonePest zp\n"		
			+ "	where zp.zoneId.id = :idzone\n"		
			+ "	and zp.pestId.id = :idpest")
	int getQuantityZonePest(@Param("idzone") Long idzone, @Param("idpest") Long idpest);
}
