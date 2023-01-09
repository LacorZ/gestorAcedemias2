package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.Factura;
import com.rts.gestor.academia.repository.FacturaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Factura}.
 */
@Service
@Transactional
public class FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaService.class);

    private final FacturaRepository facturaRepository;

    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    /**
     * Save a factura.
     *
     * @param factura the entity to save.
     * @return the persisted entity.
     */
    public Factura save(Factura factura) {
        log.debug("Request to save Factura : {}", factura);
        return facturaRepository.save(factura);
    }

    /**
     * Update a factura.
     *
     * @param factura the entity to save.
     * @return the persisted entity.
     */
    public Factura update(Factura factura) {
        log.debug("Request to update Factura : {}", factura);
        return facturaRepository.save(factura);
    }

    /**
     * Partially update a factura.
     *
     * @param factura the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Factura> partialUpdate(Factura factura) {
        log.debug("Request to partially update Factura : {}", factura);

        return facturaRepository
            .findById(factura.getId())
            .map(existingFactura -> {
                if (factura.getFacturado() != null) {
                    existingFactura.setFacturado(factura.getFacturado());
                }
                if (factura.getFechaFactura() != null) {
                    existingFactura.setFechaFactura(factura.getFechaFactura());
                }
                if (factura.getObservaciones() != null) {
                    existingFactura.setObservaciones(factura.getObservaciones());
                }

                return existingFactura;
            })
            .map(facturaRepository::save);
    }

    /**
     * Get all the facturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Factura> findAll(Pageable pageable) {
        log.debug("Request to get all Facturas");
        return facturaRepository.findAll(pageable);
    }

    /**
     * Get one factura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Factura> findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id);
    }

    /**
     * Delete the factura by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }
}
