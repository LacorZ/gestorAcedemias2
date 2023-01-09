package com.rts.gestor.academia.repository;

import com.rts.gestor.academia.domain.Factura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Factura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long>, JpaSpecificationExecutor<Factura> {}
