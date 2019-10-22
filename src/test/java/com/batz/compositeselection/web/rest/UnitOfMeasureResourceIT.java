package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.UnitOfMeasure;
import com.batz.compositeselection.repository.UnitOfMeasureRepository;
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
 * Integration tests for the {@link UnitOfMeasureResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class UnitOfMeasureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

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

    private MockMvc restUnitOfMeasureMockMvc;

    private UnitOfMeasure unitOfMeasure;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnitOfMeasureResource unitOfMeasureResource = new UnitOfMeasureResource(unitOfMeasureRepository);
        this.restUnitOfMeasureMockMvc = MockMvcBuilders.standaloneSetup(unitOfMeasureResource)
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
    public static UnitOfMeasure createEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .name(DEFAULT_NAME);
        return unitOfMeasure;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createUpdatedEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .name(UPDATED_NAME);
        return unitOfMeasure;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasure = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnitOfMeasure() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();

        // Create the UnitOfMeasure
        restUnitOfMeasureMockMvc.perform(post("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isCreated());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate + 1);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createUnitOfMeasureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();

        // Create the UnitOfMeasure with an existing ID
        unitOfMeasure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasureMockMvc.perform(post("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setName(null);

        // Create the UnitOfMeasure, which fails.

        restUnitOfMeasureMockMvc.perform(post("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnitOfMeasures() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures/{id}", unitOfMeasure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnitOfMeasure() throws Exception {
        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(get("/api/unit-of-measures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure
        UnitOfMeasure updatedUnitOfMeasure = unitOfMeasureRepository.findById(unitOfMeasure.getId()).get();
        // Disconnect from session so that the updates on updatedUnitOfMeasure are not directly saved in db
        em.detach(updatedUnitOfMeasure);
        updatedUnitOfMeasure
            .name(UPDATED_NAME);

        restUnitOfMeasureMockMvc.perform(put("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUnitOfMeasure)))
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Create the UnitOfMeasure

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc.perform(put("/api/unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeDelete = unitOfMeasureRepository.findAll().size();

        // Delete the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(delete("/api/unit-of-measures/{id}", unitOfMeasure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasure.class);
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setId(1L);
        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure2.setId(unitOfMeasure1.getId());
        assertThat(unitOfMeasure1).isEqualTo(unitOfMeasure2);
        unitOfMeasure2.setId(2L);
        assertThat(unitOfMeasure1).isNotEqualTo(unitOfMeasure2);
        unitOfMeasure1.setId(null);
        assertThat(unitOfMeasure1).isNotEqualTo(unitOfMeasure2);
    }
}
