package com.rts.gestor.academia.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.rts.gestor.academia.domain.Padre} entity. This class is used
 * in {@link com.rts.gestor.academia.web.rest.PadreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /padres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PadreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter email;

    private StringFilter telefono;

    private StringFilter observaciones;

    private LongFilter estudiantesId;

    private Boolean distinct;

    public PadreCriteria() {}

    public PadreCriteria(PadreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telefono = other.telefono == null ? null : other.telefono.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.estudiantesId = other.estudiantesId == null ? null : other.estudiantesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PadreCriteria copy() {
        return new PadreCriteria(this);
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

    public LongFilter getEstudiantesId() {
        return estudiantesId;
    }

    public LongFilter estudiantesId() {
        if (estudiantesId == null) {
            estudiantesId = new LongFilter();
        }
        return estudiantesId;
    }

    public void setEstudiantesId(LongFilter estudiantesId) {
        this.estudiantesId = estudiantesId;
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
        final PadreCriteria that = (PadreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(estudiantesId, that.estudiantesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, email, telefono, observaciones, estudiantesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PadreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (telefono != null ? "telefono=" + telefono + ", " : "") +
            (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
            (estudiantesId != null ? "estudiantesId=" + estudiantesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
