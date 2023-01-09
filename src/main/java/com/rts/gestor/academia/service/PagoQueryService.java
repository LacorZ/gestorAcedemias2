package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.*; // for static metamodels
import com.rts.gestor.academia.domain.Pago;
import com.rts.gestor.academia.repository.PagoRepository;
import com.rts.gestor.academia.service.criteria.PagoCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pago} entities in the database.
 * The main input is a {@link PagoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Pago} or a {@link Page} of {@link Pago} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PagoQueryService extends QueryService<Pago> {

    private final Logger log = LoggerFactory.getLogger(PagoQueryService.class);

    private final PagoRepository pagoRepository;

    public PagoQueryService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    /**
     * Return a {@link List} of {@link Pago} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Pago> findByCriteria(PagoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pago> specification = createSpecification(criteria);
        return pagoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Pago} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pago> findByCriteria(PagoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pago> specification = createSpecification(criteria);
        return pagoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PagoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pago> specification = createSpecification(criteria);
        return pagoRepository.count(specification);
    }

    /**
     * Function to convert {@link PagoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pago> createSpecification(PagoCriteria criteria) {
        Specification<Pago> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pago_.id));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), Pago_.cantidad));
            }
            if (criteria.getFechaPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaPago(), Pago_.fechaPago));
            }
            if (criteria.getMetodoPago() != null) {
                specification = specification.and(buildSpecification(criteria.getMetodoPago(), Pago_.metodoPago));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Pago_.observaciones));
            }
            if (criteria.getFacturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFacturaId(), root -> root.join(Pago_.facturas, JoinType.LEFT).get(Factura_.id))
                    );
            }
        }
        return specification;
    }
}
