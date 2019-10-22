package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.LoadDirection;
import com.batz.compositeselection.repository.LoadDirectionRepository;
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

import com.batz.compositeselection.domain.enumeration.LoadDirectionEnum;
/**
 * Integration tests for the {@link LoadDirectionResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class LoadDirectionResourceIT {

    private static final LoadDirectionEnum DEFAULT_LOAD_DIRECTION_ENUM = LoadDirectionEnum.LONGITUDINAL;
    private static final LoadDirectionEnum UPDATED_LOAD_DIRECTION_ENUM = LoadDirectionEnum.TRANSVERSE;

    @Autowired
    private LoadDirectionRepository loadDirectionRepository;

    @Mock
    private LoadDirectionRepository loadDirectionRepositoryMock;

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

    private MockMvc restLoadDirectionMockMvc;

    private LoadDirection loadDirection;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoadDirectionResource loadDirectionResource = new LoadDirectionResource(loadDirectionRepository);
        this.restLoadDirectionMockMvc = MockMvcBuilders.standaloneSetup(loadDirectionResource)
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
    public static LoadDirection createEntity(EntityManager em) {
        LoadDirection loadDirection = new LoadDirection()
            .loadDirectionEnum(DEFAULT_LOAD_DIRECTION_ENUM);
        return loadDirection;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoadDirection createUpdatedEntity(EntityManager em) {
        LoadDirection loadDirection = new LoadDirection()
            .loadDirectionEnum(UPDATED_LOAD_DIRECTION_ENUM);
        return loadDirection;
    }

    @BeforeEach
    public void initTest() {
        loadDirection = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoadDirection() throws Exception {
        int databaseSizeBeforeCreate = loadDirectionRepository.findAll().size();

        // Create the LoadDirection
        restLoadDirectionMockMvc.perform(post("/api/load-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadDirection)))
            .andExpect(status().isCreated());

        // Validate the LoadDirection in the database
        List<LoadDirection> loadDirectionList = loadDirectionRepository.findAll();
        assertThat(loadDirectionList).hasSize(databaseSizeBeforeCreate + 1);
        LoadDirection testLoadDirection = loadDirectionList.get(loadDirectionList.size() - 1);
        assertThat(testLoadDirection.getLoadDirectionEnum()).isEqualTo(DEFAULT_LOAD_DIRECTION_ENUM);
    }

    @Test
    @Transactional
    public void createLoadDirectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loadDirectionRepository.findAll().size();

        // Create the LoadDirection with an existing ID
        loadDirection.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoadDirectionMockMvc.perform(post("/api/load-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadDirection)))
            .andExpect(status().isBadRequest());

        // Validate the LoadDirection in the database
        List<LoadDirection> loadDirectionList = loadDirectionRepository.findAll();
        assertThat(loadDirectionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLoadDirectionEnumIsRequired() throws Exception {
        int databaseSizeBeforeTest = loadDirectionRepository.findAll().size();
        // set the field null
        loadDirection.setLoadDirectionEnum(null);

        // Create the LoadDirection, which fails.

        restLoadDirectionMockMvc.perform(post("/api/load-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadDirection)))
            .andExpect(status().isBadRequest());

        List<LoadDirection> loadDirectionList = loadDirectionRepository.findAll();
        assertThat(loadDirectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLoadDirections() throws Exception {
        // Initialize the database
        loadDirectionRepository.saveAndFlush(loadDirection);

        // Get all the loadDirectionList
        restLoadDirectionMockMvc.perform(get("/api/load-directions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loadDirection.getId().intValue())))
            .andExpect(jsonPath("$.[*].loadDirectionEnum").value(hasItem(DEFAULT_LOAD_DIRECTION_ENUM.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllLoadDirectionsWithEagerRelationshipsIsEnabled() throws Exception {
        LoadDirectionResource loadDirectionResource = new LoadDirectionResource(loadDirectionRepositoryMock);
        when(loadDirectionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restLoadDirectionMockMvc = MockMvcBuilders.standaloneSetup(loadDirectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLoadDirectionMockMvc.perform(get("/api/load-directions?eagerload=true"))
        .andExpect(status().isOk());

        verify(loadDirectionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllLoadDirectionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        LoadDirectionResource loadDirectionResource = new LoadDirectionResource(loadDirectionRepositoryMock);
            when(loadDirectionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restLoadDirectionMockMvc = MockMvcBuilders.standaloneSetup(loadDirectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLoadDirectionMockMvc.perform(get("/api/load-directions?eagerload=true"))
        .andExpect(status().isOk());

            verify(loadDirectionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getLoadDirection() throws Exception {
        // Initialize the database
        loadDirectionRepository.saveAndFlush(loadDirection);

        // Get the loadDirection
        restLoadDirectionMockMvc.perform(get("/api/load-directions/{id}", loadDirection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loadDirection.getId().intValue()))
            .andExpect(jsonPath("$.loadDirectionEnum").value(DEFAULT_LOAD_DIRECTION_ENUM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLoadDirection() throws Exception {
        // Get the loadDirection
        restLoadDirectionMockMvc.perform(get("/api/load-directions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoadDirection() throws Exception {
        // Initialize the database
        loadDirectionRepository.saveAndFlush(loadDirection);

        int databaseSizeBeforeUpdate = loadDirectionRepository.findAll().size();

        // Update the loadDirection
        LoadDirection updatedLoadDirection = loadDirectionRepository.findById(loadDirection.getId()).get();
        // Disconnect from session so that the updates on updatedLoadDirection are not directly saved in db
        em.detach(updatedLoadDirection);
        updatedLoadDirection
            .loadDirectionEnum(UPDATED_LOAD_DIRECTION_ENUM);

        restLoadDirectionMockMvc.perform(put("/api/load-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLoadDirection)))
            .andExpect(status().isOk());

        // Validate the LoadDirection in the database
        List<LoadDirection> loadDirectionList = loadDirectionRepository.findAll();
        assertThat(loadDirectionList).hasSize(databaseSizeBeforeUpdate);
        LoadDirection testLoadDirection = loadDirectionList.get(loadDirectionList.size() - 1);
        assertThat(testLoadDirection.getLoadDirectionEnum()).isEqualTo(UPDATED_LOAD_DIRECTION_ENUM);
    }

    @Test
    @Transactional
    public void updateNonExistingLoadDirection() throws Exception {
        int databaseSizeBeforeUpdate = loadDirectionRepository.findAll().size();

        // Create the LoadDirection

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoadDirectionMockMvc.perform(put("/api/load-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loadDirection)))
            .andExpect(status().isBadRequest());

        // Validate the LoadDirection in the database
        List<LoadDirection> loadDirectionList = loadDirectionRepository.findAll();
        assertThat(loadDirectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLoadDirection() throws Exception {
        // Initialize the database
        loadDirectionRepository.saveAndFlush(loadDirection);

        int databaseSizeBeforeDelete = loadDirectionRepository.findAll().size();

        // Delete the loadDirection
        restLoadDirectionMockMvc.perform(delete("/api/load-directions/{id}", loadDirection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoadDirection> loadDirectionList = loadDirectionRepository.findAll();
        assertThat(loadDirectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoadDirection.class);
        LoadDirection loadDirection1 = new LoadDirection();
        loadDirection1.setId(1L);
        LoadDirection loadDirection2 = new LoadDirection();
        loadDirection2.setId(loadDirection1.getId());
        assertThat(loadDirection1).isEqualTo(loadDirection2);
        loadDirection2.setId(2L);
        assertThat(loadDirection1).isNotEqualTo(loadDirection2);
        loadDirection1.setId(null);
        assertThat(loadDirection1).isNotEqualTo(loadDirection2);
    }
}
