package com.batz.compositeselection.web.rest;

import com.batz.compositeselection.domain.Composite;
import com.batz.compositeselection.repository.CompositeRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.batz.compositeselection.domain.Composite}.
 */
@RestController
@RequestMapping("/api")
public class CompositeResource {

    private final Logger log = LoggerFactory.getLogger(CompositeResource.class);

    private static final String ENTITY_NAME = "composite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompositeRepository compositeRepository;

    public CompositeResource(CompositeRepository compositeRepository) {
        this.compositeRepository = compositeRepository;
    }

    /**
     * {@code POST  /composites} : Create a new composite.
     *
     * @param composite the composite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new composite, or with status {@code 400 (Bad Request)} if the composite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/composites")
    public ResponseEntity<Composite> createComposite(@Valid @RequestBody Composite composite) throws URISyntaxException {
        log.debug("REST request to save Composite : {}", composite);
        if (composite.getId() != null) {
            throw new BadRequestAlertException("A new composite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Composite result = compositeRepository.save(composite);
        return ResponseEntity.created(new URI("/api/composites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /composites} : Updates an existing composite.
     *
     * @param composite the composite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated composite,
     * or with status {@code 400 (Bad Request)} if the composite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the composite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/composites")
    public ResponseEntity<Composite> updateComposite(@Valid @RequestBody Composite composite) throws URISyntaxException {
        log.debug("REST request to update Composite : {}", composite);
        if (composite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Composite result = compositeRepository.save(composite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, composite.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /composites} : get all the composites.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of composites in body.
     */
    @GetMapping("/composites")
    public List<Composite> getAllComposites(@RequestParam(required = false) String filter) {
        if ("result-is-null".equals(filter)) {
            log.debug("REST request to get all Composites where result is null");
            return StreamSupport
                .stream(compositeRepository.findAll().spliterator(), false)
                .filter(composite -> composite.getResult() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Composites");
        return compositeRepository.findAll();
    }

    /**
     * {@code GET  /composites/:id} : get the "id" composite.
     *
     * @param id the id of the composite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the composite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/composites/{id}")
    public ResponseEntity<Composite> getComposite(@PathVariable Long id) {
        log.debug("REST request to get Composite : {}", id);
        Optional<Composite> composite = compositeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(composite);
    }

    /**
     * {@code DELETE  /composites/:id} : delete the "id" composite.
     *
     * @param id the id of the composite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/composites/{id}")
    public ResponseEntity<Void> deleteComposite(@PathVariable Long id) {
        log.debug("REST request to delete Composite : {}", id);
        compositeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
