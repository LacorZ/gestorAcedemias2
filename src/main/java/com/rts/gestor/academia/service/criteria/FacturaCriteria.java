package com.rts.gestor.academia.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.rts.gestor.academia.domain.Factura} entity. This class is used
 * in {@link com.rts.gestor.academia.web.rest.FacturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter facturado;

    private InstantFilter fechaFactura;

    private StringFilter observaciones;

    private LongFilter estudianteId;

    private LongFilter pagosId;

    private LongFilter pagoId;

    private Boolean distinct;

    public FacturaCriteria() {}

    public FacturaCriteria(FacturaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.facturado = other.facturado == null ? null : other.facturado.copy();
        this.fechaFactura = other.fechaFactura == null ? null : other.fechaFactura.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.estudianteId = other.estudianteId == null ? null : other.estudianteId.copy();
        this.pagosId = other.pagosId == null ? null : other.pagosId.copy();
        this.pagoId = other.pagoId == null ? null : other.pagoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FacturaCriteria copy() {
        return new FacturaCriteria(this);
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

    public BigDecimalFilter getFacturado() {
        return facturado;
    }

    public BigDecimalFilter facturado() {
        if (facturado == null) {
            facturado = new BigDecimalFilter();
        }
        return facturado;
    }

    public void setFacturado(BigDecimalFilter facturado) {
        this.facturado = facturado;
    }

    public InstantFilter getFechaFactura() {
        return fechaFactura;
    }

    public InstantFilter fechaFactura() {
        if (fechaFactura == null) {
            fechaFactura = new InstantFilter();
        }
        return fechaFactura;
    }

    public void setFechaFactura(InstantFilter fechaFactura) {
        this.fechaFactura = fechaFactura;
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

    public LongFilter getEstudianteId() {
        return estudianteId;
    }

    public LongFilter estudianteId() {
        if (estudianteId == null) {
            estudianteId = new LongFilter();
        }
        return estudianteId;
    }

    public void setEstudianteId(LongFilter estudianteId) {
        this.estudianteId = estudianteId;
    }

    public LongFilter getPagosId() {
        return pagosId;
    }

    public LongFilter pagosId() {
        if (pagosId == null) {
            pagosId = new LongFilter();
        }
        return pagosId;
    }

    public void setPagosId(LongFilter pagosId) {
        this.pagosId = pagosId;
    }

    public LongFilter getPagoId() {
        return pagoId;
    }

    public LongFilter pagoId() {
        if (pagoId == null) {
            pagoId = new LongFilter();
        }
        return pagoId;
    }

    public void setPagoId(LongFilter pagoId) {
        this.pagoId = pagoId;
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
        final FacturaCriteria that = (FacturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(facturado, that.facturado) &&
            Objects.equals(fechaFactura, that.fechaFactura) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(estudianteId, that.estudianteId) &&
            Objects.equals(pagosId, that.pagosId) &&
            Objects.equals(pagoId, that.pagoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, facturado, fechaFactura, observaciones, estudianteId, pagosId, pagoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (facturado != null ? "facturado=" + facturado + ", " : "") +
            (fechaFactura != null ? "fechaFactura=" + fechaFactura + ", " : "") +
            (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
            (estudianteId != null ? "estudianteId=" + estudianteId + ", " : "") +
            (pagosId != null ? "pagosId=" + pagosId + ", " : "") +
            (pagoId != null ? "pagoId=" + pagoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
