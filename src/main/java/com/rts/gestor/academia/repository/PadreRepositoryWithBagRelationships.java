package com.rts.gestor.academia.repository;

import com.rts.gestor.academia.domain.Padre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PadreRepositoryWithBagRelationships {
    Optional<Padre> fetchBagRelationships(Optional<Padre> padre);

    List<Padre> fetchBagRelationships(List<Padre> padres);

    Page<Padre> fetchBagRelationships(Page<Padre> padres);
}
