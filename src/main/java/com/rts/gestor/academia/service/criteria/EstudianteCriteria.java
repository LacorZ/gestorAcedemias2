package com.rts.gestor.academia.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.rts.gestor.academia.domain.Estudiante} entity. This class is used
 * in {@link com.rts.gestor.academia.web.rest.EstudianteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /estudiantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstudianteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter email;

    private StringFilter telefono;

    private StringFilter observaciones;

    private LongFilter asistenciasId;

    private LongFilter facturasId;

    private LongFilter cursosId;

    private LongFilter asistenciaId;

    private LongFilter facturaId;

    private LongFilter padresId;

    private Boolean distinct;

    public EstudianteCriteria() {}

    public EstudianteCriteria(EstudianteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telefono = other.telefono == null ? null : other.telefono.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.asistenciasId = other.asistenciasId == null ? null : other.asistenciasId.copy();
        this.facturasId = other.facturasId == null ? null : other.facturasId.copy();
        this.cursosId = other.cursosId == null ? null : other.cursosId.copy();
        this.asistenciaId = other.asistenciaId == null ? null : other.asistenciaId.copy();
        this.facturaId = other.facturaId == null ? null : other.facturaId.copy();
        this.padresId = other.padresId == null ? null : other.padresId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EstudianteCriteria copy() {
        return new EstudianteCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public StringFilter telefono() {
        if (telefono == null) {
            telefono = new StringFilter();
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
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

    public LongFilter getAsistenciasId() {
        return asistenciasId;
    }

    public LongFilter asistenciasId() {
        if (asistenciasId == null) {
            asistenciasId = new LongFilter();
        }
        return asistenciasId;
    }

    public void setAsistenciasId(LongFilter asistenciasId) {
        this.asistenciasId = asistenciasId;
    }

    public LongFilter getFacturasId() {
        return facturasId;
    }

    public LongFilter facturasId() {
        if (facturasId == null) {
            facturasId = new LongFilter();
        }
        return facturasId;
    }

    public void setFacturasId(LongFilter facturasId) {
        this.facturasId = facturasId;
    }

    public LongFilter getCursosId() {
        return cursosId;
    }

    public LongFilter cursosId() {
        if (cursosId == null) {
            cursosId = new LongFilter();
        }
        return cursosId;
    }

    public void setCursosId(LongFilter cursosId) {
        this.cursosId = cursosId;
    }

    public LongFilter getAsistenciaId() {
        return asistenciaId;
    }

    public LongFilter asistenciaId() {
        if (asistenciaId == null) {
            asistenciaId = new LongFilter();
        }
        return asistenciaId;
    }

    public void setAsistenciaId(LongFilter asistenciaId) {
        this.asistenciaId = asistenciaId;
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

    public LongFilter getPadresId() {
        return padresId;
    }

    public LongFilter padresId() {
        if (padresId == null) {
            padresId = new LongFilter();
        }
        return padresId;
    }

    public void setPadresId(LongFilter padresId) {
        this.padresId = padresId;
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
        final EstudianteCriteria that = (EstudianteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(asistenciasId, that.asistenciasId) &&
            Objects.equals(facturasId, that.facturasId) &&
            Objects.equals(cursosId, that.cursosId) &&
            Objects.equals(asistenciaId, that.asistenciaId) &&
            Objects.equals(facturaId, that.facturaId) &&
            Objects.equals(padresId, that.padresId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            email,
            telefono,
            observaciones,
            asistenciasId,
            facturasId,
            cursosId,
            asistenciaId,
            facturaId,
            padresId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstudianteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (telefono != null ? "telefono=" + telefono + ", " : "") +
            (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
            (asistenciasId != null ? "asistenciasId=" + asistenciasId + ", " : "") +
            (facturasId != null ? "facturasId=" + facturasId + ", " : "") +
            (cursosId != null ? "cursosId=" + cursosId + ", " : "") +
            (asistenciaId != null ? "asistenciaId=" + asistenciaId + ", " : "") +
            (facturaId != null ? "facturaId=" + facturaId + ", " : "") +
            (padresId != null ? "padresId=" + padresId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
