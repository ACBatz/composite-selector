package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.Noun;
import com.batz.compositeselection.repository.NounRepository;
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
 * Integration tests for the {@link NounResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class NounResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private NounRepository nounRepository;

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

    private MockMvc restNounMockMvc;

    private Noun noun;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NounResource nounResource = new NounResource(nounRepository);
        this.restNounMockMvc = MockMvcBuilders.standaloneSetup(nounResource)
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
    public static Noun createEntity(EntityManager em) {
        Noun noun = new Noun()
            .name(DEFAULT_NAME);
        return noun;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Noun createUpdatedEntity(EntityManager em) {
        Noun noun = new Noun()
            .name(UPDATED_NAME);
        return noun;
    }

    @BeforeEach
    public void initTest() {
        noun = createEntity(em);
    }

    @Test
    @Transactional
    public void createNoun() throws Exception {
        int databaseSizeBeforeCreate = nounRepository.findAll().size();

        // Create the Noun
        restNounMockMvc.perform(post("/api/nouns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noun)))
            .andExpect(status().isCreated());

        // Validate the Noun in the database
        List<Noun> nounList = nounRepository.findAll();
        assertThat(nounList).hasSize(databaseSizeBeforeCreate + 1);
        Noun testNoun = nounList.get(nounList.size() - 1);
        assertThat(testNoun.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createNounWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nounRepository.findAll().size();

        // Create the Noun with an existing ID
        noun.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNounMockMvc.perform(post("/api/nouns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noun)))
            .andExpect(status().isBadRequest());

        // Validate the Noun in the database
        List<Noun> nounList = nounRepository.findAll();
        assertThat(nounList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nounRepository.findAll().size();
        // set the field null
        noun.setName(null);

        // Create the Noun, which fails.

        restNounMockMvc.perform(post("/api/nouns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noun)))
            .andExpect(status().isBadRequest());

        List<Noun> nounList = nounRepository.findAll();
        assertThat(nounList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNouns() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        // Get all the nounList
        restNounMockMvc.perform(get("/api/nouns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(noun.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getNoun() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        // Get the noun
        restNounMockMvc.perform(get("/api/nouns/{id}", noun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(noun.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNoun() throws Exception {
        // Get the noun
        restNounMockMvc.perform(get("/api/nouns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNoun() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        int databaseSizeBeforeUpdate = nounRepository.findAll().size();

        // Update the noun
        Noun updatedNoun = nounRepository.findById(noun.getId()).get();
        // Disconnect from session so that the updates on updatedNoun are not directly saved in db
        em.detach(updatedNoun);
        updatedNoun
            .name(UPDATED_NAME);

        restNounMockMvc.perform(put("/api/nouns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNoun)))
            .andExpect(status().isOk());

        // Validate the Noun in the database
        List<Noun> nounList = nounRepository.findAll();
        assertThat(nounList).hasSize(databaseSizeBeforeUpdate);
        Noun testNoun = nounList.get(nounList.size() - 1);
        assertThat(testNoun.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingNoun() throws Exception {
        int databaseSizeBeforeUpdate = nounRepository.findAll().size();

        // Create the Noun

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNounMockMvc.perform(put("/api/nouns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noun)))
            .andExpect(status().isBadRequest());

        // Validate the Noun in the database
        List<Noun> nounList = nounRepository.findAll();
        assertThat(nounList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNoun() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        int databaseSizeBeforeDelete = nounRepository.findAll().size();

        // Delete the noun
        restNounMockMvc.perform(delete("/api/nouns/{id}", noun.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Noun> nounList = nounRepository.findAll();
        assertThat(nounList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Noun.class);
        Noun noun1 = new Noun();
        noun1.setId(1L);
        Noun noun2 = new Noun();
        noun2.setId(noun1.getId());
        assertThat(noun1).isEqualTo(noun2);
        noun2.setId(2L);
        assertThat(noun1).isNotEqualTo(noun2);
        noun1.setId(null);
        assertThat(noun1).isNotEqualTo(noun2);
    }
}
