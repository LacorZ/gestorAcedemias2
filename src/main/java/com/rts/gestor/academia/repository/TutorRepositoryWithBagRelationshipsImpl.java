package com.rts.gestor.academia.repository;

import com.rts.gestor.academia.domain.Tutor;
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
public class TutorRepositoryWithBagRelationshipsImpl implements TutorRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tutor> fetchBagRelationships(Optional<Tutor> tutor) {
        return tutor.map(this::fetchCourses);
    }

    @Override
    public Page<Tutor> fetchBagRelationships(Page<Tutor> tutors) {
        return new PageImpl<>(fetchBagRelationships(tutors.getContent()), tutors.getPageable(), tutors.getTotalElements());
    }

    @Override
    public List<Tutor> fetchBagRelationships(List<Tutor> tutors) {
        return Optional.of(tutors).map(this::fetchCourses).orElse(Collections.emptyList());
    }

    Tutor fetchCourses(Tutor result) {
        return entityManager
            .createQuery("select tutor from Tutor tutor left join fetch tutor.courses where tutor is :tutor", Tutor.class)
            .setParameter("tutor", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Tutor> fetchCourses(List<Tutor> tutors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, tutors.size()).forEach(index -> order.put(tutors.get(index).getId(), index));
        List<Tutor> result = entityManager
            .createQuery("select distinct tutor from Tutor tutor left join fetch tutor.courses where tutor in :tutors", Tutor.class)
            .setParameter("tutors", tutors)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
