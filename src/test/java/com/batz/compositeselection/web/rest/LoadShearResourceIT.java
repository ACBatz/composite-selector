package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.LoadShear;
import com.batz.compositeselection.repository.LoadShearRepository;
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

import com.batz.compositeselection.domain.enumeration.LoadShearEnum;
/**
 * Integration tests for the {@link LoadShearResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class LoadShearResourceIT {

    private static final LoadShearEnum DEFAULT_LOAD_SHEAR_ENUM = LoadShearEnum.INPLANE;
    private static final LoadShearEnum UPDATED_LOAD_SHEAR_ENUM = LoadShearEnum.INTERLAMINAR;

    @Autowired
    private LoadShearRepository loadShearRepository;

    @Mock
    private LoadShearRepository loadShearRepositoryMock;

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

    private MockMvc restLoadShearMockMvc;

    private LoadShear loadShear;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoadShearResource loadShearResource = new LoadShearResource(loadShearRepository);
        this.restLoadShearMockMvc = MockMvcBuilders.standaloneSetup(loadShearResource)
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
    public static LoadShear createEntity(EntityManager em) {
        LoadShear loadShear = new LoadShear()
            .loadShearEnum(DEFAULT_LOAD_SHEAR_ENUM);
        return loadShear;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoadShear createUpdatedEntity(EntityManager em) {
        LoadShear loadShear = new LoadShear()
            .loadShearEnum(UPDATED_LOAD_SHEAR_ENUM);
        return loadShear;
    }

    @BeforeEach
    public void initTest() {
        loadShear = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoadShear() throws Exception {
        int databaseSizeBeforeCreate = loadShearRepository.findAll().size();

        // Create the LoadShear
        restLoadShearMockMvc.perform(post("/api/load-shears")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadShear)))
            .andExpect(status().isCreated());

        // Validate the LoadShear in the database
        List<LoadShear> loadShearList = loadShearRepository.findAll();
        assertThat(loadShearList).hasSize(databaseSizeBeforeCreate + 1);
        LoadShear testLoadShear = loadShearList.get(loadShearList.size() - 1);
        assertThat(testLoadShear.getLoadShearEnum()).isEqualTo(DEFAULT_LOAD_SHEAR_ENUM);
    }

    @Test
    @Transactional
    public void createLoadShearWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loadShearRepository.findAll().size();

        // Create the LoadShear with an existing ID
        loadShear.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoadShearMockMvc.perform(post("/api/load-shears")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadShear)))
            .andExpect(status().isBadRequest());

        // Validate the LoadShear in the database
        List<LoadShear> loadShearList = loadShearRepository.findAll();
        assertThat(loadShearList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLoadShearEnumIsRequired() throws Exception {
        int databaseSizeBeforeTest = loadShearRepository.findAll().size();
        // set the field null
        loadShear.setLoadShearEnum(null);

        // Create the LoadShear, which fails.

        restLoadShearMockMvc.perform(post("/api/load-shears")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadShear)))
            .andExpect(status().isBadRequest());

        List<LoadShear> loadShearList = loadShearRepository.findAll();
        assertThat(loadShearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLoadShears() throws Exception {
        // Initialize the database
        loadShearRepository.saveAndFlush(loadShear);

        // Get all the loadShearList
        restLoadShearMockMvc.perform(get("/api/load-shears?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loadShear.getId().intValue())))
            .andExpect(jsonPath("$.[*].loadShearEnum").value(hasItem(DEFAULT_LOAD_SHEAR_ENUM.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllLoadShearsWithEagerRelationshipsIsEnabled() throws Exception {
        LoadShearResource loadShearResource = new LoadShearResource(loadShearRepositoryMock);
        when(loadShearRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restLoadShearMockMvc = MockMvcBuilders.standaloneSetup(loadShearResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLoadShearMockMvc.perform(get("/api/load-shears?eagerload=true"))
        .andExpect(status().isOk());

        verify(loadShearRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllLoadShearsWithEagerRelationshipsIsNotEnabled() throws Exception {
        LoadShearResource loadShearResource = new LoadShearResource(loadShearRepositoryMock);
            when(loadShearRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restLoadShearMockMvc = MockMvcBuilders.standaloneSetup(loadShearResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLoadShearMockMvc.perform(get("/api/load-shears?eagerload=true"))
        .andExpect(status().isOk());

            verify(loadShearRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getLoadShear() throws Exception {
        // Initialize the database
        loadShearRepository.saveAndFlush(loadShear);

        // Get the loadShear
        restLoadShearMockMvc.perform(get("/api/load-shears/{id}", loadShear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loadShear.getId().intValue()))
            .andExpect(jsonPath("$.loadShearEnum").value(DEFAULT_LOAD_SHEAR_ENUM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLoadShear() throws Exception {
        // Get the loadShear
        restLoadShearMockMvc.perform(get("/api/load-shears/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoadShear() throws Exception {
        // Initialize the database
        loadShearRepository.saveAndFlush(loadShear);

        int databaseSizeBeforeUpdate = loadShearRepository.findAll().size();

        // Update the loadShear
        LoadShear updatedLoadShear = loadShearRepository.findById(loadShear.getId()).get();
        // Disconnect from session so that the updates on updatedLoadShear are not directly saved in db
        em.detach(updatedLoadShear);
        updatedLoadShear
            .loadShearEnum(UPDATED_LOAD_SHEAR_ENUM);

        restLoadShearMockMvc.perform(put("/api/load-shears")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLoadShear)))
            .andExpect(status().isOk());

        // Validate the LoadShear in the database
        List<LoadShear> loadShearList = loadShearRepository.findAll();
        assertThat(loadShearList).hasSize(databaseSizeBeforeUpdate);
        LoadShear testLoadShear = loadShearList.get(loadShearList.size() - 1);
        assertThat(testLoadShear.getLoadShearEnum()).isEqualTo(UPDATED_LOAD_SHEAR_ENUM);
    }

    @Test
    @Transactional
    public void updateNonExistingLoadShear() throws Exception {
        int databaseSizeBeforeUpdate = loadShearRepository.findAll().size();

        // Create the LoadShear

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoadShearMockMvc.perform(put("/api/load-shears")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadShear)))
            .andExpect(status().isBadRequest());

        // Validate the LoadShear in the database
        List<LoadShear> loadShearList = loadShearRepository.findAll();
        assertThat(loadShearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLoadShear() throws Exception {
        // Initialize the database
        loadShearRepository.saveAndFlush(loadShear);

        int databaseSizeBeforeDelete = loadShearRepository.findAll().size();

        // Delete the loadShear
        restLoadShearMockMvc.perform(delete("/api/load-shears/{id}", loadShear.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoadShear> loadShearList = loadShearRepository.findAll();
        assertThat(loadShearList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoadShear.class);
        LoadShear loadShear1 = new LoadShear();
        loadShear1.setId(1L);
        LoadShear loadShear2 = new LoadShear();
        loadShear2.setId(loadShear1.getId());
        assertThat(loadShear1).isEqualTo(loadShear2);
        loadShear2.setId(2L);
        assertThat(loadShear1).isNotEqualTo(loadShear2);
        loadShear1.setId(null);
        assertThat(loadShear1).isNotEqualTo(loadShear2);
    }
}
