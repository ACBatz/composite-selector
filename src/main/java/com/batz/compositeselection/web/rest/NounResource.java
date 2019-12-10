package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.Noun;
import com.batz.compositeselection.repository.NounRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.Noun}.
 */
@RestController
@RequestMapping("/api")
public class NounResource {

    private final Logger log = LoggerFactory.getLogger(NounResource.class);

    private static final String ENTITY_NAME = "noun";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NounRepository nounRepository;

    public NounResource(NounRepository nounRepository) {
        this.nounRepository = nounRepository;
    }

    /**
     * {@code POST  /nouns} : Create a new noun.
     *
     * @param noun the noun to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noun, or with status {@code 400 (Bad Request)} if the noun has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nouns")
    public ResponseEntity<Noun> createNoun(@Valid @RequestBody Noun noun) throws URISyntaxException {
        log.debug("REST request to save Noun : {}", noun);
        if (noun.getId() != null) {
            throw new BadRequestAlertException("A new noun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Noun result = nounRepository.save(noun);
        return ResponseEntity.created(new URI("/api/nouns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nouns} : Updates an existing noun.
     *
     * @param noun the noun to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noun,
     * or with status {@code 400 (Bad Request)} if the noun is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noun couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nouns")
    public ResponseEntity<Noun> updateNoun(@Valid @RequestBody Noun noun) throws URISyntaxException {
        log.debug("REST request to update Noun : {}", noun);
        if (noun.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Noun result = nounRepository.save(noun);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, noun.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /nouns} : get all the nouns.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nouns in body.
     */
    @GetMapping("/nouns")
    public List<Noun> getAllNouns() {
        log.debug("REST request to get all Nouns");
        return nounRepository.findAll();
    }

    /**
     * {@code GET  /nouns/:id} : get the "id" noun.
     *
     * @param id the id of the noun to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noun, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nouns/{id}")
    public ResponseEntity<Noun> getNoun(@PathVariable Long id) {
        log.debug("REST request to get Noun : {}", id);
        Optional<Noun> noun = nounRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(noun);
    }

    /**
     * {@code DELETE  /nouns/:id} : delete the "id" noun.
     *
     * @param id the id of the noun to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nouns/{id}")
    public ResponseEntity<Void> deleteNoun(@PathVariable Long id) {
        log.debug("REST request to delete Noun : {}", id);
        nounRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
