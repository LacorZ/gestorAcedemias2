package com.rts.gestor.academia.service.criteria;

import com.rts.gestor.academia.domain.enumeration.AsistenciaEstado;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.rts.gestor.academia.domain.Asistencia} entity. This class is used
 * in {@link com.rts.gestor.academia.web.rest.AsistenciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asistencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsistenciaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AsistenciaEstado
     */
    public static class AsistenciaEstadoFilter extends Filter<AsistenciaEstado> {

        public AsistenciaEstadoFilter() {}

        public AsistenciaEstadoFilter(AsistenciaEstadoFilter filter) {
            super(filter);
        }

        @Override
        public AsistenciaEstadoFilter copy() {
            return new AsistenciaEstadoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private AsistenciaEstadoFilter estado;

    private InstantFilter horaEntrada;

    private InstantFilter horaSalida;

    private StringFilter observaciones;

    private LongFilter estudianteId;

    private LongFilter cursoId;

    private Boolean distinct;

    public AsistenciaCriteria() {}

    public AsistenciaCriteria(AsistenciaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.horaEntrada = other.horaEntrada == null ? null : other.horaEntrada.copy();
        this.horaSalida = other.horaSalida == null ? null : other.horaSalida.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.estudianteId = other.estudianteId == null ? null : other.estudianteId.copy();
        this.cursoId = other.cursoId == null ? null : other.cursoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AsistenciaCriteria copy() {
        return new AsistenciaCriteria(this);
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

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public LocalDateFilter fecha() {
        if (fecha == null) {
            fecha = new LocalDateFilter();
        }
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public AsistenciaEstadoFilter getEstado() {
        return estado;
    }

    public AsistenciaEstadoFilter estado() {
        if (estado == null) {
            estado = new AsistenciaEstadoFilter();
        }
        return estado;
    }

    public void setEstado(AsistenciaEstadoFilter estado) {
        this.estado = estado;
    }

    public InstantFilter getHoraEntrada() {
        return horaEntrada;
    }

    public InstantFilter horaEntrada() {
        if (horaEntrada == null) {
            horaEntrada = new InstantFilter();
        }
        return horaEntrada;
    }

    public void setHoraEntrada(InstantFilter horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public InstantFilter getHoraSalida() {
        return horaSalida;
    }

    public InstantFilter horaSalida() {
        if (horaSalida == null) {
            horaSalida = new InstantFilter();
        }
        return horaSalida;
    }

    public void setHoraSalida(InstantFilter horaSalida) {
        this.horaSalida = horaSalida;
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

    public LongFilter getCursoId() {
        return cursoId;
    }

    public LongFilter cursoId() {
        if (cursoId == null) {
            cursoId = new LongFilter();
        }
        return cursoId;
    }

    public void setCursoId(LongFilter cursoId) {
        this.cursoId = cursoId;
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
        final AsistenciaCriteria that = (AsistenciaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(horaEntrada, that.horaEntrada) &&
            Objects.equals(horaSalida, that.horaSalida) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(estudianteId, that.estudianteId) &&
            Objects.equals(cursoId, that.cursoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, estado, horaEntrada, horaSalida, observaciones, estudianteId, cursoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsistenciaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (horaEntrada != null ? "horaEntrada=" + horaEntrada + ", " : "") +
            (horaSalida != null ? "horaSalida=" + horaSalida + ", " : "") +
            (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
            (estudianteId != null ? "estudianteId=" + estudianteId + ", " : "") +
            (cursoId != null ? "cursoId=" + cursoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
