package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.Pago;
import com.rts.gestor.academia.repository.PagoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pago}.
 */
@Service
@Transactional
public class PagoService {

    private final Logger log = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    /**
     * Save a pago.
     *
     * @param pago the entity to save.
     * @return the persisted entity.
     */
    public Pago save(Pago pago) {
        log.debug("Request to save Pago : {}", pago);
        return pagoRepository.save(pago);
    }

    /**
     * Update a pago.
     *
     * @param pago the entity to save.
     * @return the persisted entity.
     */
    public Pago update(Pago pago) {
        log.debug("Request to update Pago : {}", pago);
        return pagoRepository.save(pago);
    }

    /**
     * Partially update a pago.
     *
     * @param pago the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pago> partialUpdate(Pago pago) {
        log.debug("Request to partially update Pago : {}", pago);

        return pagoRepository
            .findById(pago.getId())
            .map(existingPago -> {
                if (pago.getCantidad() != null) {
                    existingPago.setCantidad(pago.getCantidad());
                }
                if (pago.getFechaPago() != null) {
                    existingPago.setFechaPago(pago.getFechaPago());
                }
                if (pago.getMetodoPago() != null) {
                    existingPago.setMetodoPago(pago.getMetodoPago());
                }
                if (pago.getObservaciones() != null) {
                    existingPago.setObservaciones(pago.getObservaciones());
                }

                return existingPago;
            })
            .map(pagoRepository::save);
    }

    /**
     * Get all the pagos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Pago> findAll(Pageable pageable) {
        log.debug("Request to get all Pagos");
        return pagoRepository.findAll(pageable);
    }

    /**
     * Get one pago by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pago> findOne(Long id) {
        log.debug("Request to get Pago : {}", id);
        return pagoRepository.findById(id);
    }

    /**
     * Delete the pago by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pago : {}", id);
        pagoRepository.deleteById(id);
    }
}
