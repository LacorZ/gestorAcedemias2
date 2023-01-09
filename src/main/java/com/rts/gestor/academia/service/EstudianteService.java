package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.Estudiante;
import com.rts.gestor.academia.repository.EstudianteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Estudiante}.
 */
@Service
@Transactional
public class EstudianteService {

    private final Logger log = LoggerFactory.getLogger(EstudianteService.class);

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * Save a estudiante.
     *
     * @param estudiante the entity to save.
     * @return the persisted entity.
     */
    public Estudiante save(Estudiante estudiante) {
        log.debug("Request to save Estudiante : {}", estudiante);
        return estudianteRepository.save(estudiante);
    }

    /**
     * Update a estudiante.
     *
     * @param estudiante the entity to save.
     * @return the persisted entity.
     */
    public Estudiante update(Estudiante estudiante) {
        log.debug("Request to update Estudiante : {}", estudiante);
        return estudianteRepository.save(estudiante);
    }

    /**
     * Partially update a estudiante.
     *
     * @param estudiante the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Estudiante> partialUpdate(Estudiante estudiante) {
        log.debug("Request to partially update Estudiante : {}", estudiante);

        return estudianteRepository
            .findById(estudiante.getId())
            .map(existingEstudiante -> {
                if (estudiante.getNombre() != null) {
                    existingEstudiante.setNombre(estudiante.getNombre());
                }
                if (estudiante.getEmail() != null) {
                    existingEstudiante.setEmail(estudiante.getEmail());
                }
                if (estudiante.getTelefono() != null) {
                    existingEstudiante.setTelefono(estudiante.getTelefono());
                }
                if (estudiante.getObservaciones() != null) {
                    existingEstudiante.setObservaciones(estudiante.getObservaciones());
                }

                return existingEstudiante;
            })
            .map(estudianteRepository::save);
    }

    /**
     * Get all the estudiantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Estudiante> findAll(Pageable pageable) {
        log.debug("Request to get all Estudiantes");
        return estudianteRepository.findAll(pageable);
    }

    /**
     * Get all the estudiantes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Estudiante> findAllWithEagerRelationships(Pageable pageable) {
        return estudianteRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one estudiante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Estudiante> findOne(Long id) {
        log.debug("Request to get Estudiante : {}", id);
        return estudianteRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the estudiante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Estudiante : {}", id);
        estudianteRepository.deleteById(id);
    }
}
