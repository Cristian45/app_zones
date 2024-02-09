package com.personal.backzone.service.impl;

import com.personal.backzone.domain.Pest;
import com.personal.backzone.repository.PestRepository;
import com.personal.backzone.service.PestService;
import com.personal.backzone.service.dto.PestDTO;
import com.personal.backzone.service.mapper.PestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pest}.
 */
@Service
@Transactional
public class PestServiceImpl implements PestService {

    private final Logger log = LoggerFactory.getLogger(PestServiceImpl.class);

    private final PestRepository pestRepository;

    private final PestMapper pestMapper;

    public PestServiceImpl(PestRepository pestRepository, PestMapper pestMapper) {
        this.pestRepository = pestRepository;
        this.pestMapper = pestMapper;
    }

    @Override
    public PestDTO save(PestDTO pestDTO) {
        log.debug("Request to save Pest : {}", pestDTO);
        Pest pest = pestMapper.toEntity(pestDTO);
        pest = pestRepository.save(pest);
        return pestMapper.toDto(pest);
    }

    @Override
    public PestDTO update(PestDTO pestDTO) {
        log.debug("Request to update Pest : {}", pestDTO);
        Pest pest = pestMapper.toEntity(pestDTO);
        pest = pestRepository.save(pest);
        return pestMapper.toDto(pest);
    }

    @Override
    public Optional<PestDTO> partialUpdate(PestDTO pestDTO) {
        log.debug("Request to partially update Pest : {}", pestDTO);

        return pestRepository
            .findById(pestDTO.getId())
            .map(existingPest -> {
                pestMapper.partialUpdate(existingPest, pestDTO);

                return existingPest;
            })
            .map(pestRepository::save)
            .map(pestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pests");
        return pestRepository.findAll(pageable).map(pestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PestDTO> findOne(Long id) {
        log.debug("Request to get Pest : {}", id);
        return pestRepository.findById(id).map(pestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pest : {}", id);
        pestRepository.deleteById(id);
    }
}
