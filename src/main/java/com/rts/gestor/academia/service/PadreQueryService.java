package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.*; // for static metamodels
import com.rts.gestor.academia.domain.Padre;
import com.rts.gestor.academia.repository.PadreRepository;
import com.rts.gestor.academia.service.criteria.PadreCriteria;
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
 * Service for executing complex queries for {@link Padre} entities in the database.
 * The main input is a {@link PadreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Padre} or a {@link Page} of {@link Padre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PadreQueryService extends QueryService<Padre> {

    private final Logger log = LoggerFactory.getLogger(PadreQueryService.class);

    private final PadreRepository padreRepository;

    public PadreQueryService(PadreRepository padreRepository) {
        this.padreRepository = padreRepository;
    }

    /**
     * Return a {@link List} of {@link Padre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Padre> findByCriteria(PadreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Padre> specification = createSpecification(criteria);
        return padreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Padre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Padre> findByCriteria(PadreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Padre> specification = createSpecification(criteria);
        return padreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PadreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Padre> specification = createSpecification(criteria);
        return padreRepository.count(specification);
    }

    /**
     * Function to convert {@link PadreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Padre> createSpecification(PadreCriteria criteria) {
        Specification<Padre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Padre_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Padre_.nombre));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Padre_.email));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Padre_.telefono));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Padre_.observaciones));
            }
            if (criteria.getEstudiantesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEstudiantesId(),
                            root -> root.join(Padre_.estudiantes, JoinType.LEFT).get(Estudiante_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
