package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.CalculationResult;
import com.batz.compositeselection.repository.CalculationResultRepository;
import com.batz.compositeselection.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.batz.compositeselection.domain.CalculationResult}.
 */
@RestController
@RequestMapping("/api")
public class CalculationResultResource {

    private final Logger log = LoggerFactory.getLogger(CalculationResultResource.class);

    private static final String ENTITY_NAME = "calculationResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalculationResultRepository calculationResultRepository;

    public CalculationResultResource(CalculationResultRepository calculationResultRepository) {
        this.calculationResultRepository = calculationResultRepository;
    }

    /**
     * {@code POST  /calculation-results} : Create a new calculationResult.
     *
     * @param calculationResult the calculationResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calculationResult, or with status {@code 400 (Bad Request)} if the calculationResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calculation-results")
    public ResponseEntity<CalculationResult> createCalculationResult(@RequestBody CalculationResult calculationResult) throws URISyntaxException {
        log.debug("REST request to save CalculationResult : {}", calculationResult);
        if (calculationResult.getId() != null) {
            throw new BadRequestAlertException("A new calculationResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalculationResult result = calculationResultRepository.save(calculationResult);
        return ResponseEntity.created(new URI("/api/calculation-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calculation-results} : Updates an existing calculationResult.
     *
     * @param calculationResult the calculationResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calculationResult,
     * or with status {@code 400 (Bad Request)} if the calculationResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calculationResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calculation-results")
    public ResponseEntity<CalculationResult> updateCalculationResult(@RequestBody CalculationResult calculationResult) throws URISyntaxException {
        log.debug("REST request to update CalculationResult : {}", calculationResult);
        if (calculationResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CalculationResult result = calculationResultRepository.save(calculationResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, calculationResult.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /calculation-results} : get all the calculationResults.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calculationResults in body.
     */
    @GetMapping("/calculation-results")
    public List<CalculationResult> getAllCalculationResults() {
        log.debug("REST request to get all CalculationResults");
        return calculationResultRepository.findAll();
    }

    /**
     * {@code GET  /calculation-results/:id} : get the "id" calculationResult.
     *
     * @param id the id of the calculationResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calculationResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calculation-results/{id}")
    public ResponseEntity<CalculationResult> getCalculationResult(@PathVariable Long id) {
        log.debug("REST request to get CalculationResult : {}", id);
        Optional<CalculationResult> calculationResult = calculationResultRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(calculationResult);
    }

    /**
     * {@code DELETE  /calculation-results/:id} : delete the "id" calculationResult.
     *
     * @param id the id of the calculationResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calculation-results/{id}")
    public ResponseEntity<Void> deleteCalculationResult(@PathVariable Long id) {
        log.debug("REST request to delete CalculationResult : {}", id);
        calculationResultRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
