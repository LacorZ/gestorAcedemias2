package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.Asistencia;
import com.rts.gestor.academia.repository.AsistenciaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Asistencia}.
 */
@Service
@Transactional
public class AsistenciaService {

    private final Logger log = LoggerFactory.getLogger(AsistenciaService.class);

    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    /**
     * Save a asistencia.
     *
     * @param asistencia the entity to save.
     * @return the persisted entity.
     */
    public Asistencia save(Asistencia asistencia) {
        log.debug("Request to save Asistencia : {}", asistencia);
        return asistenciaRepository.save(asistencia);
    }

    /**
     * Update a asistencia.
     *
     * @param asistencia the entity to save.
     * @return the persisted entity.
     */
    public Asistencia update(Asistencia asistencia) {
        log.debug("Request to update Asistencia : {}", asistencia);
        return asistenciaRepository.save(asistencia);
    }

    /**
     * Partially update a asistencia.
     *
     * @param asistencia the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Asistencia> partialUpdate(Asistencia asistencia) {
        log.debug("Request to partially update Asistencia : {}", asistencia);

        return asistenciaRepository
            .findById(asistencia.getId())
            .map(existingAsistencia -> {
                if (asistencia.getFecha() != null) {
                    existingAsistencia.setFecha(asistencia.getFecha());
                }
                if (asistencia.getEstado() != null) {
                    existingAsistencia.setEstado(asistencia.getEstado());
                }
                if (asistencia.getHoraEntrada() != null) {
                    existingAsistencia.setHoraEntrada(asistencia.getHoraEntrada());
                }
                if (asistencia.getHoraSalida() != null) {
                    existingAsistencia.setHoraSalida(asistencia.getHoraSalida());
                }
                if (asistencia.getObservaciones() != null) {
                    existingAsistencia.setObservaciones(asistencia.getObservaciones());
                }

                return existingAsistencia;
            })
            .map(asistenciaRepository::save);
    }

    /**
     * Get all the asistencias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Asistencia> findAll(Pageable pageable) {
        log.debug("Request to get all Asistencias");
        return asistenciaRepository.findAll(pageable);
    }

    /**
     * Get one asistencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Asistencia> findOne(Long id) {
        log.debug("Request to get Asistencia : {}", id);
        return asistenciaRepository.findById(id);
    }

    /**
     * Delete the asistencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Asistencia : {}", id);
        asistenciaRepository.deleteById(id);
    }
}
