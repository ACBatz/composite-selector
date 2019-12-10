package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.Verb;
import com.batz.compositeselection.repository.VerbRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.Verb}.
 */
@RestController
@RequestMapping("/api")
public class VerbResource {

    private final Logger log = LoggerFactory.getLogger(VerbResource.class);

    private static final String ENTITY_NAME = "verb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VerbRepository verbRepository;

    public VerbResource(VerbRepository verbRepository) {
        this.verbRepository = verbRepository;
    }

    /**
     * {@code POST  /verbs} : Create a new verb.
     *
     * @param verb the verb to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new verb, or with status {@code 400 (Bad Request)} if the verb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/verbs")
    public ResponseEntity<Verb> createVerb(@Valid @RequestBody Verb verb) throws URISyntaxException {
        log.debug("REST request to save Verb : {}", verb);
        if (verb.getId() != null) {
            throw new BadRequestAlertException("A new verb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Verb result = verbRepository.save(verb);
        return ResponseEntity.created(new URI("/api/verbs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /verbs} : Updates an existing verb.
     *
     * @param verb the verb to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated verb,
     * or with status {@code 400 (Bad Request)} if the verb is not valid,
     * or with status {@code 500 (Internal Server Error)} if the verb couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/verbs")
    public ResponseEntity<Verb> updateVerb(@Valid @RequestBody Verb verb) throws URISyntaxException {
        log.debug("REST request to update Verb : {}", verb);
        if (verb.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Verb result = verbRepository.save(verb);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, verb.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /verbs} : get all the verbs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of verbs in body.
     */
    @GetMapping("/verbs")
    public List<Verb> getAllVerbs() {
        log.debug("REST request to get all Verbs");
        return verbRepository.findAll();
    }

    /**
     * {@code GET  /verbs/:id} : get the "id" verb.
     *
     * @param id the id of the verb to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the verb, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/verbs/{id}")
    public ResponseEntity<Verb> getVerb(@PathVariable Long id) {
        log.debug("REST request to get Verb : {}", id);
        Optional<Verb> verb = verbRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(verb);
    }

    /**
     * {@code DELETE  /verbs/:id} : delete the "id" verb.
     *
     * @param id the id of the verb to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/verbs/{id}")
    public ResponseEntity<Void> deleteVerb(@PathVariable Long id) {
        log.debug("REST request to delete Verb : {}", id);
        verbRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
