package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.Calculation;
import com.batz.compositeselection.repository.CalculationRepository;
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
 * Integration tests for the {@link CalculationResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class CalculationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CalculationRepository calculationRepository;

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

    private MockMvc restCalculationMockMvc;

    private Calculation calculation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CalculationResource calculationResource = new CalculationResource(calculationRepository);
        this.restCalculationMockMvc = MockMvcBuilders.standaloneSetup(calculationResource)
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
    public static Calculation createEntity(EntityManager em) {
        Calculation calculation = new Calculation()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return calculation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calculation createUpdatedEntity(EntityManager em) {
        Calculation calculation = new Calculation()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return calculation;
    }

    @BeforeEach
    public void initTest() {
        calculation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalculation() throws Exception {
        int databaseSizeBeforeCreate = calculationRepository.findAll().size();

        // Create the Calculation
        restCalculationMockMvc.perform(post("/api/calculations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calculation)))
            .andExpect(status().isCreated());

        // Validate the Calculation in the database
        List<Calculation> calculationList = calculationRepository.findAll();
        assertThat(calculationList).hasSize(databaseSizeBeforeCreate + 1);
        Calculation testCalculation = calculationList.get(calculationList.size() - 1);
        assertThat(testCalculation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCalculation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCalculationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = calculationRepository.findAll().size();

        // Create the Calculation with an existing ID
        calculation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalculationMockMvc.perform(post("/api/calculations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calculation)))
            .andExpect(status().isBadRequest());

        // Validate the Calculation in the database
        List<Calculation> calculationList = calculationRepository.findAll();
        assertThat(calculationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = calculationRepository.findAll().size();
        // set the field null
        calculation.setName(null);

        // Create the Calculation, which fails.

        restCalculationMockMvc.perform(post("/api/calculations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calculation)))
            .andExpect(status().isBadRequest());

        List<Calculation> calculationList = calculationRepository.findAll();
        assertThat(calculationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalculations() throws Exception {
        // Initialize the database
        calculationRepository.saveAndFlush(calculation);

        // Get all the calculationList
        restCalculationMockMvc.perform(get("/api/calculations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getCalculation() throws Exception {
        // Initialize the database
        calculationRepository.saveAndFlush(calculation);

        // Get the calculation
        restCalculationMockMvc.perform(get("/api/calculations/{id}", calculation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calculation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCalculation() throws Exception {
        // Get the calculation
        restCalculationMockMvc.perform(get("/api/calculations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalculation() throws Exception {
        // Initialize the database
        calculationRepository.saveAndFlush(calculation);

        int databaseSizeBeforeUpdate = calculationRepository.findAll().size();

        // Update the calculation
        Calculation updatedCalculation = calculationRepository.findById(calculation.getId()).get();
        // Disconnect from session so that the updates on updatedCalculation are not directly saved in db
        em.detach(updatedCalculation);
        updatedCalculation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restCalculationMockMvc.perform(put("/api/calculations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalculation)))
            .andExpect(status().isOk());

        // Validate the Calculation in the database
        List<Calculation> calculationList = calculationRepository.findAll();
        assertThat(calculationList).hasSize(databaseSizeBeforeUpdate);
        Calculation testCalculation = calculationList.get(calculationList.size() - 1);
        assertThat(testCalculation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCalculation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCalculation() throws Exception {
        int databaseSizeBeforeUpdate = calculationRepository.findAll().size();

        // Create the Calculation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalculationMockMvc.perform(put("/api/calculations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calculation)))
            .andExpect(status().isBadRequest());

        // Validate the Calculation in the database
        List<Calculation> calculationList = calculationRepository.findAll();
        assertThat(calculationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCalculation() throws Exception {
        // Initialize the database
        calculationRepository.saveAndFlush(calculation);

        int databaseSizeBeforeDelete = calculationRepository.findAll().size();

        // Delete the calculation
        restCalculationMockMvc.perform(delete("/api/calculations/{id}", calculation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calculation> calculationList = calculationRepository.findAll();
        assertThat(calculationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Calculation.class);
        Calculation calculation1 = new Calculation();
        calculation1.setId(1L);
        Calculation calculation2 = new Calculation();
        calculation2.setId(calculation1.getId());
        assertThat(calculation1).isEqualTo(calculation2);
        calculation2.setId(2L);
        assertThat(calculation1).isNotEqualTo(calculation2);
        calculation1.setId(null);
        assertThat(calculation1).isNotEqualTo(calculation2);
    }
}
