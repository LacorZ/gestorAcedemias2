package com.rts.gestor.academia.repository;

import com.rts.gestor.academia.domain.Padre;
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
public class PadreRepositoryWithBagRelationshipsImpl implements PadreRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Padre> fetchBagRelationships(Optional<Padre> padre) {
        return padre.map(this::fetchEstudiantes);
    }

    @Override
    public Page<Padre> fetchBagRelationships(Page<Padre> padres) {
        return new PageImpl<>(fetchBagRelationships(padres.getContent()), padres.getPageable(), padres.getTotalElements());
    }

    @Override
    public List<Padre> fetchBagRelationships(List<Padre> padres) {
        return Optional.of(padres).map(this::fetchEstudiantes).orElse(Collections.emptyList());
    }

    Padre fetchEstudiantes(Padre result) {
        return entityManager
            .createQuery("select padre from Padre padre left join fetch padre.estudiantes where padre is :padre", Padre.class)
            .setParameter("padre", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Padre> fetchEstudiantes(List<Padre> padres) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, padres.size()).forEach(index -> order.put(padres.get(index).getId(), index));
        List<Padre> result = entityManager
            .createQuery("select distinct padre from Padre padre left join fetch padre.estudiantes where padre in :padres", Padre.class)
            .setParameter("padres", padres)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
