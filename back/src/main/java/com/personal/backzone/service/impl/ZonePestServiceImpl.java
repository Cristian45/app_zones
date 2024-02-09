package com.personal.backzone.service.impl;

import com.personal.backzone.domain.ZonePest;
import com.personal.backzone.repository.ZonePestRepository;
import com.personal.backzone.service.ZonePestService;
import com.personal.backzone.service.ZoneService;
import com.personal.backzone.service.dto.ZoneDTO;
import com.personal.backzone.service.dto.ZonePestDTO;
import com.personal.backzone.service.mapper.ZonePestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ZonePest}.
 */
@Service
@Transactional
public class ZonePestServiceImpl implements ZonePestService {

	private final Logger log = LoggerFactory.getLogger(ZonePestServiceImpl.class);

	private final ZonePestRepository zonePestRepository;

	private final ZonePestMapper zonePestMapper;
	private final ZoneService zoneService;

	public ZonePestServiceImpl(ZonePestRepository zonePestRepository, ZonePestMapper zonePestMapper,
			ZoneService zoneService) {
		this.zonePestRepository = zonePestRepository;
		this.zonePestMapper = zonePestMapper;
		this.zoneService = zoneService;
	}

	@Override
	public ZonePestDTO save(ZonePestDTO zonePestDTO) {
		log.debug("Request to save ZonePest : {}", zonePestDTO);
		ZonePest zonePest = zonePestMapper.toEntity(zonePestDTO);
		zonePest = zonePestRepository.save(zonePest);
		return zonePestMapper.toDto(zonePest);
	}

	@Override
	public ZonePestDTO update(ZonePestDTO zonePestDTO) {
		log.debug("Request to update ZonePest : {}", zonePestDTO);
		ZonePest zonePest = zonePestMapper.toEntity(zonePestDTO);
		zonePest = zonePestRepository.save(zonePest);
		return zonePestMapper.toDto(zonePest);
	}

	@Override
	public Optional<ZonePestDTO> partialUpdate(ZonePestDTO zonePestDTO) {
		log.debug("Request to partially update ZonePest : {}", zonePestDTO);

		return zonePestRepository.findById(zonePestDTO.getId()).map(existingZonePest -> {
			zonePestMapper.partialUpdate(existingZonePest, zonePestDTO);

			return existingZonePest;
		}).map(zonePestRepository::save).map(zonePestMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ZonePestDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ZonePests");
		return zonePestRepository.findAll(pageable).map(zonePestMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ZonePestDTO> findOne(Long id) {
		log.debug("Request to get ZonePest : {}", id);
		return zonePestRepository.findById(id).map(zonePestMapper::toDto);
	}

	@Override
	public void delete(Long id) {
		log.debug("Request to delete ZonePest : {}", id);
		zonePestRepository.deleteById(id);
	}

	@Override
	public void updateDatazone(Long beforeId, ZonePestDTO zonePestDTO) {
		try {
			Optional<ZoneDTO> zoneDTO = this.zoneService.findOne(zonePestDTO.getZoneId().getId());
			ZoneDTO zone = zoneDTO.get();

			int quantity = this.zonePestRepository.getQuantityPestByZone(zonePestDTO.getZoneId().getId());

			if (quantity > 0) {
				zone.setIsAffected("S");
			} else {
				zone.setIsAffected("N");
			}

			this.zoneService.save(zone);

			// Afectacion de zona anterior [start]

			zoneDTO = this.zoneService.findOne(beforeId);
			zone = zoneDTO.get();

			quantity = this.zonePestRepository.getQuantityPestByZone(beforeId);

			if (quantity > 0) {
				zone.setIsAffected("S");
			} else {
				zone.setIsAffected("N");
			}

			this.zoneService.save(zone);

			// Afectacion de zona anterior [end]
		} catch (Exception e) {
			log.error("error en funcion updateDatazone");
		}

	}

	@Override
	public void updateDatazoneByDelete(Long id) {
		try {
			Optional<ZoneDTO> zoneDTO = this.zoneService.findOne(id);
			ZoneDTO zone = zoneDTO.get();

			int quantity = this.zonePestRepository.getQuantityPestByZone(id);

			if (quantity > 0) {
				zone.setIsAffected("S");
			} else {
				zone.setIsAffected("N");
			}

			this.zoneService.save(zone);

		} catch (Exception e) {
			log.error("error en funcion updateDatazoneByDelete");
		}

	}
}
