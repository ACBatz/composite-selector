package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.Composite;
import com.batz.compositeselection.repository.CompositeRepository;
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
 * Integration tests for the {@link CompositeResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class CompositeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CompositeRepository compositeRepository;

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

    private MockMvc restCompositeMockMvc;

    private Composite composite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompositeResource compositeResource = new CompositeResource(compositeRepository);
        this.restCompositeMockMvc = MockMvcBuilders.standaloneSetup(compositeResource)
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
    public static Composite createEntity(EntityManager em) {
        Composite composite = new Composite()
            .name(DEFAULT_NAME);
        return composite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Composite createUpdatedEntity(EntityManager em) {
        Composite composite = new Composite()
            .name(UPDATED_NAME);
        return composite;
    }

    @BeforeEach
    public void initTest() {
        composite = createEntity(em);
    }

    @Test
    @Transactional
    public void createComposite() throws Exception {
        int databaseSizeBeforeCreate = compositeRepository.findAll().size();

        // Create the Composite
        restCompositeMockMvc.perform(post("/api/composites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(composite)))
            .andExpect(status().isCreated());

        // Validate the Composite in the database
        List<Composite> compositeList = compositeRepository.findAll();
        assertThat(compositeList).hasSize(databaseSizeBeforeCreate + 1);
        Composite testComposite = compositeList.get(compositeList.size() - 1);
        assertThat(testComposite.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCompositeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = compositeRepository.findAll().size();

        // Create the Composite with an existing ID
        composite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompositeMockMvc.perform(post("/api/composites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(composite)))
            .andExpect(status().isBadRequest());

        // Validate the Composite in the database
        List<Composite> compositeList = compositeRepository.findAll();
        assertThat(compositeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = compositeRepository.findAll().size();
        // set the field null
        composite.setName(null);

        // Create the Composite, which fails.

        restCompositeMockMvc.perform(post("/api/composites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(composite)))
            .andExpect(status().isBadRequest());

        List<Composite> compositeList = compositeRepository.findAll();
        assertThat(compositeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComposites() throws Exception {
        // Initialize the database
        compositeRepository.saveAndFlush(composite);

        // Get all the compositeList
        restCompositeMockMvc.perform(get("/api/composites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(composite.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getComposite() throws Exception {
        // Initialize the database
        compositeRepository.saveAndFlush(composite);

        // Get the composite
        restCompositeMockMvc.perform(get("/api/composites/{id}", composite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(composite.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComposite() throws Exception {
        // Get the composite
        restCompositeMockMvc.perform(get("/api/composites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComposite() throws Exception {
        // Initialize the database
        compositeRepository.saveAndFlush(composite);

        int databaseSizeBeforeUpdate = compositeRepository.findAll().size();

        // Update the composite
        Composite updatedComposite = compositeRepository.findById(composite.getId()).get();
        // Disconnect from session so that the updates on updatedComposite are not directly saved in db
        em.detach(updatedComposite);
        updatedComposite
            .name(UPDATED_NAME);

        restCompositeMockMvc.perform(put("/api/composites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedComposite)))
            .andExpect(status().isOk());

        // Validate the Composite in the database
        List<Composite> compositeList = compositeRepository.findAll();
        assertThat(compositeList).hasSize(databaseSizeBeforeUpdate);
        Composite testComposite = compositeList.get(compositeList.size() - 1);
        assertThat(testComposite.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingComposite() throws Exception {
        int databaseSizeBeforeUpdate = compositeRepository.findAll().size();

        // Create the Composite

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompositeMockMvc.perform(put("/api/composites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(composite)))
            .andExpect(status().isBadRequest());

        // Validate the Composite in the database
        List<Composite> compositeList = compositeRepository.findAll();
        assertThat(compositeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteComposite() throws Exception {
        // Initialize the database
        compositeRepository.saveAndFlush(composite);

        int databaseSizeBeforeDelete = compositeRepository.findAll().size();

        // Delete the composite
        restCompositeMockMvc.perform(delete("/api/composites/{id}", composite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Composite> compositeList = compositeRepository.findAll();
        assertThat(compositeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Composite.class);
        Composite composite1 = new Composite();
        composite1.setId(1L);
        Composite composite2 = new Composite();
        composite2.setId(composite1.getId());
        assertThat(composite1).isEqualTo(composite2);
        composite2.setId(2L);
        assertThat(composite1).isNotEqualTo(composite2);
        composite1.setId(null);
        assertThat(composite1).isNotEqualTo(composite2);
    }
}
