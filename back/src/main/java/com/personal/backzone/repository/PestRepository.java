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

}
