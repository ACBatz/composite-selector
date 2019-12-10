package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.LoadShear;
import com.batz.compositeselection.repository.LoadShearRepository;
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
 * REST controller for managing {@link com.batz.compositeselection.domain.LoadShear}.
 */
@RestController
@RequestMapping("/api")
public class LoadShearResource {

    private final Logger log = LoggerFactory.getLogger(LoadShearResource.class);

    private static final String ENTITY_NAME = "loadShear";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoadShearRepository loadShearRepository;

    public LoadShearResource(LoadShearRepository loadShearRepository) {
        this.loadShearRepository = loadShearRepository;
    }

    /**
     * {@code POST  /load-shears} : Create a new loadShear.
     *
     * @param loadShear the loadShear to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loadShear, or with status {@code 400 (Bad Request)} if the loadShear has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/load-shears")
    public ResponseEntity<LoadShear> createLoadShear(@Valid @RequestBody LoadShear loadShear) throws URISyntaxException {
        log.debug("REST request to save LoadShear : {}", loadShear);
        if (loadShear.getId() != null) {
            throw new BadRequestAlertException("A new loadShear cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoadShear result = loadShearRepository.save(loadShear);
        return ResponseEntity.created(new URI("/api/load-shears/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /load-shears} : Updates an existing loadShear.
     *
     * @param loadShear the loadShear to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loadShear,
     * or with status {@code 400 (Bad Request)} if the loadShear is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loadShear couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/load-shears")
    public ResponseEntity<LoadShear> updateLoadShear(@Valid @RequestBody LoadShear loadShear) throws URISyntaxException {
        log.debug("REST request to update LoadShear : {}", loadShear);
        if (loadShear.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LoadShear result = loadShearRepository.save(loadShear);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loadShear.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /load-shears} : get all the loadShears.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loadShears in body.
     */
    @GetMapping("/load-shears")
    public List<LoadShear> getAllLoadShears(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LoadShears");
        return loadShearRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /load-shears/:id} : get the "id" loadShear.
     *
     * @param id the id of the loadShear to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loadShear, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/load-shears/{id}")
    public ResponseEntity<LoadShear> getLoadShear(@PathVariable Long id) {
        log.debug("REST request to get LoadShear : {}", id);
        Optional<LoadShear> loadShear = loadShearRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(loadShear);
    }

    /**
     * {@code DELETE  /load-shears/:id} : delete the "id" loadShear.
     *
     * @param id the id of the loadShear to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/load-shears/{id}")
    public ResponseEntity<Void> deleteLoadShear(@PathVariable Long id) {
        log.debug("REST request to delete LoadShear : {}", id);
        loadShearRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
