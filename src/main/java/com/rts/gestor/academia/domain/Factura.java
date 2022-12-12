package com.rts.gestor.academia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "facturado", precision = 21, scale = 2, nullable = false)
    private BigDecimal facturado;

    @NotNull
    @Column(name = "fecha_factura", nullable = false)
    private Instant fechaFactura;

    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "factura")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asistencias", "facturas", "cursos", "asistencia", "factura", "padres" }, allowSetters = true)
    private Set<Estudiante> estudiantes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "facturas" }, allowSetters = true)
    private Pago pagos;

    @ManyToOne
    @JsonIgnoreProperties(value = { "facturas" }, allowSetters = true)
    private Pago pago;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFacturado() {
        return this.facturado;
    }

    public Factura facturado(BigDecimal facturado) {
        this.setFacturado(facturado);
        return this;
    }

    public void setFacturado(BigDecimal facturado) {
        this.facturado = facturado;
    }

    public Instant getFechaFactura() {
        return this.fechaFactura;
    }

    public Factura fechaFactura(Instant fechaFactura) {
        this.setFechaFactura(fechaFactura);
        return this;
    }

    public void setFechaFactura(Instant fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Factura observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<Estudiante> getEstudiantes() {
        return this.estudiantes;
    }

    public void setEstudiantes(Set<Estudiante> estudiantes) {
        if (this.estudiantes != null) {
            this.estudiantes.forEach(i -> i.setFactura(null));
        }
        if (estudiantes != null) {
            estudiantes.forEach(i -> i.setFactura(this));
        }
        this.estudiantes = estudiantes;
    }

    public Factura estudiantes(Set<Estudiante> estudiantes) {
        this.setEstudiantes(estudiantes);
        return this;
    }

    public Factura addEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
        estudiante.setFactura(this);
        return this;
    }

    public Factura removeEstudiante(Estudiante estudiante) {
        this.estudiantes.remove(estudiante);
        estudiante.setFactura(null);
        return this;
    }

    public Pago getPagos() {
        return this.pagos;
    }

    public void setPagos(Pago pago) {
        this.pagos = pago;
    }

    public Factura pagos(Pago pago) {
        this.setPagos(pago);
        return this;
    }

    public Pago getPago() {
        return this.pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Factura pago(Pago pago) {
        this.setPago(pago);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", facturado=" + getFacturado() +
            ", fechaFactura='" + getFechaFactura() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
