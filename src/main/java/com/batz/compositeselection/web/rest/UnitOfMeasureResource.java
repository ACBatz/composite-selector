package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.UnitOfMeasure;
import com.batz.compositeselection.repository.UnitOfMeasureRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.UnitOfMeasure}.
 */
@RestController
@RequestMapping("/api")
public class UnitOfMeasureResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureResource.class);

    private static final String ENTITY_NAME = "unitOfMeasure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureResource(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    /**
     * {@code POST  /unit-of-measures} : Create a new unitOfMeasure.
     *
     * @param unitOfMeasure the unitOfMeasure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitOfMeasure, or with status {@code 400 (Bad Request)} if the unitOfMeasure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-of-measures")
    public ResponseEntity<UnitOfMeasure> createUnitOfMeasure(@Valid @RequestBody UnitOfMeasure unitOfMeasure) throws URISyntaxException {
        log.debug("REST request to save UnitOfMeasure : {}", unitOfMeasure);
        if (unitOfMeasure.getId() != null) {
            throw new BadRequestAlertException("A new unitOfMeasure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitOfMeasure result = unitOfMeasureRepository.save(unitOfMeasure);
        return ResponseEntity.created(new URI("/api/unit-of-measures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-of-measures} : Updates an existing unitOfMeasure.
     *
     * @param unitOfMeasure the unitOfMeasure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasure,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-of-measures")
    public ResponseEntity<UnitOfMeasure> updateUnitOfMeasure(@Valid @RequestBody UnitOfMeasure unitOfMeasure) throws URISyntaxException {
        log.debug("REST request to update UnitOfMeasure : {}", unitOfMeasure);
        if (unitOfMeasure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UnitOfMeasure result = unitOfMeasureRepository.save(unitOfMeasure);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unitOfMeasure.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /unit-of-measures} : get all the unitOfMeasures.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitOfMeasures in body.
     */
    @GetMapping("/unit-of-measures")
    public List<UnitOfMeasure> getAllUnitOfMeasures() {
        log.debug("REST request to get all UnitOfMeasures");
        return unitOfMeasureRepository.findAll();
    }

    /**
     * {@code GET  /unit-of-measures/:id} : get the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitOfMeasure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-of-measures/{id}")
    public ResponseEntity<UnitOfMeasure> getUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to get UnitOfMeasure : {}", id);
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(unitOfMeasure);
    }

    /**
     * {@code DELETE  /unit-of-measures/:id} : delete the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-of-measures/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to delete UnitOfMeasure : {}", id);
        unitOfMeasureRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
