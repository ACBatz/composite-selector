package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.Verb;
import com.batz.compositeselection.repository.VerbRepository;
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
 * Integration tests for the {@link VerbResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class VerbResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private VerbRepository verbRepository;

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

    private MockMvc restVerbMockMvc;

    private Verb verb;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VerbResource verbResource = new VerbResource(verbRepository);
        this.restVerbMockMvc = MockMvcBuilders.standaloneSetup(verbResource)
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
    public static Verb createEntity(EntityManager em) {
        Verb verb = new Verb()
            .name(DEFAULT_NAME);
        return verb;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verb createUpdatedEntity(EntityManager em) {
        Verb verb = new Verb()
            .name(UPDATED_NAME);
        return verb;
    }

    @BeforeEach
    public void initTest() {
        verb = createEntity(em);
    }

    @Test
    @Transactional
    public void createVerb() throws Exception {
        int databaseSizeBeforeCreate = verbRepository.findAll().size();

        // Create the Verb
        restVerbMockMvc.perform(post("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isCreated());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeCreate + 1);
        Verb testVerb = verbList.get(verbList.size() - 1);
        assertThat(testVerb.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createVerbWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = verbRepository.findAll().size();

        // Create the Verb with an existing ID
        verb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVerbMockMvc.perform(post("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isBadRequest());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = verbRepository.findAll().size();
        // set the field null
        verb.setName(null);

        // Create the Verb, which fails.

        restVerbMockMvc.perform(post("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isBadRequest());

        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVerbs() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Get all the verbList
        restVerbMockMvc.perform(get("/api/verbs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verb.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Get the verb
        restVerbMockMvc.perform(get("/api/verbs/{id}", verb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(verb.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVerb() throws Exception {
        // Get the verb
        restVerbMockMvc.perform(get("/api/verbs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        int databaseSizeBeforeUpdate = verbRepository.findAll().size();

        // Update the verb
        Verb updatedVerb = verbRepository.findById(verb.getId()).get();
        // Disconnect from session so that the updates on updatedVerb are not directly saved in db
        em.detach(updatedVerb);
        updatedVerb
            .name(UPDATED_NAME);

        restVerbMockMvc.perform(put("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVerb)))
            .andExpect(status().isOk());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeUpdate);
        Verb testVerb = verbList.get(verbList.size() - 1);
        assertThat(testVerb.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingVerb() throws Exception {
        int databaseSizeBeforeUpdate = verbRepository.findAll().size();

        // Create the Verb

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVerbMockMvc.perform(put("/api/verbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(verb)))
            .andExpect(status().isBadRequest());

        // Validate the Verb in the database
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        int databaseSizeBeforeDelete = verbRepository.findAll().size();

        // Delete the verb
        restVerbMockMvc.perform(delete("/api/verbs/{id}", verb.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Verb> verbList = verbRepository.findAll();
        assertThat(verbList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Verb.class);
        Verb verb1 = new Verb();
        verb1.setId(1L);
        Verb verb2 = new Verb();
        verb2.setId(verb1.getId());
        assertThat(verb1).isEqualTo(verb2);
        verb2.setId(2L);
        assertThat(verb1).isNotEqualTo(verb2);
        verb1.setId(null);
        assertThat(verb1).isNotEqualTo(verb2);
    }
}
