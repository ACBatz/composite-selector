package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.LoadDirection;
import com.batz.compositeselection.repository.LoadDirectionRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.LoadDirection}.
 */
@RestController
@RequestMapping("/api")
public class LoadDirectionResource {

    private final Logger log = LoggerFactory.getLogger(LoadDirectionResource.class);

    private static final String ENTITY_NAME = "loadDirection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoadDirectionRepository loadDirectionRepository;

    public LoadDirectionResource(LoadDirectionRepository loadDirectionRepository) {
        this.loadDirectionRepository = loadDirectionRepository;
    }

    /**
     * {@code POST  /load-directions} : Create a new loadDirection.
     *
     * @param loadDirection the loadDirection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loadDirection, or with status {@code 400 (Bad Request)} if the loadDirection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/load-directions")
    public ResponseEntity<LoadDirection> createLoadDirection(@Valid @RequestBody LoadDirection loadDirection) throws URISyntaxException {
        log.debug("REST request to save LoadDirection : {}", loadDirection);
        if (loadDirection.getId() != null) {
            throw new BadRequestAlertException("A new loadDirection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoadDirection result = loadDirectionRepository.save(loadDirection);
        return ResponseEntity.created(new URI("/api/load-directions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /load-directions} : Updates an existing loadDirection.
     *
     * @param loadDirection the loadDirection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loadDirection,
     * or with status {@code 400 (Bad Request)} if the loadDirection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loadDirection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/load-directions")
    public ResponseEntity<LoadDirection> updateLoadDirection(@Valid @RequestBody LoadDirection loadDirection) throws URISyntaxException {
        log.debug("REST request to update LoadDirection : {}", loadDirection);
        if (loadDirection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LoadDirection result = loadDirectionRepository.save(loadDirection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loadDirection.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /load-directions} : get all the loadDirections.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loadDirections in body.
     */
    @GetMapping("/load-directions")
    public List<LoadDirection> getAllLoadDirections(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LoadDirections");
        return loadDirectionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /load-directions/:id} : get the "id" loadDirection.
     *
     * @param id the id of the loadDirection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loadDirection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/load-directions/{id}")
    public ResponseEntity<LoadDirection> getLoadDirection(@PathVariable Long id) {
        log.debug("REST request to get LoadDirection : {}", id);
        Optional<LoadDirection> loadDirection = loadDirectionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(loadDirection);
    }

    /**
     * {@code DELETE  /load-directions/:id} : delete the "id" loadDirection.
     *
     * @param id the id of the loadDirection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/load-directions/{id}")
    public ResponseEntity<Void> deleteLoadDirection(@PathVariable Long id) {
        log.debug("REST request to delete LoadDirection : {}", id);
        loadDirectionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
