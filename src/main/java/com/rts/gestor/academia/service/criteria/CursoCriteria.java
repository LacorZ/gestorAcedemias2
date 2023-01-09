package com.rts.gestor.academia.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.rts.gestor.academia.domain.Curso} entity. This class is used
 * in {@link com.rts.gestor.academia.web.rest.CursoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cursos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CursoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaFin;

    private StringFilter observaciones;

    private LongFilter asistenciasId;

    private LongFilter asistenciaId;

    private LongFilter tutoresId;

    private LongFilter estudiantesId;

    private Boolean distinct;

    public CursoCriteria() {}

    public CursoCriteria(CursoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaFin = other.fechaFin == null ? null : other.fechaFin.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.asistenciasId = other.asistenciasId == null ? null : other.asistenciasId.copy();
        this.asistenciaId = other.asistenciaId == null ? null : other.asistenciaId.copy();
        this.tutoresId = other.tutoresId == null ? null : other.tutoresId.copy();
        this.estudiantesId = other.estudiantesId == null ? null : other.estudiantesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CursoCriteria copy() {
        return new CursoCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateFilter getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateFilter fechaInicio() {
        if (fechaInicio == null) {
            fechaInicio = new LocalDateFilter();
        }
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateFilter fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateFilter getFechaFin() {
        return fechaFin;
    }

    public LocalDateFilter fechaFin() {
        if (fechaFin == null) {
            fechaFin = new LocalDateFilter();
        }
        return fechaFin;
    }

    public void setFechaFin(LocalDateFilter fechaFin) {
        this.fechaFin = fechaFin;
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

    public LongFilter getTutoresId() {
        return tutoresId;
    }

    public LongFilter tutoresId() {
        if (tutoresId == null) {
            tutoresId = new LongFilter();
        }
        return tutoresId;
    }

    public void setTutoresId(LongFilter tutoresId) {
        this.tutoresId = tutoresId;
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
        final CursoCriteria that = (CursoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(asistenciasId, that.asistenciasId) &&
            Objects.equals(asistenciaId, that.asistenciaId) &&
            Objects.equals(tutoresId, that.tutoresId) &&
            Objects.equals(estudiantesId, that.estudiantesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            descripcion,
            fechaInicio,
            fechaFin,
            observaciones,
            asistenciasId,
            asistenciaId,
            tutoresId,
            estudiantesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaFin != null ? "fechaFin=" + fechaFin + ", " : "") +
            (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
            (asistenciasId != null ? "asistenciasId=" + asistenciasId + ", " : "") +
            (asistenciaId != null ? "asistenciaId=" + asistenciaId + ", " : "") +
            (tutoresId != null ? "tutoresId=" + tutoresId + ", " : "") +
            (estudiantesId != null ? "estudiantesId=" + estudiantesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
