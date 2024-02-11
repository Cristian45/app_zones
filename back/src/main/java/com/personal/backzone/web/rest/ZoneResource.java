package com.personal.backzone.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.personal.backzone.repository.ZoneRepository;
import com.personal.backzone.service.ZoneService;
import com.personal.backzone.service.dto.PestByZoneDTO;
import com.personal.backzone.service.dto.ZoneDTO;
import com.personal.backzone.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.personal.backzone.domain.Zone}.
 */
@RestController
@RequestMapping("/api")
public class ZoneResource {

	private final Logger log = LoggerFactory.getLogger(ZoneResource.class);

	private static final String ENTITY_NAME = "backzoneZone";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ZoneService zoneService;

	private final ZoneRepository zoneRepository;

	public ZoneResource(ZoneService zoneService, ZoneRepository zoneRepository) {
		this.zoneService = zoneService;
		this.zoneRepository = zoneRepository;
	}

	/**
	 * {@code POST  /zones} : Create a new zone.
	 *
	 * @param zoneDTO the zoneDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new zoneDTO, or with status {@code 400 (Bad Request)} if the
	 *         zone has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/zones")
	public ResponseEntity<ZoneDTO> createZone(@RequestBody ZoneDTO zoneDTO) throws URISyntaxException {
		log.debug("REST request to save Zone : {}", zoneDTO);
		if (zoneDTO.getId() != null) {
			throw new BadRequestAlertException("A new zone cannot already have an ID", ENTITY_NAME, "idexists");
		}

		if (zoneDTO.getName() == null) {
			throw new BadRequestAlertException("Se debe enviar un nombre", ENTITY_NAME, "");
		}

		if (zoneDTO.getDescription() == null) {
			throw new BadRequestAlertException("Se debe enviar una descripción", ENTITY_NAME, "");
		}

		if (zoneDTO.getPalmsQuantity() == null) {
			throw new BadRequestAlertException("Se debe enviar una cantidad de palmas", ENTITY_NAME, "");
		}

		// [start] validacion de existencia
		int quantity = this.zoneRepository.validateZoneInsert(zoneDTO.getName());

		if (quantity > 0) {
			throw new BadRequestAlertException("Ya existe una zona con el nombre [" + zoneDTO.getName() + "]",
					ENTITY_NAME, "");
		}
		// [end] validacion de existencia

		zoneDTO.setIsAffected("N");
		ZoneDTO result = zoneService.save(zoneDTO);
		return ResponseEntity.created(new URI("/api/zones/" + result.getId())).headers(HeaderUtil
				// .createEntityCreationAlert(applicationName, false, ENTITY_NAME,
				// result.getId().toString()))
				.createAlert(applicationName, "Fue creada una zona con ID " + result.getId().toString(), ""))
				.body(result);
	}

	/**
	 * {@code PUT  /zones/:id} : Updates an existing zone.
	 *
	 * @param id      the id of the zoneDTO to save.
	 * @param zoneDTO the zoneDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated zoneDTO, or with status {@code 400 (Bad Request)} if the
	 *         zoneDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the zoneDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/zones/{id}")
	public ResponseEntity<ZoneDTO> updateZone(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody ZoneDTO zoneDTO) throws URISyntaxException {
		log.debug("REST request to update Zone : {}, {}", id, zoneDTO);
		if (zoneDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, zoneDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!zoneRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		if (zoneDTO.getName() == null) {
			throw new BadRequestAlertException("Se debe enviar un nombre", ENTITY_NAME, "");
		}

		if (zoneDTO.getDescription() == null) {
			throw new BadRequestAlertException("Se debe enviar una descripción", ENTITY_NAME, "");
		}

		// [start] validacion de existencia
		int quantity = this.zoneRepository.validateZoneUpdate(zoneDTO.getName(), zoneDTO.getId());

		if (quantity > 0) {
			throw new BadRequestAlertException("Ya existe una plaga con el nombre [" + zoneDTO.getName() + "]",
					ENTITY_NAME, "");
		}
		// [end] validacion de existencia

		ZoneDTO result = zoneService.update(zoneDTO);
		return ResponseEntity.ok().headers(HeaderUtil
				// .createEntityUpdateAlert(applicationName, false, ENTITY_NAME,
				// zoneDTO.getId().toString()))
				.createAlert(applicationName, "Fue actualizada una zona con ID " + result.getId().toString(), ""))
				.body(result);
	}

	/**
	 * {@code PATCH  /zones/:id} : Partial updates given fields of an existing zone,
	 * field will ignore if it is null
	 *
	 * @param id      the id of the zoneDTO to save.
	 * @param zoneDTO the zoneDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated zoneDTO, or with status {@code 400 (Bad Request)} if the
	 *         zoneDTO is not valid, or with status {@code 404 (Not Found)} if the
	 *         zoneDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the zoneDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/zones/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<ZoneDTO> partialUpdateZone(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody ZoneDTO zoneDTO) throws URISyntaxException {
		log.debug("REST request to partial update Zone partially : {}, {}", id, zoneDTO);
		if (zoneDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, zoneDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!zoneRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<ZoneDTO> result = zoneService.partialUpdate(zoneDTO);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zoneDTO.getId().toString()));
	}

	/**
	 * {@code GET  /zones} : get all the zones.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of zones in body.
	 */
	@GetMapping("/zones")
	public ResponseEntity<List<ZoneDTO>> getAllZones(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
		log.debug("REST request to get a page of Zones");
		Page<ZoneDTO> page = zoneService.findAll(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /zones/:id} : get the "id" zone.
	 *
	 * @param id the id of the zoneDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the zoneDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/zones/{id}")
	public ResponseEntity<ZoneDTO> getZone(@PathVariable Long id) {
		log.debug("REST request to get Zone : {}", id);
		Optional<ZoneDTO> zoneDTO = zoneService.findOne(id);

		return ResponseUtil.wrapOrNotFound(zoneDTO);
	}

	@GetMapping("/zones/datapest/{id}")
	public ResponseEntity<PestByZoneDTO> getDataAboutZone(@PathVariable Long id) {
		log.debug("REST request to get Zone : {}", id);
		Optional<ZoneDTO> zoneDTO = zoneService.findOne(id);
		int quantity = this.zoneRepository.getDataAboutZoneQauntity(zoneDTO.get().getId());
		String[] result = {};
		if (quantity > 0) {
			result = this.zoneRepository.getDataAboutZone(zoneDTO.get().getId());
		}

		PestByZoneDTO pestByZoneDTOBase = new PestByZoneDTO();
		pestByZoneDTOBase.setPets(result);
		Optional<PestByZoneDTO> pestByZoneDTO = Optional.of(pestByZoneDTOBase);
		return ResponseUtil.wrapOrNotFound(pestByZoneDTO);
	}

	/**
	 * {@code DELETE  /zones/:id} : delete the "id" zone.
	 *
	 * @param id the id of the zoneDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/zones/{id}")
	public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
		log.debug("REST request to delete Zone : {}", id);
		zoneService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
				.build();
	}
}
