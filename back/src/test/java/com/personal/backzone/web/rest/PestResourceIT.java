package com.personal.backzone.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.personal.backzone.IntegrationTest;
import com.personal.backzone.domain.Pest;
import com.personal.backzone.repository.PestRepository;
import com.personal.backzone.service.dto.PestDTO;
import com.personal.backzone.service.mapper.PestMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/pests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PestRepository pestRepository;

    @Autowired
    private PestMapper pestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPestMockMvc;

    private Pest pest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pest createEntity(EntityManager em) {
        Pest pest = new Pest()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT);
        return pest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pest createUpdatedEntity(EntityManager em) {
        Pest pest = new Pest()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT);
        return pest;
    }

    @BeforeEach
    public void initTest() {
        pest = createEntity(em);
    }

    @Test
    @Transactional
    void createPest() throws Exception {
        int databaseSizeBeforeCreate = pestRepository.findAll().size();
        // Create the Pest
        PestDTO pestDTO = pestMapper.toDto(pest);
        restPestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pestDTO)))
            .andExpect(status().isCreated());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeCreate + 1);
        Pest testPest = pestList.get(pestList.size() - 1);
        assertThat(testPest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPest.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPest.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
    }

    @Test
    @Transactional
    void createPestWithExistingId() throws Exception {
        // Create the Pest with an existing ID
        pest.setId(1L);
        PestDTO pestDTO = pestMapper.toDto(pest);

        int databaseSizeBeforeCreate = pestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPests() throws Exception {
        // Initialize the database
        pestRepository.saveAndFlush(pest);

        // Get all the pestList
        restPestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())));
    }

    @Test
    @Transactional
    void getPest() throws Exception {
        // Initialize the database
        pestRepository.saveAndFlush(pest);

        // Get the pest
        restPestMockMvc
            .perform(get(ENTITY_API_URL_ID, pest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPest() throws Exception {
        // Get the pest
        restPestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPest() throws Exception {
        // Initialize the database
        pestRepository.saveAndFlush(pest);

        int databaseSizeBeforeUpdate = pestRepository.findAll().size();

        // Update the pest
        Pest updatedPest = pestRepository.findById(pest.getId()).get();
        // Disconnect from session so that the updates on updatedPest are not directly saved in db
        em.detach(updatedPest);
        updatedPest.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);
        PestDTO pestDTO = pestMapper.toDto(updatedPest);

        restPestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pestDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
        Pest testPest = pestList.get(pestList.size() - 1);
        assertThat(testPest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPest.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPest.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void putNonExistingPest() throws Exception {
        int databaseSizeBeforeUpdate = pestRepository.findAll().size();
        pest.setId(count.incrementAndGet());

        // Create the Pest
        PestDTO pestDTO = pestMapper.toDto(pest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPest() throws Exception {
        int databaseSizeBeforeUpdate = pestRepository.findAll().size();
        pest.setId(count.incrementAndGet());

        // Create the Pest
        PestDTO pestDTO = pestMapper.toDto(pest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPest() throws Exception {
        int databaseSizeBeforeUpdate = pestRepository.findAll().size();
        pest.setId(count.incrementAndGet());

        // Create the Pest
        PestDTO pestDTO = pestMapper.toDto(pest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePestWithPatch() throws Exception {
        // Initialize the database
        pestRepository.saveAndFlush(pest);

        int databaseSizeBeforeUpdate = pestRepository.findAll().size();

        // Update the pest using partial update
        Pest partialUpdatedPest = new Pest();
        partialUpdatedPest.setId(pest.getId());

        partialUpdatedPest.description(UPDATED_DESCRIPTION).updatedat(UPDATED_UPDATEDAT);

        restPestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPest))
            )
            .andExpect(status().isOk());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
        Pest testPest = pestList.get(pestList.size() - 1);
        assertThat(testPest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPest.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPest.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void fullUpdatePestWithPatch() throws Exception {
        // Initialize the database
        pestRepository.saveAndFlush(pest);

        int databaseSizeBeforeUpdate = pestRepository.findAll().size();

        // Update the pest using partial update
        Pest partialUpdatedPest = new Pest();
        partialUpdatedPest.setId(pest.getId());

        partialUpdatedPest.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);

        restPestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPest))
            )
            .andExpect(status().isOk());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
        Pest testPest = pestList.get(pestList.size() - 1);
        assertThat(testPest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPest.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPest.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void patchNonExistingPest() throws Exception {
        int databaseSizeBeforeUpdate = pestRepository.findAll().size();
        pest.setId(count.incrementAndGet());

        // Create the Pest
        PestDTO pestDTO = pestMapper.toDto(pest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPest() throws Exception {
        int databaseSizeBeforeUpdate = pestRepository.findAll().size();
        pest.setId(count.incrementAndGet());

        // Create the Pest
        PestDTO pestDTO = pestMapper.toDto(pest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPest() throws Exception {
        int databaseSizeBeforeUpdate = pestRepository.findAll().size();
        pest.setId(count.incrementAndGet());

        // Create the Pest
        PestDTO pestDTO = pestMapper.toDto(pest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pest in the database
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePest() throws Exception {
        // Initialize the database
        pestRepository.saveAndFlush(pest);

        int databaseSizeBeforeDelete = pestRepository.findAll().size();

        // Delete the pest
        restPestMockMvc
            .perform(delete(ENTITY_API_URL_ID, pest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pest> pestList = pestRepository.findAll();
        assertThat(pestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
