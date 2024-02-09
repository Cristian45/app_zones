package com.personal.backzone.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.personal.backzone.IntegrationTest;
import com.personal.backzone.domain.ZonePest;
import com.personal.backzone.repository.ZonePestRepository;
import com.personal.backzone.service.dto.ZonePestDTO;
import com.personal.backzone.service.mapper.ZonePestMapper;
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
 * Integration tests for the {@link ZonePestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ZonePestResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/zone-pests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ZonePestRepository zonePestRepository;

    @Autowired
    private ZonePestMapper zonePestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZonePestMockMvc;

    private ZonePest zonePest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZonePest createEntity(EntityManager em) {
        ZonePest zonePest = new ZonePest().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT);
        return zonePest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZonePest createUpdatedEntity(EntityManager em) {
        ZonePest zonePest = new ZonePest().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);
        return zonePest;
    }

    @BeforeEach
    public void initTest() {
        zonePest = createEntity(em);
    }

    @Test
    @Transactional
    void createZonePest() throws Exception {
        int databaseSizeBeforeCreate = zonePestRepository.findAll().size();
        // Create the ZonePest
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);
        restZonePestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zonePestDTO)))
            .andExpect(status().isCreated());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeCreate + 1);
        ZonePest testZonePest = zonePestList.get(zonePestList.size() - 1);
        assertThat(testZonePest.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testZonePest.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
    }

    @Test
    @Transactional
    void createZonePestWithExistingId() throws Exception {
        // Create the ZonePest with an existing ID
        zonePest.setId(1L);
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);

        int databaseSizeBeforeCreate = zonePestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZonePestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zonePestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllZonePests() throws Exception {
        // Initialize the database
        zonePestRepository.saveAndFlush(zonePest);

        // Get all the zonePestList
        restZonePestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zonePest.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())));
    }

    @Test
    @Transactional
    void getZonePest() throws Exception {
        // Initialize the database
        zonePestRepository.saveAndFlush(zonePest);

        // Get the zonePest
        restZonePestMockMvc
            .perform(get(ENTITY_API_URL_ID, zonePest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zonePest.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingZonePest() throws Exception {
        // Get the zonePest
        restZonePestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingZonePest() throws Exception {
        // Initialize the database
        zonePestRepository.saveAndFlush(zonePest);

        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();

        // Update the zonePest
        ZonePest updatedZonePest = zonePestRepository.findById(zonePest.getId()).get();
        // Disconnect from session so that the updates on updatedZonePest are not directly saved in db
        em.detach(updatedZonePest);
        updatedZonePest.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(updatedZonePest);

        restZonePestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zonePestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zonePestDTO))
            )
            .andExpect(status().isOk());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
        ZonePest testZonePest = zonePestList.get(zonePestList.size() - 1);
        assertThat(testZonePest.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testZonePest.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void putNonExistingZonePest() throws Exception {
        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();
        zonePest.setId(count.incrementAndGet());

        // Create the ZonePest
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZonePestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zonePestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zonePestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZonePest() throws Exception {
        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();
        zonePest.setId(count.incrementAndGet());

        // Create the ZonePest
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonePestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zonePestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZonePest() throws Exception {
        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();
        zonePest.setId(count.incrementAndGet());

        // Create the ZonePest
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonePestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zonePestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZonePestWithPatch() throws Exception {
        // Initialize the database
        zonePestRepository.saveAndFlush(zonePest);

        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();

        // Update the zonePest using partial update
        ZonePest partialUpdatedZonePest = new ZonePest();
        partialUpdatedZonePest.setId(zonePest.getId());

        partialUpdatedZonePest.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);

        restZonePestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZonePest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZonePest))
            )
            .andExpect(status().isOk());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
        ZonePest testZonePest = zonePestList.get(zonePestList.size() - 1);
        assertThat(testZonePest.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testZonePest.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void fullUpdateZonePestWithPatch() throws Exception {
        // Initialize the database
        zonePestRepository.saveAndFlush(zonePest);

        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();

        // Update the zonePest using partial update
        ZonePest partialUpdatedZonePest = new ZonePest();
        partialUpdatedZonePest.setId(zonePest.getId());

        partialUpdatedZonePest.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);

        restZonePestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZonePest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZonePest))
            )
            .andExpect(status().isOk());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
        ZonePest testZonePest = zonePestList.get(zonePestList.size() - 1);
        assertThat(testZonePest.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testZonePest.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void patchNonExistingZonePest() throws Exception {
        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();
        zonePest.setId(count.incrementAndGet());

        // Create the ZonePest
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZonePestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zonePestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zonePestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZonePest() throws Exception {
        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();
        zonePest.setId(count.incrementAndGet());

        // Create the ZonePest
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonePestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zonePestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZonePest() throws Exception {
        int databaseSizeBeforeUpdate = zonePestRepository.findAll().size();
        zonePest.setId(count.incrementAndGet());

        // Create the ZonePest
        ZonePestDTO zonePestDTO = zonePestMapper.toDto(zonePest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZonePestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(zonePestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZonePest in the database
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZonePest() throws Exception {
        // Initialize the database
        zonePestRepository.saveAndFlush(zonePest);

        int databaseSizeBeforeDelete = zonePestRepository.findAll().size();

        // Delete the zonePest
        restZonePestMockMvc
            .perform(delete(ENTITY_API_URL_ID, zonePest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ZonePest> zonePestList = zonePestRepository.findAll();
        assertThat(zonePestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
