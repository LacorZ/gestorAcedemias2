package com.rts.gestor.academia.service.criteria;

import com.rts.gestor.academia.domain.enumeration.MetodoPago;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.rts.gestor.academia.domain.Pago} entity. This class is used
 * in {@link com.rts.gestor.academia.web.rest.PagoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pagos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MetodoPago
     */
    public static class MetodoPagoFilter extends Filter<MetodoPago> {

        public MetodoPagoFilter() {}

        public MetodoPagoFilter(MetodoPagoFilter filter) {
            super(filter);
        }

        @Override
        public MetodoPagoFilter copy() {
            return new MetodoPagoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter cantidad;

    private InstantFilter fechaPago;

    private MetodoPagoFilter metodoPago;

    private StringFilter observaciones;

    private LongFilter facturaId;

    private Boolean distinct;

    public PagoCriteria() {}

    public PagoCriteria(PagoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.fechaPago = other.fechaPago == null ? null : other.fechaPago.copy();
        this.metodoPago = other.metodoPago == null ? null : other.metodoPago.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.facturaId = other.facturaId == null ? null : other.facturaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PagoCriteria copy() {
        return new PagoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getCantidad() {
        return cantidad;
    }

    public BigDecimalFilter cantidad() {
        if (cantidad == null) {
            cantidad = new BigDecimalFilter();
        }
        return cantidad;
    }

    public void setCantidad(BigDecimalFilter cantidad) {
        this.cantidad = cantidad;
    }

    public InstantFilter getFechaPago() {
        return fechaPago;
    }

    public InstantFilter fechaPago() {
        if (fechaPago == null) {
            fechaPago = new InstantFilter();
        }
        return fechaPago;
    }

    public void setFechaPago(InstantFilter fechaPago) {
        this.fechaPago = fechaPago;
    }

    public MetodoPagoFilter getMetodoPago() {
        return metodoPago;
    }

    public MetodoPagoFilter metodoPago() {
        if (metodoPago == null) {
            metodoPago = new MetodoPagoFilter();
        }
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoFilter metodoPago) {
        this.metodoPago = metodoPago;
    }

    public StringFilter getObservaciones() {
        return observaciones;
    }

    public StringFilter observaciones() {
        if (observaciones == null) {
            observaciones = new StringFilter();
        }
        return observaciones;
    }

    public void setObservaciones(StringFilter observaciones) {
        this.observaciones = observaciones;
    }

    public LongFilter getFacturaId() {
        return facturaId;
    }

    public LongFilter facturaId() {
        if (facturaId == null) {
            facturaId = new LongFilter();
        }
        return facturaId;
    }

    public void setFacturaId(LongFilter facturaId) {
        this.facturaId = facturaId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PagoCriteria that = (PagoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(fechaPago, that.fechaPago) &&
            Objects.equals(metodoPago, that.metodoPago) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(facturaId, that.facturaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, fechaPago, metodoPago, observaciones, facturaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (fechaPago != null ? "fechaPago=" + fechaPago + ", " : "") +
            (metodoPago != null ? "metodoPago=" + metodoPago + ", " : "") +
            (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
            (facturaId != null ? "facturaId=" + facturaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
