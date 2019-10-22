package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.WeightingFactor;
import com.batz.compositeselection.repository.WeightingFactorRepository;
import com.batz.compositeselection.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.batz.compositeselection.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WeightingFactorResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class WeightingFactorResourceIT {

    private static final Double DEFAULT_VALUE = 0D;
    private static final Double UPDATED_VALUE = 1D;
    private static final Double SMALLER_VALUE = 0D - 1D;

    @Autowired
    private WeightingFactorRepository weightingFactorRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWeightingFactorMockMvc;

    private WeightingFactor weightingFactor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WeightingFactorResource weightingFactorResource = new WeightingFactorResource(weightingFactorRepository);
        this.restWeightingFactorMockMvc = MockMvcBuilders.standaloneSetup(weightingFactorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeightingFactor createEntity(EntityManager em) {
        WeightingFactor weightingFactor = new WeightingFactor()
            .value(DEFAULT_VALUE);
        return weightingFactor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeightingFactor createUpdatedEntity(EntityManager em) {
        WeightingFactor weightingFactor = new WeightingFactor()
            .value(UPDATED_VALUE);
        return weightingFactor;
    }

    @BeforeEach
    public void initTest() {
        weightingFactor = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeightingFactor() throws Exception {
        int databaseSizeBeforeCreate = weightingFactorRepository.findAll().size();

        // Create the WeightingFactor
        restWeightingFactorMockMvc.perform(post("/api/weighting-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weightingFactor)))
            .andExpect(status().isCreated());

        // Validate the WeightingFactor in the database
        List<WeightingFactor> weightingFactorList = weightingFactorRepository.findAll();
        assertThat(weightingFactorList).hasSize(databaseSizeBeforeCreate + 1);
        WeightingFactor testWeightingFactor = weightingFactorList.get(weightingFactorList.size() - 1);
        assertThat(testWeightingFactor.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createWeightingFactorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weightingFactorRepository.findAll().size();

        // Create the WeightingFactor with an existing ID
        weightingFactor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeightingFactorMockMvc.perform(post("/api/weighting-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weightingFactor)))
            .andExpect(status().isBadRequest());

        // Validate the WeightingFactor in the database
        List<WeightingFactor> weightingFactorList = weightingFactorRepository.findAll();
        assertThat(weightingFactorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWeightingFactors() throws Exception {
        // Initialize the database
        weightingFactorRepository.saveAndFlush(weightingFactor);

        // Get all the weightingFactorList
        restWeightingFactorMockMvc.perform(get("/api/weighting-factors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weightingFactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getWeightingFactor() throws Exception {
        // Initialize the database
        weightingFactorRepository.saveAndFlush(weightingFactor);

        // Get the weightingFactor
        restWeightingFactorMockMvc.perform(get("/api/weighting-factors/{id}", weightingFactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(weightingFactor.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWeightingFactor() throws Exception {
        // Get the weightingFactor
        restWeightingFactorMockMvc.perform(get("/api/weighting-factors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeightingFactor() throws Exception {
        // Initialize the database
        weightingFactorRepository.saveAndFlush(weightingFactor);

        int databaseSizeBeforeUpdate = weightingFactorRepository.findAll().size();

        // Update the weightingFactor
        WeightingFactor updatedWeightingFactor = weightingFactorRepository.findById(weightingFactor.getId()).get();
        // Disconnect from session so that the updates on updatedWeightingFactor are not directly saved in db
        em.detach(updatedWeightingFactor);
        updatedWeightingFactor
            .value(UPDATED_VALUE);

        restWeightingFactorMockMvc.perform(put("/api/weighting-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWeightingFactor)))
            .andExpect(status().isOk());

        // Validate the WeightingFactor in the database
        List<WeightingFactor> weightingFactorList = weightingFactorRepository.findAll();
        assertThat(weightingFactorList).hasSize(databaseSizeBeforeUpdate);
        WeightingFactor testWeightingFactor = weightingFactorList.get(weightingFactorList.size() - 1);
        assertThat(testWeightingFactor.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingWeightingFactor() throws Exception {
        int databaseSizeBeforeUpdate = weightingFactorRepository.findAll().size();

        // Create the WeightingFactor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeightingFactorMockMvc.perform(put("/api/weighting-factors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weightingFactor)))
            .andExpect(status().isBadRequest());

        // Validate the WeightingFactor in the database
        List<WeightingFactor> weightingFactorList = weightingFactorRepository.findAll();
        assertThat(weightingFactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWeightingFactor() throws Exception {
        // Initialize the database
        weightingFactorRepository.saveAndFlush(weightingFactor);

        int databaseSizeBeforeDelete = weightingFactorRepository.findAll().size();

        // Delete the weightingFactor
        restWeightingFactorMockMvc.perform(delete("/api/weighting-factors/{id}", weightingFactor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WeightingFactor> weightingFactorList = weightingFactorRepository.findAll();
        assertThat(weightingFactorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeightingFactor.class);
        WeightingFactor weightingFactor1 = new WeightingFactor();
        weightingFactor1.setId(1L);
        WeightingFactor weightingFactor2 = new WeightingFactor();
        weightingFactor2.setId(weightingFactor1.getId());
        assertThat(weightingFactor1).isEqualTo(weightingFactor2);
        weightingFactor2.setId(2L);
        assertThat(weightingFactor1).isNotEqualTo(weightingFactor2);
        weightingFactor1.setId(null);
        assertThat(weightingFactor1).isNotEqualTo(weightingFactor2);
    }
}
