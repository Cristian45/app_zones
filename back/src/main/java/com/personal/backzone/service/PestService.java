package com.personal.backzone.service;

import com.personal.backzone.service.dto.PestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.personal.backzone.domain.Pest}.
 */
public interface PestService {
    /**
     * Save a pest.
     *
     * @param pestDTO the entity to save.
     * @return the persisted entity.
     */
    PestDTO save(PestDTO pestDTO);

    /**
     * Updates a pest.
     *
     * @param pestDTO the entity to update.
     * @return the persisted entity.
     */
    PestDTO update(PestDTO pestDTO);

    /**
     * Partially updates a pest.
     *
     * @param pestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PestDTO> partialUpdate(PestDTO pestDTO);

    /**
     * Get all the pests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PestDTO> findOne(Long id);

    /**
     * Delete the "id" pest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
