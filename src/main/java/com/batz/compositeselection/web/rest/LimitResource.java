package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.Limit;
import com.batz.compositeselection.repository.LimitRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.Limit}.
 */
@RestController
@RequestMapping("/api")
public class LimitResource {

    private final Logger log = LoggerFactory.getLogger(LimitResource.class);

    private static final String ENTITY_NAME = "limit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LimitRepository limitRepository;

    public LimitResource(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    /**
     * {@code POST  /limits} : Create a new limit.
     *
     * @param limit the limit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new limit, or with status {@code 400 (Bad Request)} if the limit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/limits")
    public ResponseEntity<Limit> createLimit(@RequestBody Limit limit) throws URISyntaxException {
        log.debug("REST request to save Limit : {}", limit);
        if (limit.getId() != null) {
            throw new BadRequestAlertException("A new limit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Limit result = limitRepository.save(limit);
        return ResponseEntity.created(new URI("/api/limits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /limits} : Updates an existing limit.
     *
     * @param limit the limit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated limit,
     * or with status {@code 400 (Bad Request)} if the limit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the limit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/limits")
    public ResponseEntity<Limit> updateLimit(@RequestBody Limit limit) throws URISyntaxException {
        log.debug("REST request to update Limit : {}", limit);
        if (limit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Limit result = limitRepository.save(limit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, limit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /limits} : get all the limits.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of limits in body.
     */
    @GetMapping("/limits")
    public List<Limit> getAllLimits(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Limits");
        return limitRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /limits/:id} : get the "id" limit.
     *
     * @param id the id of the limit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the limit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/limits/{id}")
    public ResponseEntity<Limit> getLimit(@PathVariable Long id) {
        log.debug("REST request to get Limit : {}", id);
        Optional<Limit> limit = limitRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(limit);
    }

    /**
     * {@code DELETE  /limits/:id} : delete the "id" limit.
     *
     * @param id the id of the limit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/limits/{id}")
    public ResponseEntity<Void> deleteLimit(@PathVariable Long id) {
        log.debug("REST request to delete Limit : {}", id);
        limitRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
