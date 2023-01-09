package com.rts.gestor.academia.repository;

import com.rts.gestor.academia.domain.Asistencia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Asistencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long>, JpaSpecificationExecutor<Asistencia> {}
