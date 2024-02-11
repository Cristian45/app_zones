package com.personal.backzone.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

import com.personal.backzone.repository.ZonePestRepository;
import com.personal.backzone.service.ZonePestService;
import com.personal.backzone.service.ZoneService;
import com.personal.backzone.service.dto.ZoneDTO;
import com.personal.backzone.service.dto.ZonePestDTO;
import com.personal.backzone.service.dto.ZonePestWithNameDetailDTO;
import com.personal.backzone.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.personal.backzone.domain.ZonePest}.
 */
@RestController
@RequestMapping("/api")
public class ZonePestResource {

	private final Logger log = LoggerFactory.getLogger(ZonePestResource.class);

	private static final String ENTITY_NAME = "backzoneZonePest";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ZonePestService zonePestService;

	private final ZonePestRepository zonePestRepository;

	private final ZoneService zoneService;

	public ZonePestResource(ZonePestService zonePestService, ZonePestRepository zonePestRepository,
			ZoneService zoneService) {
		this.zonePestService = zonePestService;
		this.zonePestRepository = zonePestRepository;
		this.zoneService = zoneService;
	}

	/**
	 * {@code POST  /zone-pests} : Create a new zonePest.
	 *
	 * @param zonePestDTO the zonePestDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new zonePestDTO, or with status {@code 400 (Bad Request)} if
	 *         the zonePest has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/zone-pests")
	public ResponseEntity<ZonePestDTO> createZonePest(@RequestBody ZonePestDTO zonePestDTO) throws URISyntaxException {
		log.debug("REST request to save ZonePest : {}", zonePestDTO);
		if (zonePestDTO.getId() != null) {
			throw new BadRequestAlertException("A new zonePest cannot already have an ID", ENTITY_NAME, "idexists");
		}

		if (zonePestDTO.getZoneId() == null) {
			throw new BadRequestAlertException("Se debe enviar una zona", ENTITY_NAME, "");
		}

		if (zonePestDTO.getPestId() == null) {
			throw new BadRequestAlertException("Se debe enviar una plaga", ENTITY_NAME, "");
		}

		// [start] validacion de existencia
		int quantity = this.zonePestRepository.getQuantityZonePest(zonePestDTO.getZoneId().getId(),
				zonePestDTO.getPestId().getId());

		if (quantity > 0) {
			throw new BadRequestAlertException("Ya existe una asignación de la plaga ["
					+ zonePestDTO.getPestId().getName() + "] a la zona [" + zonePestDTO.getZoneId().getName() + "]",
					ENTITY_NAME, "");
		}
		// [end] validacion de existencia

		ZonePestDTO result = zonePestService.save(zonePestDTO);

		Optional<ZoneDTO> zoneDTO = this.zoneService.findOne(zonePestDTO.getZoneId().getId());
		ZoneDTO zone = zoneDTO.get();
		zone.setIsAffected("S");
		this.zoneService.save(zone);

		return ResponseEntity
				.created(new URI("/api/zone-pests/" + result.getId())).headers(HeaderUtil
						//.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
						.createAlert(applicationName, "Fue creada una afectación de zona con ID "+result.getId().toString(), ""))
				.body(result);
	}

	/**
	 * {@code PUT  /zone-pests/:id} : Updates an existing zonePest.
	 *
	 * @param id          the id of the zonePestDTO to save.
	 * @param zonePestDTO the zonePestDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated zonePestDTO, or with status {@code 400 (Bad Request)} if
	 *         the zonePestDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the zonePestDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/zone-pests/{id}")
	public ResponseEntity<ZonePestDTO> updateZonePest(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody ZonePestDTO zonePestDTO) throws URISyntaxException {
		log.debug("REST request to update ZonePest : {}, {}", id, zonePestDTO);
		if (zonePestDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, zonePestDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!zonePestRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		if (zonePestDTO.getZoneId() == null) {
			throw new BadRequestAlertException("Se debe enviar una zona", ENTITY_NAME, "");
		}

		if (zonePestDTO.getPestId() == null) {
			throw new BadRequestAlertException("Se debe enviar una plaga", ENTITY_NAME, "");
		}

		// [start] validacion de existencia
		int quantity = this.zonePestRepository.getQuantityZonePestUpdating(zonePestDTO.getZoneId().getId(),
				zonePestDTO.getPestId().getId(), zonePestDTO.getId());

		if (quantity > 0) {
			throw new BadRequestAlertException("Ya existe esta asignación de plaga ",
					ENTITY_NAME, "");
		}
		// [end] validacion de existencia

		// consulta antes del update [start]

		Long beforeZone = this.zonePestService.findOne(zonePestDTO.getId()).get().getZoneId().getId();
		// consulta antes del update [end]

		ZonePestDTO result = zonePestService.update(zonePestDTO);

		zonePestService.updateDatazone(beforeZone, result);

		return ResponseEntity.ok().headers(
				HeaderUtil
				//.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zonePestDTO.getId().toString()))
				.createAlert(applicationName, "Fue actualizada una afectación de zona con ID "+result.getId().toString(), ""))
				.body(result);
	}

	/**
	 * {@code PATCH  /zone-pests/:id} : Partial updates given fields of an existing
	 * zonePest, field will ignore if it is null
	 *
	 * @param id          the id of the zonePestDTO to save.
	 * @param zonePestDTO the zonePestDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated zonePestDTO, or with status {@code 400 (Bad Request)} if
	 *         the zonePestDTO is not valid, or with status {@code 404 (Not Found)}
	 *         if the zonePestDTO is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the zonePestDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/zone-pests/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<ZonePestDTO> partialUpdateZonePest(
			@PathVariable(value = "id", required = false) final Long id, @RequestBody ZonePestDTO zonePestDTO)
			throws URISyntaxException {
		log.debug("REST request to partial update ZonePest partially : {}, {}", id, zonePestDTO);
		if (zonePestDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, zonePestDTO.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!zonePestRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<ZonePestDTO> result = zonePestService.partialUpdate(zonePestDTO);

		return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false,
				ENTITY_NAME, zonePestDTO.getId().toString()));
	}

	/**
	 * {@code GET  /zone-pests} : get all the zonePests.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of zonePests in body.
	 */
	@GetMapping("/zone-pests")
	public ResponseEntity<List<ZonePestDTO>> getAllZonePests(
			@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
		log.debug("REST request to get a page of ZonePests");
		Page<ZonePestDTO> page = zonePestService.findAll(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /zone-pests/:id} : get the "id" zonePest.
	 *
	 * @param id the id of the zonePestDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the zonePestDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/zone-pests/{id}")
	public ResponseEntity<ZonePestDTO> getZonePest(@PathVariable Long id) {
		log.debug("REST request to get ZonePest : {}", id);
		Optional<ZonePestDTO> zonePestDTO = zonePestService.findOne(id);
		return ResponseUtil.wrapOrNotFound(zonePestDTO);
	}

	@GetMapping("/zone-pests/zonepestwithzonename")
	public ResponseEntity<List<ZonePestWithNameDetailDTO>> getZonePestWithZoneName() {
		log.debug("REST request to get a page of ZonePests");

		List<ZonePestWithNameDetailDTO> zonePestWithNameDetailDTO = new ArrayList<ZonePestWithNameDetailDTO>();
		List<Object[]> object = this.zonePestRepository.getZonePestWithZoneName();
		for (Object[] objects : object) {
			ZonePestWithNameDetailDTO oneZonePest = new ZonePestWithNameDetailDTO();
			oneZonePest.setId((Long) objects[0]);
			oneZonePest.setZoneId((Long) objects[1]);
			oneZonePest.setZoneName((String) objects[2]);
			oneZonePest.setPestId((Long) objects[3]);
			oneZonePest.setPestName((String) objects[4]);
			zonePestWithNameDetailDTO.add(oneZonePest);
		}

		return ResponseEntity.ok().body(zonePestWithNameDetailDTO);
	}

	/**
	 * {@code DELETE  /zone-pests/:id} : delete the "id" zonePest.
	 *
	 * @param id the id of the zonePestDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/zone-pests/{id}")
	public ResponseEntity<Void> deleteZonePest(@PathVariable Long id) {
		log.debug("REST request to delete ZonePest : {}", id);

		Optional<ZonePestDTO> zonePestDTOthis = this.zonePestService.findOne(id);
		zonePestService.delete(id);
		this.zonePestService.updateDatazoneByDelete(zonePestDTOthis.get().getZoneId().getId());
		return ResponseEntity.noContent()
				.headers(HeaderUtil
						//.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
						.createAlert(applicationName, "Fue eliminada una afectación de zona con ID "+id, ""))
				.build();
	}
}
