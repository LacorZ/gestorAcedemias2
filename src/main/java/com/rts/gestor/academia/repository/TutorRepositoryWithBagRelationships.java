package com.rts.gestor.academia.repository;

import com.rts.gestor.academia.domain.Tutor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TutorRepositoryWithBagRelationships {
    Optional<Tutor> fetchBagRelationships(Optional<Tutor> tutor);

    List<Tutor> fetchBagRelationships(List<Tutor> tutors);

    Page<Tutor> fetchBagRelationships(Page<Tutor> tutors);
}
