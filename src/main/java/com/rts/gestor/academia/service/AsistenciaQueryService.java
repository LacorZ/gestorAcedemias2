package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.*; // for static metamodels
import com.rts.gestor.academia.domain.Asistencia;
import com.rts.gestor.academia.repository.AsistenciaRepository;
import com.rts.gestor.academia.service.criteria.AsistenciaCriteria;
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
 * Service for executing complex queries for {@link Asistencia} entities in the database.
 * The main input is a {@link AsistenciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Asistencia} or a {@link Page} of {@link Asistencia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AsistenciaQueryService extends QueryService<Asistencia> {

    private final Logger log = LoggerFactory.getLogger(AsistenciaQueryService.class);

    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaQueryService(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    /**
     * Return a {@link List} of {@link Asistencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Asistencia> findByCriteria(AsistenciaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Asistencia> specification = createSpecification(criteria);
        return asistenciaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Asistencia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Asistencia> findByCriteria(AsistenciaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Asistencia> specification = createSpecification(criteria);
        return asistenciaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AsistenciaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Asistencia> specification = createSpecification(criteria);
        return asistenciaRepository.count(specification);
    }

    /**
     * Function to convert {@link AsistenciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Asistencia> createSpecification(AsistenciaCriteria criteria) {
        Specification<Asistencia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Asistencia_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Asistencia_.fecha));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Asistencia_.estado));
            }
            if (criteria.getHoraEntrada() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHoraEntrada(), Asistencia_.horaEntrada));
            }
            if (criteria.getHoraSalida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHoraSalida(), Asistencia_.horaSalida));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Asistencia_.observaciones));
            }
            if (criteria.getEstudianteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEstudianteId(),
                            root -> root.join(Asistencia_.estudiantes, JoinType.LEFT).get(Estudiante_.id)
                        )
                    );
            }
            if (criteria.getCursoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCursoId(), root -> root.join(Asistencia_.cursos, JoinType.LEFT).get(Curso_.id))
                    );
            }
        }
        return specification;
    }
}
