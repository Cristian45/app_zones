package com.personal.backzone.service;

import com.personal.backzone.service.dto.ZonePestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.personal.backzone.domain.ZonePest}.
 */
public interface ZonePestService {
    /**
     * Save a zonePest.
     *
     * @param zonePestDTO the entity to save.
     * @return the persisted entity.
     */
    ZonePestDTO save(ZonePestDTO zonePestDTO);

    /**
     * Updates a zonePest.
     *
     * @param zonePestDTO the entity to update.
     * @return the persisted entity.
     */
    ZonePestDTO update(ZonePestDTO zonePestDTO);

    /**
     * Partially updates a zonePest.
     *
     * @param zonePestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ZonePestDTO> partialUpdate(ZonePestDTO zonePestDTO);

    /**
     * Get all the zonePests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ZonePestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" zonePest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ZonePestDTO> findOne(Long id);

    /**
     * Delete the "id" zonePest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    
    /**
     * Update data zone  when the zone_pest is updated
     *  
     */
    void updateDatazone(Long beforeId, ZonePestDTO zonePestDTO);
    
    /**
     * Update data zone  when the zone_pest is deleted
     *  
     */
    void updateDatazoneByDelete(Long id);
}
