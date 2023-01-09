package com.rts.gestor.academia.web.rest;

import com.rts.gestor.academia.domain.Tutor;
import com.rts.gestor.academia.repository.TutorRepository;
import com.rts.gestor.academia.service.TutorQueryService;
import com.rts.gestor.academia.service.TutorService;
import com.rts.gestor.academia.service.criteria.TutorCriteria;
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
 * REST controller for managing {@link com.rts.gestor.academia.domain.Tutor}.
 */
@RestController
@RequestMapping("/api")
public class TutorResource {

    private final Logger log = LoggerFactory.getLogger(TutorResource.class);

    private static final String ENTITY_NAME = "tutor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TutorService tutorService;

    private final TutorRepository tutorRepository;

    private final TutorQueryService tutorQueryService;

    public TutorResource(TutorService tutorService, TutorRepository tutorRepository, TutorQueryService tutorQueryService) {
        this.tutorService = tutorService;
        this.tutorRepository = tutorRepository;
        this.tutorQueryService = tutorQueryService;
    }

    /**
     * {@code POST  /tutors} : Create a new tutor.
     *
     * @param tutor the tutor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tutor, or with status {@code 400 (Bad Request)} if the tutor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tutors")
    public ResponseEntity<Tutor> createTutor(@Valid @RequestBody Tutor tutor) throws URISyntaxException {
        log.debug("REST request to save Tutor : {}", tutor);
        if (tutor.getId() != null) {
            throw new BadRequestAlertException("A new tutor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tutor result = tutorService.save(tutor);
        return ResponseEntity
            .created(new URI("/api/tutors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tutors/:id} : Updates an existing tutor.
     *
     * @param id the id of the tutor to save.
     * @param tutor the tutor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tutor,
     * or with status {@code 400 (Bad Request)} if the tutor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tutor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tutors/{id}")
    public ResponseEntity<Tutor> updateTutor(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Tutor tutor)
        throws URISyntaxException {
        log.debug("REST request to update Tutor : {}, {}", id, tutor);
        if (tutor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tutor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tutorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tutor result = tutorService.update(tutor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tutor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tutors/:id} : Partial updates given fields of an existing tutor, field will ignore if it is null
     *
     * @param id the id of the tutor to save.
     * @param tutor the tutor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tutor,
     * or with status {@code 400 (Bad Request)} if the tutor is not valid,
     * or with status {@code 404 (Not Found)} if the tutor is not found,
     * or with status {@code 500 (Internal Server Error)} if the tutor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tutors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tutor> partialUpdateTutor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tutor tutor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tutor partially : {}, {}", id, tutor);
        if (tutor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tutor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tutorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tutor> result = tutorService.partialUpdate(tutor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tutor.getId().toString())
        );
    }

    /**
     * {@code GET  /tutors} : get all the tutors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tutors in body.
     */
    @GetMapping("/tutors")
    public ResponseEntity<List<Tutor>> getAllTutors(
        TutorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Tutors by criteria: {}", criteria);
        Page<Tutor> page = tutorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tutors/count} : count all the tutors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tutors/count")
    public ResponseEntity<Long> countTutors(TutorCriteria criteria) {
        log.debug("REST request to count Tutors by criteria: {}", criteria);
        return ResponseEntity.ok().body(tutorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tutors/:id} : get the "id" tutor.
     *
     * @param id the id of the tutor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tutor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tutors/{id}")
    public ResponseEntity<Tutor> getTutor(@PathVariable Long id) {
        log.debug("REST request to get Tutor : {}", id);
        Optional<Tutor> tutor = tutorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tutor);
    }

    /**
     * {@code DELETE  /tutors/:id} : delete the "id" tutor.
     *
     * @param id the id of the tutor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tutors/{id}")
    public ResponseEntity<Void> deleteTutor(@PathVariable Long id) {
        log.debug("REST request to delete Tutor : {}", id);
        tutorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
