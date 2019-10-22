package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.CompositeSelectorApp;
import com.batz.compositeselection.domain.Limit;
import com.batz.compositeselection.repository.LimitRepository;
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
 * Integration tests for the {@link LimitResource} REST controller.
 */
@SpringBootTest(classes = CompositeSelectorApp.class)
public class LimitResourceIT {

    private static final Boolean DEFAULT_MAXIMUM = false;
    private static final Boolean UPDATED_MAXIMUM = true;

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;
    private static final Double SMALLER_VALUE = 1D - 1D;

    @Autowired
    private LimitRepository limitRepository;

    @Mock
    private LimitRepository limitRepositoryMock;

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

    private MockMvc restLimitMockMvc;

    private Limit limit;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LimitResource limitResource = new LimitResource(limitRepository);
        this.restLimitMockMvc = MockMvcBuilders.standaloneSetup(limitResource)
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
    public static Limit createEntity(EntityManager em) {
        Limit limit = new Limit()
            .maximum(DEFAULT_MAXIMUM)
            .value(DEFAULT_VALUE);
        return limit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Limit createUpdatedEntity(EntityManager em) {
        Limit limit = new Limit()
            .maximum(UPDATED_MAXIMUM)
            .value(UPDATED_VALUE);
        return limit;
    }

    @BeforeEach
    public void initTest() {
        limit = createEntity(em);
    }

    @Test
    @Transactional
    public void createLimit() throws Exception {
        int databaseSizeBeforeCreate = limitRepository.findAll().size();

        // Create the Limit
        restLimitMockMvc.perform(post("/api/limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(limit)))
            .andExpect(status().isCreated());

        // Validate the Limit in the database
        List<Limit> limitList = limitRepository.findAll();
        assertThat(limitList).hasSize(databaseSizeBeforeCreate + 1);
        Limit testLimit = limitList.get(limitList.size() - 1);
        assertThat(testLimit.isMaximum()).isEqualTo(DEFAULT_MAXIMUM);
        assertThat(testLimit.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createLimitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = limitRepository.findAll().size();

        // Create the Limit with an existing ID
        limit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLimitMockMvc.perform(post("/api/limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(limit)))
            .andExpect(status().isBadRequest());

        // Validate the Limit in the database
        List<Limit> limitList = limitRepository.findAll();
        assertThat(limitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLimits() throws Exception {
        // Initialize the database
        limitRepository.saveAndFlush(limit);

        // Get all the limitList
        restLimitMockMvc.perform(get("/api/limits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(limit.getId().intValue())))
            .andExpect(jsonPath("$.[*].maximum").value(hasItem(DEFAULT_MAXIMUM.booleanValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllLimitsWithEagerRelationshipsIsEnabled() throws Exception {
        LimitResource limitResource = new LimitResource(limitRepositoryMock);
        when(limitRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restLimitMockMvc = MockMvcBuilders.standaloneSetup(limitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLimitMockMvc.perform(get("/api/limits?eagerload=true"))
        .andExpect(status().isOk());

        verify(limitRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllLimitsWithEagerRelationshipsIsNotEnabled() throws Exception {
        LimitResource limitResource = new LimitResource(limitRepositoryMock);
            when(limitRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restLimitMockMvc = MockMvcBuilders.standaloneSetup(limitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLimitMockMvc.perform(get("/api/limits?eagerload=true"))
        .andExpect(status().isOk());

            verify(limitRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getLimit() throws Exception {
        // Initialize the database
        limitRepository.saveAndFlush(limit);

        // Get the limit
        restLimitMockMvc.perform(get("/api/limits/{id}", limit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(limit.getId().intValue()))
            .andExpect(jsonPath("$.maximum").value(DEFAULT_MAXIMUM.booleanValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLimit() throws Exception {
        // Get the limit
        restLimitMockMvc.perform(get("/api/limits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLimit() throws Exception {
        // Initialize the database
        limitRepository.saveAndFlush(limit);

        int databaseSizeBeforeUpdate = limitRepository.findAll().size();

        // Update the limit
        Limit updatedLimit = limitRepository.findById(limit.getId()).get();
        // Disconnect from session so that the updates on updatedLimit are not directly saved in db
        em.detach(updatedLimit);
        updatedLimit
            .maximum(UPDATED_MAXIMUM)
            .value(UPDATED_VALUE);

        restLimitMockMvc.perform(put("/api/limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLimit)))
            .andExpect(status().isOk());

        // Validate the Limit in the database
        List<Limit> limitList = limitRepository.findAll();
        assertThat(limitList).hasSize(databaseSizeBeforeUpdate);
        Limit testLimit = limitList.get(limitList.size() - 1);
        assertThat(testLimit.isMaximum()).isEqualTo(UPDATED_MAXIMUM);
        assertThat(testLimit.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingLimit() throws Exception {
        int databaseSizeBeforeUpdate = limitRepository.findAll().size();

        // Create the Limit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLimitMockMvc.perform(put("/api/limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(limit)))
            .andExpect(status().isBadRequest());

        // Validate the Limit in the database
        List<Limit> limitList = limitRepository.findAll();
        assertThat(limitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLimit() throws Exception {
        // Initialize the database
        limitRepository.saveAndFlush(limit);

        int databaseSizeBeforeDelete = limitRepository.findAll().size();

        // Delete the limit
        restLimitMockMvc.perform(delete("/api/limits/{id}", limit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Limit> limitList = limitRepository.findAll();
        assertThat(limitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Limit.class);
        Limit limit1 = new Limit();
        limit1.setId(1L);
        Limit limit2 = new Limit();
        limit2.setId(limit1.getId());
        assertThat(limit1).isEqualTo(limit2);
        limit2.setId(2L);
        assertThat(limit1).isNotEqualTo(limit2);
        limit1.setId(null);
        assertThat(limit1).isNotEqualTo(limit2);
    }
}
