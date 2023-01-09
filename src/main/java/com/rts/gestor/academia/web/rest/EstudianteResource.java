package com.rts.gestor.academia.web.rest;

import com.rts.gestor.academia.domain.Estudiante;
import com.rts.gestor.academia.repository.EstudianteRepository;
import com.rts.gestor.academia.service.EstudianteQueryService;
import com.rts.gestor.academia.service.EstudianteService;
import com.rts.gestor.academia.service.criteria.EstudianteCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rts.gestor.academia.domain.Estudiante}.
 */
@RestController
@RequestMapping("/api")
public class EstudianteResource {

    private final Logger log = LoggerFactory.getLogger(EstudianteResource.class);

    private static final String ENTITY_NAME = "estudiante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstudianteService estudianteService;

    private final EstudianteRepository estudianteRepository;

    private final EstudianteQueryService estudianteQueryService;

    public EstudianteResource(
        EstudianteService estudianteService,
        EstudianteRepository estudianteRepository,
        EstudianteQueryService estudianteQueryService
    ) {
        this.estudianteService = estudianteService;
        this.estudianteRepository = estudianteRepository;
        this.estudianteQueryService = estudianteQueryService;
    }

    /**
     * {@code POST  /estudiantes} : Create a new estudiante.
     *
     * @param estudiante the estudiante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estudiante, or with status {@code 400 (Bad Request)} if the estudiante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/estudiantes")
    public ResponseEntity<Estudiante> createEstudiante(@Valid @RequestBody Estudiante estudiante) throws URISyntaxException {
        log.debug("REST request to save Estudiante : {}", estudiante);
        if (estudiante.getId() != null) {
            throw new BadRequestAlertException("A new estudiante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Estudiante result = estudianteService.save(estudiante);
        return ResponseEntity
            .created(new URI("/api/estudiantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /estudiantes/:id} : Updates an existing estudiante.
     *
     * @param id the id of the estudiante to save.
     * @param estudiante the estudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estudiante,
     * or with status {@code 400 (Bad Request)} if the estudiante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/estudiantes/{id}")
    public ResponseEntity<Estudiante> updateEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Estudiante estudiante
    ) throws URISyntaxException {
        log.debug("REST request to update Estudiante : {}, {}", id, estudiante);
        if (estudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Estudiante result = estudianteService.update(estudiante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, estudiante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /estudiantes/:id} : Partial updates given fields of an existing estudiante, field will ignore if it is null
     *
     * @param id the id of the estudiante to save.
     * @param estudiante the estudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estudiante,
     * or with status {@code 400 (Bad Request)} if the estudiante is not valid,
     * or with status {@code 404 (Not Found)} if the estudiante is not found,
     * or with status {@code 500 (Internal Server Error)} if the estudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/estudiantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Estudiante> partialUpdateEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Estudiante estudiante
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estudiante partially : {}, {}", id, estudiante);
        if (estudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Estudiante> result = estudianteService.partialUpdate(estudiante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, estudiante.getId().toString())
        );
    }

    /**
     * {@code GET  /estudiantes} : get all the estudiantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estudiantes in body.
     */
    @GetMapping("/estudiantes")
    public ResponseEntity<List<Estudiante>> getAllEstudiantes(
        EstudianteCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Estudiantes by criteria: {}", criteria);
        Page<Estudiante> page = estudianteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estudiantes/count} : count all the estudiantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/estudiantes/count")
    public ResponseEntity<Long> countEstudiantes(EstudianteCriteria criteria) {
        log.debug("REST request to count Estudiantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(estudianteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /estudiantes/:id} : get the "id" estudiante.
     *
     * @param id the id of the estudiante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estudiante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/estudiantes/{id}")
    public ResponseEntity<Estudiante> getEstudiante(@PathVariable Long id) {
        log.debug("REST request to get Estudiante : {}", id);
        Optional<Estudiante> estudiante = estudianteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estudiante);
    }

    /**
     * {@code DELETE  /estudiantes/:id} : delete the "id" estudiante.
     *
     * @param id the id of the estudiante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/estudiantes/{id}")
    public ResponseEntity<Void> deleteEstudiante(@PathVariable Long id) {
        log.debug("REST request to delete Estudiante : {}", id);
        estudianteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
