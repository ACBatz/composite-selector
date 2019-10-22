package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.MiscellaneousConstraint;
import com.batz.compositeselection.repository.MiscellaneousConstraintRepository;
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
 * Integration tests for the {@link MiscellaneousConstraintResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class MiscellaneousConstraintResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MAXIMIZE = false;
    private static final Boolean UPDATED_MAXIMIZE = true;

    private static final Boolean DEFAULT_MINIMIZE = false;
    private static final Boolean UPDATED_MINIMIZE = true;

    @Autowired
    private MiscellaneousConstraintRepository miscellaneousConstraintRepository;

    @Mock
    private MiscellaneousConstraintRepository miscellaneousConstraintRepositoryMock;

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

    private MockMvc restMiscellaneousConstraintMockMvc;

    private MiscellaneousConstraint miscellaneousConstraint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MiscellaneousConstraintResource miscellaneousConstraintResource = new MiscellaneousConstraintResource(miscellaneousConstraintRepository);
        this.restMiscellaneousConstraintMockMvc = MockMvcBuilders.standaloneSetup(miscellaneousConstraintResource)
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
    public static MiscellaneousConstraint createEntity(EntityManager em) {
        MiscellaneousConstraint miscellaneousConstraint = new MiscellaneousConstraint()
            .name(DEFAULT_NAME)
            .maximize(DEFAULT_MAXIMIZE)
            .minimize(DEFAULT_MINIMIZE);
        return miscellaneousConstraint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MiscellaneousConstraint createUpdatedEntity(EntityManager em) {
        MiscellaneousConstraint miscellaneousConstraint = new MiscellaneousConstraint()
            .name(UPDATED_NAME)
            .maximize(UPDATED_MAXIMIZE)
            .minimize(UPDATED_MINIMIZE);
        return miscellaneousConstraint;
    }

    @BeforeEach
    public void initTest() {
        miscellaneousConstraint = createEntity(em);
    }

    @Test
    @Transactional
    public void createMiscellaneousConstraint() throws Exception {
        int databaseSizeBeforeCreate = miscellaneousConstraintRepository.findAll().size();

        // Create the MiscellaneousConstraint
        restMiscellaneousConstraintMockMvc.perform(post("/api/miscellaneous-constraints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(miscellaneousConstraint)))
            .andExpect(status().isCreated());

        // Validate the MiscellaneousConstraint in the database
        List<MiscellaneousConstraint> miscellaneousConstraintList = miscellaneousConstraintRepository.findAll();
        assertThat(miscellaneousConstraintList).hasSize(databaseSizeBeforeCreate + 1);
        MiscellaneousConstraint testMiscellaneousConstraint = miscellaneousConstraintList.get(miscellaneousConstraintList.size() - 1);
        assertThat(testMiscellaneousConstraint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMiscellaneousConstraint.isMaximize()).isEqualTo(DEFAULT_MAXIMIZE);
        assertThat(testMiscellaneousConstraint.isMinimize()).isEqualTo(DEFAULT_MINIMIZE);
    }

    @Test
    @Transactional
    public void createMiscellaneousConstraintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = miscellaneousConstraintRepository.findAll().size();

        // Create the MiscellaneousConstraint with an existing ID
        miscellaneousConstraint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMiscellaneousConstraintMockMvc.perform(post("/api/miscellaneous-constraints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(miscellaneousConstraint)))
            .andExpect(status().isBadRequest());

        // Validate the MiscellaneousConstraint in the database
        List<MiscellaneousConstraint> miscellaneousConstraintList = miscellaneousConstraintRepository.findAll();
        assertThat(miscellaneousConstraintList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = miscellaneousConstraintRepository.findAll().size();
        // set the field null
        miscellaneousConstraint.setName(null);

        // Create the MiscellaneousConstraint, which fails.

        restMiscellaneousConstraintMockMvc.perform(post("/api/miscellaneous-constraints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(miscellaneousConstraint)))
            .andExpect(status().isBadRequest());

        List<MiscellaneousConstraint> miscellaneousConstraintList = miscellaneousConstraintRepository.findAll();
        assertThat(miscellaneousConstraintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMiscellaneousConstraints() throws Exception {
        // Initialize the database
        miscellaneousConstraintRepository.saveAndFlush(miscellaneousConstraint);

        // Get all the miscellaneousConstraintList
        restMiscellaneousConstraintMockMvc.perform(get("/api/miscellaneous-constraints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(miscellaneousConstraint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].maximize").value(hasItem(DEFAULT_MAXIMIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].minimize").value(hasItem(DEFAULT_MINIMIZE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllMiscellaneousConstraintsWithEagerRelationshipsIsEnabled() throws Exception {
        MiscellaneousConstraintResource miscellaneousConstraintResource = new MiscellaneousConstraintResource(miscellaneousConstraintRepositoryMock);
        when(miscellaneousConstraintRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMiscellaneousConstraintMockMvc = MockMvcBuilders.standaloneSetup(miscellaneousConstraintResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMiscellaneousConstraintMockMvc.perform(get("/api/miscellaneous-constraints?eagerload=true"))
        .andExpect(status().isOk());

        verify(miscellaneousConstraintRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllMiscellaneousConstraintsWithEagerRelationshipsIsNotEnabled() throws Exception {
        MiscellaneousConstraintResource miscellaneousConstraintResource = new MiscellaneousConstraintResource(miscellaneousConstraintRepositoryMock);
            when(miscellaneousConstraintRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMiscellaneousConstraintMockMvc = MockMvcBuilders.standaloneSetup(miscellaneousConstraintResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMiscellaneousConstraintMockMvc.perform(get("/api/miscellaneous-constraints?eagerload=true"))
        .andExpect(status().isOk());

            verify(miscellaneousConstraintRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMiscellaneousConstraint() throws Exception {
        // Initialize the database
        miscellaneousConstraintRepository.saveAndFlush(miscellaneousConstraint);

        // Get the miscellaneousConstraint
        restMiscellaneousConstraintMockMvc.perform(get("/api/miscellaneous-constraints/{id}", miscellaneousConstraint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(miscellaneousConstraint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.maximize").value(DEFAULT_MAXIMIZE.booleanValue()))
            .andExpect(jsonPath("$.minimize").value(DEFAULT_MINIMIZE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMiscellaneousConstraint() throws Exception {
        // Get the miscellaneousConstraint
        restMiscellaneousConstraintMockMvc.perform(get("/api/miscellaneous-constraints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMiscellaneousConstraint() throws Exception {
        // Initialize the database
        miscellaneousConstraintRepository.saveAndFlush(miscellaneousConstraint);

        int databaseSizeBeforeUpdate = miscellaneousConstraintRepository.findAll().size();

        // Update the miscellaneousConstraint
        MiscellaneousConstraint updatedMiscellaneousConstraint = miscellaneousConstraintRepository.findById(miscellaneousConstraint.getId()).get();
        // Disconnect from session so that the updates on updatedMiscellaneousConstraint are not directly saved in db
        em.detach(updatedMiscellaneousConstraint);
        updatedMiscellaneousConstraint
            .name(UPDATED_NAME)
            .maximize(UPDATED_MAXIMIZE)
            .minimize(UPDATED_MINIMIZE);

        restMiscellaneousConstraintMockMvc.perform(put("/api/miscellaneous-constraints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMiscellaneousConstraint)))
            .andExpect(status().isOk());

        // Validate the MiscellaneousConstraint in the database
        List<MiscellaneousConstraint> miscellaneousConstraintList = miscellaneousConstraintRepository.findAll();
        assertThat(miscellaneousConstraintList).hasSize(databaseSizeBeforeUpdate);
        MiscellaneousConstraint testMiscellaneousConstraint = miscellaneousConstraintList.get(miscellaneousConstraintList.size() - 1);
        assertThat(testMiscellaneousConstraint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMiscellaneousConstraint.isMaximize()).isEqualTo(UPDATED_MAXIMIZE);
        assertThat(testMiscellaneousConstraint.isMinimize()).isEqualTo(UPDATED_MINIMIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingMiscellaneousConstraint() throws Exception {
        int databaseSizeBeforeUpdate = miscellaneousConstraintRepository.findAll().size();

        // Create the MiscellaneousConstraint

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMiscellaneousConstraintMockMvc.perform(put("/api/miscellaneous-constraints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(miscellaneousConstraint)))
            .andExpect(status().isBadRequest());

        // Validate the MiscellaneousConstraint in the database
        List<MiscellaneousConstraint> miscellaneousConstraintList = miscellaneousConstraintRepository.findAll();
        assertThat(miscellaneousConstraintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMiscellaneousConstraint() throws Exception {
        // Initialize the database
        miscellaneousConstraintRepository.saveAndFlush(miscellaneousConstraint);

        int databaseSizeBeforeDelete = miscellaneousConstraintRepository.findAll().size();

        // Delete the miscellaneousConstraint
        restMiscellaneousConstraintMockMvc.perform(delete("/api/miscellaneous-constraints/{id}", miscellaneousConstraint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MiscellaneousConstraint> miscellaneousConstraintList = miscellaneousConstraintRepository.findAll();
        assertThat(miscellaneousConstraintList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MiscellaneousConstraint.class);
        MiscellaneousConstraint miscellaneousConstraint1 = new MiscellaneousConstraint();
        miscellaneousConstraint1.setId(1L);
        MiscellaneousConstraint miscellaneousConstraint2 = new MiscellaneousConstraint();
        miscellaneousConstraint2.setId(miscellaneousConstraint1.getId());
        assertThat(miscellaneousConstraint1).isEqualTo(miscellaneousConstraint2);
        miscellaneousConstraint2.setId(2L);
        assertThat(miscellaneousConstraint1).isNotEqualTo(miscellaneousConstraint2);
        miscellaneousConstraint1.setId(null);
        assertThat(miscellaneousConstraint1).isNotEqualTo(miscellaneousConstraint2);
    }
}
