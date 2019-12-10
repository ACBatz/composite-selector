package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.EnvironmentalEffect;
import com.batz.compositeselection.repository.EnvironmentalEffectRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.EnvironmentalEffect}.
 */
@RestController
@RequestMapping("/api")
public class EnvironmentalEffectResource {

    private final Logger log = LoggerFactory.getLogger(EnvironmentalEffectResource.class);

    private static final String ENTITY_NAME = "environmentalEffect";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnvironmentalEffectRepository environmentalEffectRepository;

    public EnvironmentalEffectResource(EnvironmentalEffectRepository environmentalEffectRepository) {
        this.environmentalEffectRepository = environmentalEffectRepository;
    }

    /**
     * {@code POST  /environmental-effects} : Create a new environmentalEffect.
     *
     * @param environmentalEffect the environmentalEffect to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new environmentalEffect, or with status {@code 400 (Bad Request)} if the environmentalEffect has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/environmental-effects")
    public ResponseEntity<EnvironmentalEffect> createEnvironmentalEffect(@Valid @RequestBody EnvironmentalEffect environmentalEffect) throws URISyntaxException {
        log.debug("REST request to save EnvironmentalEffect : {}", environmentalEffect);
        if (environmentalEffect.getId() != null) {
            throw new BadRequestAlertException("A new environmentalEffect cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnvironmentalEffect result = environmentalEffectRepository.save(environmentalEffect);
        return ResponseEntity.created(new URI("/api/environmental-effects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /environmental-effects} : Updates an existing environmentalEffect.
     *
     * @param environmentalEffect the environmentalEffect to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated environmentalEffect,
     * or with status {@code 400 (Bad Request)} if the environmentalEffect is not valid,
     * or with status {@code 500 (Internal Server Error)} if the environmentalEffect couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/environmental-effects")
    public ResponseEntity<EnvironmentalEffect> updateEnvironmentalEffect(@Valid @RequestBody EnvironmentalEffect environmentalEffect) throws URISyntaxException {
        log.debug("REST request to update EnvironmentalEffect : {}", environmentalEffect);
        if (environmentalEffect.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EnvironmentalEffect result = environmentalEffectRepository.save(environmentalEffect);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, environmentalEffect.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /environmental-effects} : get all the environmentalEffects.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of environmentalEffects in body.
     */
    @GetMapping("/environmental-effects")
    public List<EnvironmentalEffect> getAllEnvironmentalEffects(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all EnvironmentalEffects");
        return environmentalEffectRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /environmental-effects/:id} : get the "id" environmentalEffect.
     *
     * @param id the id of the environmentalEffect to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the environmentalEffect, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/environmental-effects/{id}")
    public ResponseEntity<EnvironmentalEffect> getEnvironmentalEffect(@PathVariable Long id) {
        log.debug("REST request to get EnvironmentalEffect : {}", id);
        Optional<EnvironmentalEffect> environmentalEffect = environmentalEffectRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(environmentalEffect);
    }

    /**
     * {@code DELETE  /environmental-effects/:id} : delete the "id" environmentalEffect.
     *
     * @param id the id of the environmentalEffect to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/environmental-effects/{id}")
    public ResponseEntity<Void> deleteEnvironmentalEffect(@PathVariable Long id) {
        log.debug("REST request to delete EnvironmentalEffect : {}", id);
        environmentalEffectRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
