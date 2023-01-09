package com.rts.gestor.academia.service;

import com.rts.gestor.academia.domain.*; // for static metamodels
import com.rts.gestor.academia.domain.Estudiante;
import com.rts.gestor.academia.repository.EstudianteRepository;
import com.rts.gestor.academia.service.criteria.EstudianteCriteria;
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
 * Service for executing complex queries for {@link Estudiante} entities in the database.
 * The main input is a {@link EstudianteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Estudiante} or a {@link Page} of {@link Estudiante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstudianteQueryService extends QueryService<Estudiante> {

    private final Logger log = LoggerFactory.getLogger(EstudianteQueryService.class);

    private final EstudianteRepository estudianteRepository;

    public EstudianteQueryService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * Return a {@link List} of {@link Estudiante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Estudiante> findByCriteria(EstudianteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Estudiante> specification = createSpecification(criteria);
        return estudianteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Estudiante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Estudiante> findByCriteria(EstudianteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Estudiante> specification = createSpecification(criteria);
        return estudianteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstudianteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Estudiante> specification = createSpecification(criteria);
        return estudianteRepository.count(specification);
    }

    /**
     * Function to convert {@link EstudianteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Estudiante> createSpecification(EstudianteCriteria criteria) {
        Specification<Estudiante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Estudiante_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Estudiante_.nombre));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Estudiante_.email));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Estudiante_.telefono));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Estudiante_.observaciones));
            }
            if (criteria.getAsistenciasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAsistenciasId(),
                            root -> root.join(Estudiante_.asistencias, JoinType.LEFT).get(Asistencia_.id)
                        )
                    );
            }
            if (criteria.getFacturasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacturasId(),
                            root -> root.join(Estudiante_.facturas, JoinType.LEFT).get(Factura_.id)
                        )
                    );
            }
            if (criteria.getCursosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCursosId(), root -> root.join(Estudiante_.cursos, JoinType.LEFT).get(Curso_.id))
                    );
            }
            if (criteria.getAsistenciaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAsistenciaId(),
                            root -> root.join(Estudiante_.asistencia, JoinType.LEFT).get(Asistencia_.id)
                        )
                    );
            }
            if (criteria.getFacturaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFacturaId(), root -> root.join(Estudiante_.factura, JoinType.LEFT).get(Factura_.id))
                    );
            }
            if (criteria.getPadresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPadresId(), root -> root.join(Estudiante_.padres, JoinType.LEFT).get(Padre_.id))
                    );
            }
        }
        return specification;
    }
}
