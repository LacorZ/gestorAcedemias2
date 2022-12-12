package com.rts.gestor.academia.repository;

import com.rts.gestor.academia.domain.Estudiante;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EstudianteRepositoryWithBagRelationshipsImpl implements EstudianteRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Estudiante> fetchBagRelationships(Optional<Estudiante> estudiante) {
        return estudiante.map(this::fetchCursos);
    }

    @Override
    public Page<Estudiante> fetchBagRelationships(Page<Estudiante> estudiantes) {
        return new PageImpl<>(fetchBagRelationships(estudiantes.getContent()), estudiantes.getPageable(), estudiantes.getTotalElements());
    }

    @Override
    public List<Estudiante> fetchBagRelationships(List<Estudiante> estudiantes) {
        return Optional.of(estudiantes).map(this::fetchCursos).orElse(Collections.emptyList());
    }

    Estudiante fetchCursos(Estudiante result) {
        return entityManager
            .createQuery(
                "select estudiante from Estudiante estudiante left join fetch estudiante.cursos where estudiante is :estudiante",
                Estudiante.class
            )
            .setParameter("estudiante", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Estudiante> fetchCursos(List<Estudiante> estudiantes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, estudiantes.size()).forEach(index -> order.put(estudiantes.get(index).getId(), index));
        List<Estudiante> result = entityManager
            .createQuery(
                "select distinct estudiante from Estudiante estudiante left join fetch estudiante.cursos where estudiante in :estudiantes",
                Estudiante.class
            )
            .setParameter("estudiantes", estudiantes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
