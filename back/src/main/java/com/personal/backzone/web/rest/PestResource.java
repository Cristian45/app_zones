package com.personal.backzone.web.rest;

import com.personal.backzone.repository.PestRepository;
import com.personal.backzone.service.PestService;
import com.personal.backzone.service.dto.PestDTO;
import com.personal.backzone.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.personal.backzone.domain.Pest}.
 */
@RestController
@RequestMapping("/api")
public class PestResource {

	private final Logger log = LoggerFactory.getLogger(PestResource.class);

	private static final String ENTITY_NAME = "backzonePest";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final PestService pestService;

	private final PestRepository pestRepository;

	public PestResource(PestService pestService, PestRepository pestRepository) {
		this.pestService = pestService;
		this.pestRepository = pestRepository;
	}

	/**
	 * {@code POST  /pests} : Create a new pest.
	 *
	 * @param pestDTO the pestDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new pestDTO, or with status {@code 400 (Bad Request)} if the
	 *         pest has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/pests")
	public ResponseEntity<PestDTO> createPest(@RequestBody PestDTO pestDTO) throws URISyntaxException {
		log.debug("REST request to save Pest : {}", pestDTO);
		if (pestDTO.getId() != null) {
			throw new BadRequestAlertException("A new pest cannot already have an ID", ENTITY_NAME, "idexists");
		}

		if (pestDTO.getName() == null) {
			throw new BadRequestAlertException("Se debe enviar un nombre", ENTITY_NAME, "");
		}

		if (pestDTO.getDescription() == null) {
			throw new BadRequestAlertException("Se debe enviar una descripción", ENTITY_NAME, "");
		}

		// [start] validacion de existencia
		int quantity = this.pestRepository.validatePestInsert(pestDTO.getName());

		if (quantity > 0) {
			throw new BadRequestAlertException("Ya existe una plaga con el nombre [" + pestDTO.getName() + "]",
					ENTITY_NAME, "");
		}
		// [end] validacion de existencia
		PestDTO result = pestService.save(pestDTO);
		return ResponseEntity.created(new URI("/api/pests/" + result.getId())).headers(HeaderUtil
				// .createEntityCreationAlert(applicationName, false, ENTITY_NAME,
				// result.getId().toString()))
				.createAlert(applicationName, "Fue creada una plaga con ID " + result.getId().toString(), ""))
				.body(result);
	}

	/**
	 * {@code PUT  /pests/:id} : Updates an existing pest.
	 *
	 * @param id      the id of the pestDTO to save.
	 * @param pestDTO the pestDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated pestDTO, or with status {@code 400 (Bad Request)} if the
	 *         pestDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the pestDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/pests/{id}")
	public ResponseEntity<PestDTO> updatePest(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody PestDTO pestDTO) throws URISyntaxException {
		log.debug("REST request to update Pest : {}, {}", id, pestDTO);
		if (pestDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, pestDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!pestRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		// [start] validacion de existencia
		int quantity = this.pestRepository.validatePestUpdate(pestDTO.getName(), pestDTO.getId());

		if (quantity > 0) {
			throw new BadRequestAlertException("Ya existe una plaga con el nombre [" + pestDTO.getName() + "]",
					ENTITY_NAME, "");
		}
		// [end] validacion de existencia

		PestDTO result = pestService.update(pestDTO);
		return ResponseEntity.ok().headers(HeaderUtil
				// .createEntityUpdateAlert(applicationName, false, ENTITY_NAME,
				// pestDTO.getId().toString()))
				.createAlert(applicationName, "Fue actualizada una plaga con ID " + result.getId().toString(), ""))
				.body(result);
	}

	/**
	 * {@code PATCH  /pests/:id} : Partial updates given fields of an existing pest,
	 * field will ignore if it is null
	 *
	 * @param id      the id of the pestDTO to save.
	 * @param pestDTO the pestDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated pestDTO, or with status {@code 400 (Bad Request)} if the
	 *         pestDTO is not valid, or with status {@code 404 (Not Found)} if the
	 *         pestDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the pestDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/pests/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<PestDTO> partialUpdatePest(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody PestDTO pestDTO) throws URISyntaxException {
		log.debug("REST request to partial update Pest partially : {}, {}", id, pestDTO);
		if (pestDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, pestDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!pestRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<PestDTO> result = pestService.partialUpdate(pestDTO);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pestDTO.getId().toString()));
	}

	/**
	 * {@code GET  /pests} : get all the pests.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of pests in body.
	 */
	@GetMapping("/pests")
	public ResponseEntity<List<PestDTO>> getAllPests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
		log.debug("REST request to get a page of Pests");
		Page<PestDTO> page = pestService.findAll(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /pests/:id} : get the "id" pest.
	 *
	 * @param id the id of the pestDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the pestDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/pests/{id}")
	public ResponseEntity<PestDTO> getPest(@PathVariable Long id) {
		log.debug("REST request to get Pest : {}", id);
		Optional<PestDTO> pestDTO = pestService.findOne(id);
		return ResponseUtil.wrapOrNotFound(pestDTO);
	}

	/**
	 * {@code DELETE  /pests/:id} : delete the "id" pest.
	 *
	 * @param id the id of the pestDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/pests/{id}")
	public ResponseEntity<Void> deletePest(@PathVariable Long id) {
		log.debug("REST request to delete Pest : {}", id);
		pestService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
				.build();
	}
}
