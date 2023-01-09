package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.Tutor;
import com.rts.gestor.academia.repository.TutorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tutor}.
 */
@Service
@Transactional
public class TutorService {

    private final Logger log = LoggerFactory.getLogger(TutorService.class);

    private final TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    /**
     * Save a tutor.
     *
     * @param tutor the entity to save.
     * @return the persisted entity.
     */
    public Tutor save(Tutor tutor) {
        log.debug("Request to save Tutor : {}", tutor);
        return tutorRepository.save(tutor);
    }

    /**
     * Update a tutor.
     *
     * @param tutor the entity to save.
     * @return the persisted entity.
     */
    public Tutor update(Tutor tutor) {
        log.debug("Request to update Tutor : {}", tutor);
        return tutorRepository.save(tutor);
    }

    /**
     * Partially update a tutor.
     *
     * @param tutor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tutor> partialUpdate(Tutor tutor) {
        log.debug("Request to partially update Tutor : {}", tutor);

        return tutorRepository
            .findById(tutor.getId())
            .map(existingTutor -> {
                if (tutor.getNombre() != null) {
                    existingTutor.setNombre(tutor.getNombre());
                }
                if (tutor.getEmail() != null) {
                    existingTutor.setEmail(tutor.getEmail());
                }
                if (tutor.getTelefono() != null) {
                    existingTutor.setTelefono(tutor.getTelefono());
                }
                if (tutor.getObservaciones() != null) {
                    existingTutor.setObservaciones(tutor.getObservaciones());
                }

                return existingTutor;
            })
            .map(tutorRepository::save);
    }

    /**
     * Get all the tutors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tutor> findAll(Pageable pageable) {
        log.debug("Request to get all Tutors");
        return tutorRepository.findAll(pageable);
    }

    /**
     * Get all the tutors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Tutor> findAllWithEagerRelationships(Pageable pageable) {
        return tutorRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one tutor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tutor> findOne(Long id) {
        log.debug("Request to get Tutor : {}", id);
        return tutorRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the tutor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tutor : {}", id);
        tutorRepository.deleteById(id);
    }
}
