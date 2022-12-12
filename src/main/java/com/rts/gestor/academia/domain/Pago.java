package com.rts.gestor.academia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rts.gestor.academia.domain.enumeration.MetodoPago;
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
 * A Pago.
 */
@Entity
@Table(name = "pago")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cantidad", precision = 21, scale = 2, nullable = false)
    private BigDecimal cantidad;

    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    private Instant fechaPago;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "pago")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "estudiantes", "pagos", "pago" }, allowSetters = true)
    private Set<Factura> facturas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pago id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCantidad() {
        return this.cantidad;
    }

    public Pago cantidad(BigDecimal cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFechaPago() {
        return this.fechaPago;
    }

    public Pago fechaPago(Instant fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(Instant fechaPago) {
        this.fechaPago = fechaPago;
    }

    public MetodoPago getMetodoPago() {
        return this.metodoPago;
    }

    public Pago metodoPago(MetodoPago metodoPago) {
        this.setMetodoPago(metodoPago);
        return this;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Pago observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<Factura> getFacturas() {
        return this.facturas;
    }

    public void setFacturas(Set<Factura> facturas) {
        if (this.facturas != null) {
            this.facturas.forEach(i -> i.setPago(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setPago(this));
        }
        this.facturas = facturas;
    }

    public Pago facturas(Set<Factura> facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public Pago addFactura(Factura factura) {
        this.facturas.add(factura);
        factura.setPago(this);
        return this;
    }

    public Pago removeFactura(Factura factura) {
        this.facturas.remove(factura);
        factura.setPago(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pago)) {
            return false;
        }
        return id != null && id.equals(((Pago) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pago{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", fechaPago='" + getFechaPago() + "'" +
            ", metodoPago='" + getMetodoPago() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
