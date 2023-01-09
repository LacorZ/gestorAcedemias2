package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.*; // for static metamodels
import com.rts.gestor.academia.domain.Tutor;
import com.rts.gestor.academia.repository.TutorRepository;
import com.rts.gestor.academia.service.criteria.TutorCriteria;
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
 * Service for executing complex queries for {@link Tutor} entities in the database.
 * The main input is a {@link TutorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Tutor} or a {@link Page} of {@link Tutor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TutorQueryService extends QueryService<Tutor> {

    private final Logger log = LoggerFactory.getLogger(TutorQueryService.class);

    private final TutorRepository tutorRepository;

    public TutorQueryService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    /**
     * Return a {@link List} of {@link Tutor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Tutor> findByCriteria(TutorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tutor> specification = createSpecification(criteria);
        return tutorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Tutor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tutor> findByCriteria(TutorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tutor> specification = createSpecification(criteria);
        return tutorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TutorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tutor> specification = createSpecification(criteria);
        return tutorRepository.count(specification);
    }

    /**
     * Function to convert {@link TutorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tutor> createSpecification(TutorCriteria criteria) {
        Specification<Tutor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tutor_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Tutor_.nombre));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Tutor_.email));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Tutor_.telefono));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Tutor_.observaciones));
            }
            if (criteria.getCursosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCursosId(), root -> root.join(Tutor_.cursos, JoinType.LEFT).get(Curso_.id))
                    );
            }
        }
        return specification;
    }
}
