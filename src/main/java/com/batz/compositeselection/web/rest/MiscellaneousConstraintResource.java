package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.MiscellaneousConstraint;
import com.batz.compositeselection.repository.MiscellaneousConstraintRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.MiscellaneousConstraint}.
 */
@RestController
@RequestMapping("/api")
public class MiscellaneousConstraintResource {

    private final Logger log = LoggerFactory.getLogger(MiscellaneousConstraintResource.class);

    private static final String ENTITY_NAME = "miscellaneousConstraint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MiscellaneousConstraintRepository miscellaneousConstraintRepository;

    public MiscellaneousConstraintResource(MiscellaneousConstraintRepository miscellaneousConstraintRepository) {
        this.miscellaneousConstraintRepository = miscellaneousConstraintRepository;
    }

    /**
     * {@code POST  /miscellaneous-constraints} : Create a new miscellaneousConstraint.
     *
     * @param miscellaneousConstraint the miscellaneousConstraint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new miscellaneousConstraint, or with status {@code 400 (Bad Request)} if the miscellaneousConstraint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/miscellaneous-constraints")
    public ResponseEntity<MiscellaneousConstraint> createMiscellaneousConstraint(@Valid @RequestBody MiscellaneousConstraint miscellaneousConstraint) throws URISyntaxException {
        log.debug("REST request to save MiscellaneousConstraint : {}", miscellaneousConstraint);
        if (miscellaneousConstraint.getId() != null) {
            throw new BadRequestAlertException("A new miscellaneousConstraint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MiscellaneousConstraint result = miscellaneousConstraintRepository.save(miscellaneousConstraint);
        return ResponseEntity.created(new URI("/api/miscellaneous-constraints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /miscellaneous-constraints} : Updates an existing miscellaneousConstraint.
     *
     * @param miscellaneousConstraint the miscellaneousConstraint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated miscellaneousConstraint,
     * or with status {@code 400 (Bad Request)} if the miscellaneousConstraint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the miscellaneousConstraint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/miscellaneous-constraints")
    public ResponseEntity<MiscellaneousConstraint> updateMiscellaneousConstraint(@Valid @RequestBody MiscellaneousConstraint miscellaneousConstraint) throws URISyntaxException {
        log.debug("REST request to update MiscellaneousConstraint : {}", miscellaneousConstraint);
        if (miscellaneousConstraint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MiscellaneousConstraint result = miscellaneousConstraintRepository.save(miscellaneousConstraint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, miscellaneousConstraint.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /miscellaneous-constraints} : get all the miscellaneousConstraints.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of miscellaneousConstraints in body.
     */
    @GetMapping("/miscellaneous-constraints")
    public List<MiscellaneousConstraint> getAllMiscellaneousConstraints(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all MiscellaneousConstraints");
        return miscellaneousConstraintRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /miscellaneous-constraints/:id} : get the "id" miscellaneousConstraint.
     *
     * @param id the id of the miscellaneousConstraint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the miscellaneousConstraint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/miscellaneous-constraints/{id}")
    public ResponseEntity<MiscellaneousConstraint> getMiscellaneousConstraint(@PathVariable Long id) {
        log.debug("REST request to get MiscellaneousConstraint : {}", id);
        Optional<MiscellaneousConstraint> miscellaneousConstraint = miscellaneousConstraintRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(miscellaneousConstraint);
    }

    /**
     * {@code DELETE  /miscellaneous-constraints/:id} : delete the "id" miscellaneousConstraint.
     *
     * @param id the id of the miscellaneousConstraint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/miscellaneous-constraints/{id}")
    public ResponseEntity<Void> deleteMiscellaneousConstraint(@PathVariable Long id) {
        log.debug("REST request to delete MiscellaneousConstraint : {}", id);
        miscellaneousConstraintRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
