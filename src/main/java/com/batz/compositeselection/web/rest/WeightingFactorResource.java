package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.WeightingFactor;
import com.batz.compositeselection.repository.WeightingFactorRepository;
import com.batz.compositeselection.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.batz.compositeselection.domain.WeightingFactor}.
 */
@RestController
@RequestMapping("/api")
public class WeightingFactorResource {

    private final Logger log = LoggerFactory.getLogger(WeightingFactorResource.class);

    private static final String ENTITY_NAME = "weightingFactor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeightingFactorRepository weightingFactorRepository;

    public WeightingFactorResource(WeightingFactorRepository weightingFactorRepository) {
        this.weightingFactorRepository = weightingFactorRepository;
    }

    /**
     * {@code POST  /weighting-factors} : Create a new weightingFactor.
     *
     * @param weightingFactor the weightingFactor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weightingFactor, or with status {@code 400 (Bad Request)} if the weightingFactor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weighting-factors")
    public ResponseEntity<WeightingFactor> createWeightingFactor(@Valid @RequestBody WeightingFactor weightingFactor) throws URISyntaxException {
        log.debug("REST request to save WeightingFactor : {}", weightingFactor);
        if (weightingFactor.getId() != null) {
            throw new BadRequestAlertException("A new weightingFactor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeightingFactor result = weightingFactorRepository.save(weightingFactor);
        return ResponseEntity.created(new URI("/api/weighting-factors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weighting-factors} : Updates an existing weightingFactor.
     *
     * @param weightingFactor the weightingFactor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weightingFactor,
     * or with status {@code 400 (Bad Request)} if the weightingFactor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weightingFactor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weighting-factors")
    public ResponseEntity<WeightingFactor> updateWeightingFactor(@Valid @RequestBody WeightingFactor weightingFactor) throws URISyntaxException {
        log.debug("REST request to update WeightingFactor : {}", weightingFactor);
        if (weightingFactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WeightingFactor result = weightingFactorRepository.save(weightingFactor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weightingFactor.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /weighting-factors} : get all the weightingFactors.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weightingFactors in body.
     */
    @GetMapping("/weighting-factors")
    public List<WeightingFactor> getAllWeightingFactors() {
        log.debug("REST request to get all WeightingFactors");
        return weightingFactorRepository.findAll();
    }

    /**
     * {@code GET  /weighting-factors/:id} : get the "id" weightingFactor.
     *
     * @param id the id of the weightingFactor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weightingFactor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weighting-factors/{id}")
    public ResponseEntity<WeightingFactor> getWeightingFactor(@PathVariable Long id) {
        log.debug("REST request to get WeightingFactor : {}", id);
        Optional<WeightingFactor> weightingFactor = weightingFactorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(weightingFactor);
    }

    /**
     * {@code DELETE  /weighting-factors/:id} : delete the "id" weightingFactor.
     *
     * @param id the id of the weightingFactor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weighting-factors/{id}")
    public ResponseEntity<Void> deleteWeightingFactor(@PathVariable Long id) {
        log.debug("REST request to delete WeightingFactor : {}", id);
        weightingFactorRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
