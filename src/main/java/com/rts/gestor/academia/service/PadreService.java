package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.Padre;
import com.rts.gestor.academia.repository.PadreRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Padre}.
 */
@Service
@Transactional
public class PadreService {

    private final Logger log = LoggerFactory.getLogger(PadreService.class);

    private final PadreRepository padreRepository;

    public PadreService(PadreRepository padreRepository) {
        this.padreRepository = padreRepository;
    }

    /**
     * Save a padre.
     *
     * @param padre the entity to save.
     * @return the persisted entity.
     */
    public Padre save(Padre padre) {
        log.debug("Request to save Padre : {}", padre);
        return padreRepository.save(padre);
    }

    /**
     * Update a padre.
     *
     * @param padre the entity to save.
     * @return the persisted entity.
     */
    public Padre update(Padre padre) {
        log.debug("Request to update Padre : {}", padre);
        return padreRepository.save(padre);
    }

    /**
     * Partially update a padre.
     *
     * @param padre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Padre> partialUpdate(Padre padre) {
        log.debug("Request to partially update Padre : {}", padre);

        return padreRepository
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
    }

    /**
     * Get all the padres.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Padre> findAll() {
        log.debug("Request to get all Padres");
        return padreRepository.findAll();
    }

    /**
     * Get all the padres with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Padre> findAllWithEagerRelationships(Pageable pageable) {
        return padreRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one padre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Padre> findOne(Long id) {
        log.debug("Request to get Padre : {}", id);
        return padreRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the padre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Padre : {}", id);
        padreRepository.deleteById(id);
    }
}
