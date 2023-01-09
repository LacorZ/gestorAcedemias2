package com.rts.gestor.academia.web.rest;

import com.rts.gestor.academia.domain.Padre;
import com.rts.gestor.academia.repository.PadreRepository;
import com.rts.gestor.academia.service.PadreQueryService;
import com.rts.gestor.academia.service.PadreService;
import com.rts.gestor.academia.service.criteria.PadreCriteria;
import com.rts.gestor.academia.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rts.gestor.academia.domain.Padre}.
 */
@RestController
@RequestMapping("/api")
public class PadreResource {

    private final Logger log = LoggerFactory.getLogger(PadreResource.class);

    private static final String ENTITY_NAME = "padre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PadreService padreService;

    private final PadreRepository padreRepository;

    private final PadreQueryService padreQueryService;

    public PadreResource(PadreService padreService, PadreRepository padreRepository, PadreQueryService padreQueryService) {
        this.padreService = padreService;
        this.padreRepository = padreRepository;
        this.padreQueryService = padreQueryService;
    }

    /**
     * {@code POST  /padres} : Create a new padre.
     *
     * @param padre the padre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new padre, or with status {@code 400 (Bad Request)} if the padre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/padres")
    public ResponseEntity<Padre> createPadre(@Valid @RequestBody Padre padre) throws URISyntaxException {
        log.debug("REST request to save Padre : {}", padre);
        if (padre.getId() != null) {
            throw new BadRequestAlertException("A new padre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Padre result = padreService.save(padre);
        return ResponseEntity
            .created(new URI("/api/padres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /padres/:id} : Updates an existing padre.
     *
     * @param id the id of the padre to save.
     * @param padre the padre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated padre,
     * or with status {@code 400 (Bad Request)} if the padre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the padre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/padres/{id}")
    public ResponseEntity<Padre> updatePadre(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Padre padre)
        throws URISyntaxException {
        log.debug("REST request to update Padre : {}, {}", id, padre);
        if (padre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, padre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!padreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Padre result = padreService.update(padre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, padre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /padres/:id} : Partial updates given fields of an existing padre, field will ignore if it is null
     *
     * @param id the id of the padre to save.
     * @param padre the padre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated padre,
     * or with status {@code 400 (Bad Request)} if the padre is not valid,
     * or with status {@code 404 (Not Found)} if the padre is not found,
     * or with status {@code 500 (Internal Server Error)} if the padre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/padres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Padre> partialUpdatePadre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Padre padre
    ) throws URISyntaxException {
        log.debug("REST request to partial update Padre partially : {}, {}", id, padre);
        if (padre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, padre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!padreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Padre> result = padreService.partialUpdate(padre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, padre.getId().toString())
        );
    }

    /**
     * {@code GET  /padres} : get all the padres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of padres in body.
     */
    @GetMapping("/padres")
    public ResponseEntity<List<Padre>> getAllPadres(PadreCriteria criteria) {
        log.debug("REST request to get Padres by criteria: {}", criteria);
        List<Padre> entityList = padreQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /padres/count} : count all the padres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/padres/count")
    public ResponseEntity<Long> countPadres(PadreCriteria criteria) {
        log.debug("REST request to count Padres by criteria: {}", criteria);
        return ResponseEntity.ok().body(padreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /padres/:id} : get the "id" padre.
     *
     * @param id the id of the padre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the padre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/padres/{id}")
    public ResponseEntity<Padre> getPadre(@PathVariable Long id) {
        log.debug("REST request to get Padre : {}", id);
        Optional<Padre> padre = padreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(padre);
    }

    /**
     * {@code DELETE  /padres/:id} : delete the "id" padre.
     *
     * @param id the id of the padre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/padres/{id}")
    public ResponseEntity<Void> deletePadre(@PathVariable Long id) {
        log.debug("REST request to delete Padre : {}", id);
        padreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
