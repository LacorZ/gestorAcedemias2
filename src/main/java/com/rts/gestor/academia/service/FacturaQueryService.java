package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.*; // for static metamodels
import com.rts.gestor.academia.domain.Factura;
import com.rts.gestor.academia.repository.FacturaRepository;
import com.rts.gestor.academia.service.criteria.FacturaCriteria;
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
 * Service for executing complex queries for {@link Factura} entities in the database.
 * The main input is a {@link FacturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Factura} or a {@link Page} of {@link Factura} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacturaQueryService extends QueryService<Factura> {

    private final Logger log = LoggerFactory.getLogger(FacturaQueryService.class);

    private final FacturaRepository facturaRepository;

    public FacturaQueryService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    /**
     * Return a {@link List} of {@link Factura} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Factura> findByCriteria(FacturaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Factura} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Factura> findByCriteria(FacturaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacturaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.count(specification);
    }

    /**
     * Function to convert {@link FacturaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Factura> createSpecification(FacturaCriteria criteria) {
        Specification<Factura> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Factura_.id));
            }
            if (criteria.getFacturado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFacturado(), Factura_.facturado));
            }
            if (criteria.getFechaFactura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFactura(), Factura_.fechaFactura));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Factura_.observaciones));
            }
            if (criteria.getEstudianteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEstudianteId(),
                            root -> root.join(Factura_.estudiantes, JoinType.LEFT).get(Estudiante_.id)
                        )
                    );
            }
            if (criteria.getPagosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPagosId(), root -> root.join(Factura_.pagos, JoinType.LEFT).get(Pago_.id))
                    );
            }
            if (criteria.getPagoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPagoId(), root -> root.join(Factura_.pago, JoinType.LEFT).get(Pago_.id))
                    );
            }
        }
        return specification;
    }
}
