package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.CalculationResult;
import com.batz.compositeselection.repository.CalculationResultRepository;
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
 * Integration tests for the {@link CalculationResultResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class CalculationResultResourceIT {

    private static final Double DEFAULT_RATING_FACTOR = 1D;
    private static final Double UPDATED_RATING_FACTOR = 2D;
    private static final Double SMALLER_RATING_FACTOR = 1D - 1D;

    @Autowired
    private CalculationResultRepository calculationResultRepository;

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

    private MockMvc restCalculationResultMockMvc;

    private CalculationResult calculationResult;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalculationResultResource calculationResultResource = new CalculationResultResource(calculationResultRepository);
        this.restCalculationResultMockMvc = MockMvcBuilders.standaloneSetup(calculationResultResource)
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
    public static CalculationResult createEntity(EntityManager em) {
        CalculationResult calculationResult = new CalculationResult()
            .ratingFactor(DEFAULT_RATING_FACTOR);
        return calculationResult;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalculationResult createUpdatedEntity(EntityManager em) {
        CalculationResult calculationResult = new CalculationResult()
            .ratingFactor(UPDATED_RATING_FACTOR);
        return calculationResult;
    }

    @BeforeEach
    public void initTest() {
        calculationResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalculationResult() throws Exception {
        int databaseSizeBeforeCreate = calculationResultRepository.findAll().size();

        // Create the CalculationResult
        restCalculationResultMockMvc.perform(post("/api/calculation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calculationResult)))
            .andExpect(status().isCreated());

        // Validate the CalculationResult in the database
        List<CalculationResult> calculationResultList = calculationResultRepository.findAll();
        assertThat(calculationResultList).hasSize(databaseSizeBeforeCreate + 1);
        CalculationResult testCalculationResult = calculationResultList.get(calculationResultList.size() - 1);
        assertThat(testCalculationResult.getRatingFactor()).isEqualTo(DEFAULT_RATING_FACTOR);
    }

    @Test
    @Transactional
    public void createCalculationResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calculationResultRepository.findAll().size();

        // Create the CalculationResult with an existing ID
        calculationResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalculationResultMockMvc.perform(post("/api/calculation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calculationResult)))
            .andExpect(status().isBadRequest());

        // Validate the CalculationResult in the database
        List<CalculationResult> calculationResultList = calculationResultRepository.findAll();
        assertThat(calculationResultList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCalculationResults() throws Exception {
        // Initialize the database
        calculationResultRepository.saveAndFlush(calculationResult);

        // Get all the calculationResultList
        restCalculationResultMockMvc.perform(get("/api/calculation-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calculationResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].ratingFactor").value(hasItem(DEFAULT_RATING_FACTOR.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCalculationResult() throws Exception {
        // Initialize the database
        calculationResultRepository.saveAndFlush(calculationResult);

        // Get the calculationResult
        restCalculationResultMockMvc.perform(get("/api/calculation-results/{id}", calculationResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calculationResult.getId().intValue()))
            .andExpect(jsonPath("$.ratingFactor").value(DEFAULT_RATING_FACTOR.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCalculationResult() throws Exception {
        // Get the calculationResult
        restCalculationResultMockMvc.perform(get("/api/calculation-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalculationResult() throws Exception {
        // Initialize the database
        calculationResultRepository.saveAndFlush(calculationResult);

        int databaseSizeBeforeUpdate = calculationResultRepository.findAll().size();

        // Update the calculationResult
        CalculationResult updatedCalculationResult = calculationResultRepository.findById(calculationResult.getId()).get();
        // Disconnect from session so that the updates on updatedCalculationResult are not directly saved in db
        em.detach(updatedCalculationResult);
        updatedCalculationResult
            .ratingFactor(UPDATED_RATING_FACTOR);

        restCalculationResultMockMvc.perform(put("/api/calculation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalculationResult)))
            .andExpect(status().isOk());

        // Validate the CalculationResult in the database
        List<CalculationResult> calculationResultList = calculationResultRepository.findAll();
        assertThat(calculationResultList).hasSize(databaseSizeBeforeUpdate);
        CalculationResult testCalculationResult = calculationResultList.get(calculationResultList.size() - 1);
        assertThat(testCalculationResult.getRatingFactor()).isEqualTo(UPDATED_RATING_FACTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingCalculationResult() throws Exception {
        int databaseSizeBeforeUpdate = calculationResultRepository.findAll().size();

        // Create the CalculationResult

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalculationResultMockMvc.perform(put("/api/calculation-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calculationResult)))
            .andExpect(status().isBadRequest());

        // Validate the CalculationResult in the database
        List<CalculationResult> calculationResultList = calculationResultRepository.findAll();
        assertThat(calculationResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCalculationResult() throws Exception {
        // Initialize the database
        calculationResultRepository.saveAndFlush(calculationResult);

        int databaseSizeBeforeDelete = calculationResultRepository.findAll().size();

        // Delete the calculationResult
        restCalculationResultMockMvc.perform(delete("/api/calculation-results/{id}", calculationResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CalculationResult> calculationResultList = calculationResultRepository.findAll();
        assertThat(calculationResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalculationResult.class);
        CalculationResult calculationResult1 = new CalculationResult();
        calculationResult1.setId(1L);
        CalculationResult calculationResult2 = new CalculationResult();
        calculationResult2.setId(calculationResult1.getId());
        assertThat(calculationResult1).isEqualTo(calculationResult2);
        calculationResult2.setId(2L);
        assertThat(calculationResult1).isNotEqualTo(calculationResult2);
        calculationResult1.setId(null);
        assertThat(calculationResult1).isNotEqualTo(calculationResult2);
    }
}
