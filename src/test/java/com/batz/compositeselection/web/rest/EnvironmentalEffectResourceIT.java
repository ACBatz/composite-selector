package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.EnvironmentalEffect;
import com.batz.compositeselection.repository.EnvironmentalEffectRepository;
import com.batz.compositeselection.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.batz.compositeselection.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EnvironmentalEffectResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class EnvironmentalEffectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EnvironmentalEffectRepository environmentalEffectRepository;

    @Mock
    private EnvironmentalEffectRepository environmentalEffectRepositoryMock;

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

    private MockMvc restEnvironmentalEffectMockMvc;

    private EnvironmentalEffect environmentalEffect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EnvironmentalEffectResource environmentalEffectResource = new EnvironmentalEffectResource(environmentalEffectRepository);
        this.restEnvironmentalEffectMockMvc = MockMvcBuilders.standaloneSetup(environmentalEffectResource)
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
    public static EnvironmentalEffect createEntity(EntityManager em) {
        EnvironmentalEffect environmentalEffect = new EnvironmentalEffect()
            .name(DEFAULT_NAME);
        return environmentalEffect;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnvironmentalEffect createUpdatedEntity(EntityManager em) {
        EnvironmentalEffect environmentalEffect = new EnvironmentalEffect()
            .name(UPDATED_NAME);
        return environmentalEffect;
    }

    @BeforeEach
    public void initTest() {
        environmentalEffect = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnvironmentalEffect() throws Exception {
        int databaseSizeBeforeCreate = environmentalEffectRepository.findAll().size();

        // Create the EnvironmentalEffect
        restEnvironmentalEffectMockMvc.perform(post("/api/environmental-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(environmentalEffect)))
            .andExpect(status().isCreated());

        // Validate the EnvironmentalEffect in the database
        List<EnvironmentalEffect> environmentalEffectList = environmentalEffectRepository.findAll();
        assertThat(environmentalEffectList).hasSize(databaseSizeBeforeCreate + 1);
        EnvironmentalEffect testEnvironmentalEffect = environmentalEffectList.get(environmentalEffectList.size() - 1);
        assertThat(testEnvironmentalEffect.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEnvironmentalEffectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = environmentalEffectRepository.findAll().size();

        // Create the EnvironmentalEffect with an existing ID
        environmentalEffect.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnvironmentalEffectMockMvc.perform(post("/api/environmental-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(environmentalEffect)))
            .andExpect(status().isBadRequest());

        // Validate the EnvironmentalEffect in the database
        List<EnvironmentalEffect> environmentalEffectList = environmentalEffectRepository.findAll();
        assertThat(environmentalEffectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = environmentalEffectRepository.findAll().size();
        // set the field null
        environmentalEffect.setName(null);

        // Create the EnvironmentalEffect, which fails.

        restEnvironmentalEffectMockMvc.perform(post("/api/environmental-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(environmentalEffect)))
            .andExpect(status().isBadRequest());

        List<EnvironmentalEffect> environmentalEffectList = environmentalEffectRepository.findAll();
        assertThat(environmentalEffectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnvironmentalEffects() throws Exception {
        // Initialize the database
        environmentalEffectRepository.saveAndFlush(environmentalEffect);

        // Get all the environmentalEffectList
        restEnvironmentalEffectMockMvc.perform(get("/api/environmental-effects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(environmentalEffect.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllEnvironmentalEffectsWithEagerRelationshipsIsEnabled() throws Exception {
        EnvironmentalEffectResource environmentalEffectResource = new EnvironmentalEffectResource(environmentalEffectRepositoryMock);
        when(environmentalEffectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEnvironmentalEffectMockMvc = MockMvcBuilders.standaloneSetup(environmentalEffectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEnvironmentalEffectMockMvc.perform(get("/api/environmental-effects?eagerload=true"))
        .andExpect(status().isOk());

        verify(environmentalEffectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllEnvironmentalEffectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        EnvironmentalEffectResource environmentalEffectResource = new EnvironmentalEffectResource(environmentalEffectRepositoryMock);
            when(environmentalEffectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEnvironmentalEffectMockMvc = MockMvcBuilders.standaloneSetup(environmentalEffectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEnvironmentalEffectMockMvc.perform(get("/api/environmental-effects?eagerload=true"))
        .andExpect(status().isOk());

            verify(environmentalEffectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEnvironmentalEffect() throws Exception {
        // Initialize the database
        environmentalEffectRepository.saveAndFlush(environmentalEffect);

        // Get the environmentalEffect
        restEnvironmentalEffectMockMvc.perform(get("/api/environmental-effects/{id}", environmentalEffect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(environmentalEffect.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnvironmentalEffect() throws Exception {
        // Get the environmentalEffect
        restEnvironmentalEffectMockMvc.perform(get("/api/environmental-effects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnvironmentalEffect() throws Exception {
        // Initialize the database
        environmentalEffectRepository.saveAndFlush(environmentalEffect);

        int databaseSizeBeforeUpdate = environmentalEffectRepository.findAll().size();

        // Update the environmentalEffect
        EnvironmentalEffect updatedEnvironmentalEffect = environmentalEffectRepository.findById(environmentalEffect.getId()).get();
        // Disconnect from session so that the updates on updatedEnvironmentalEffect are not directly saved in db
        em.detach(updatedEnvironmentalEffect);
        updatedEnvironmentalEffect
            .name(UPDATED_NAME);

        restEnvironmentalEffectMockMvc.perform(put("/api/environmental-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEnvironmentalEffect)))
            .andExpect(status().isOk());

        // Validate the EnvironmentalEffect in the database
        List<EnvironmentalEffect> environmentalEffectList = environmentalEffectRepository.findAll();
        assertThat(environmentalEffectList).hasSize(databaseSizeBeforeUpdate);
        EnvironmentalEffect testEnvironmentalEffect = environmentalEffectList.get(environmentalEffectList.size() - 1);
        assertThat(testEnvironmentalEffect.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEnvironmentalEffect() throws Exception {
        int databaseSizeBeforeUpdate = environmentalEffectRepository.findAll().size();

        // Create the EnvironmentalEffect

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentalEffectMockMvc.perform(put("/api/environmental-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(environmentalEffect)))
            .andExpect(status().isBadRequest());

        // Validate the EnvironmentalEffect in the database
        List<EnvironmentalEffect> environmentalEffectList = environmentalEffectRepository.findAll();
        assertThat(environmentalEffectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEnvironmentalEffect() throws Exception {
        // Initialize the database
        environmentalEffectRepository.saveAndFlush(environmentalEffect);

        int databaseSizeBeforeDelete = environmentalEffectRepository.findAll().size();

        // Delete the environmentalEffect
        restEnvironmentalEffectMockMvc.perform(delete("/api/environmental-effects/{id}", environmentalEffect.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EnvironmentalEffect> environmentalEffectList = environmentalEffectRepository.findAll();
        assertThat(environmentalEffectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnvironmentalEffect.class);
        EnvironmentalEffect environmentalEffect1 = new EnvironmentalEffect();
        environmentalEffect1.setId(1L);
        EnvironmentalEffect environmentalEffect2 = new EnvironmentalEffect();
        environmentalEffect2.setId(environmentalEffect1.getId());
        assertThat(environmentalEffect1).isEqualTo(environmentalEffect2);
        environmentalEffect2.setId(2L);
        assertThat(environmentalEffect1).isNotEqualTo(environmentalEffect2);
        environmentalEffect1.setId(null);
        assertThat(environmentalEffect1).isNotEqualTo(environmentalEffect2);
    }
}
