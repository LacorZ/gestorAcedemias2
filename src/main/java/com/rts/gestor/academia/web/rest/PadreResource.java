package com.rts.gestor.academia.web.rest;

import com.rts.gestor.academia.domain.Padre;
import com.rts.gestor.academia.repository.PadreRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rts.gestor.academia.domain.Padre}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PadreResource {

    private final Logger log = LoggerFactory.getLogger(PadreResource.class);

    private static final String ENTITY_NAME = "padre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PadreRepository padreRepository;

    public PadreResource(PadreRepository padreRepository) {
        this.padreRepository = padreRepository;
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
        Padre result = padreRepository.save(padre);
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

        Padre result = padreRepository.save(padre);
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

        Optional<Padre> result = padreRepository
            .findById(padre.getId())
            .map(existingPadre -> {
                if (padre.getNombre() != null) {
                    existingPadre.setNombre(padre.getNombre());
                }
                if (padre.getEmail() != null) {
                    existingPadre.setEmail(padre.getEmail());
                }
                if (padre.getTelefono() != null) {
                    existingPadre.setTelefono(padre.getTelefono());
                }
                if (padre.getObservaciones() != null) {
                    existingPadre.setObservaciones(padre.getObservaciones());
                }

                return existingPadre;
            })
            .map(padreRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, padre.getId().toString())
        );
    }

    /**
     * {@code GET  /padres} : get all the padres.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of padres in body.
     */
    @GetMapping("/padres")
    public List<Padre> getAllPadres(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Padres");
        if (eagerload) {
            return padreRepository.findAllWithEagerRelationships();
        } else {
            return padreRepository.findAll();
        }
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
        Optional<Padre> padre = padreRepository.findOneWithEagerRelationships(id);
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
        padreRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
