package com.rts.gestor.academia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rts.gestor.academia.domain.enumeration.AsistenciaEstado;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Asistencia.
 */
@Entity
@Table(name = "asistencia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Asistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AsistenciaEstado estado;

    @Column(name = "hora_entrada")
    private Instant horaEntrada;

    @Column(name = "hora_salida")
    private Instant horaSalida;

    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "asistencia")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asistencias", "facturas", "cursos", "asistencia", "factura", "padres" }, allowSetters = true)
    private Set<Estudiante> estudiantes = new HashSet<>();

    @OneToMany(mappedBy = "asistencia")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asistencias", "asistencia", "tutors", "estudiantes" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Asistencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Asistencia fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public AsistenciaEstado getEstado() {
        return this.estado;
    }

    public Asistencia estado(AsistenciaEstado estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(AsistenciaEstado estado) {
        this.estado = estado;
    }

    public Instant getHoraEntrada() {
        return this.horaEntrada;
    }

    public Asistencia horaEntrada(Instant horaEntrada) {
        this.setHoraEntrada(horaEntrada);
        return this;
    }

    public void setHoraEntrada(Instant horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Instant getHoraSalida() {
        return this.horaSalida;
    }

    public Asistencia horaSalida(Instant horaSalida) {
        this.setHoraSalida(horaSalida);
        return this;
    }

    public void setHoraSalida(Instant horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Asistencia observaciones(String observaciones) {
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
            this.estudiantes.forEach(i -> i.setAsistencia(null));
        }
        if (estudiantes != null) {
            estudiantes.forEach(i -> i.setAsistencia(this));
        }
        this.estudiantes = estudiantes;
    }

    public Asistencia estudiantes(Set<Estudiante> estudiantes) {
        this.setEstudiantes(estudiantes);
        return this;
    }

    public Asistencia addEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
        estudiante.setAsistencia(this);
        return this;
    }

    public Asistencia removeEstudiante(Estudiante estudiante) {
        this.estudiantes.remove(estudiante);
        estudiante.setAsistencia(null);
        return this;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        if (this.cursos != null) {
            this.cursos.forEach(i -> i.setAsistencia(null));
        }
        if (cursos != null) {
            cursos.forEach(i -> i.setAsistencia(this));
        }
        this.cursos = cursos;
    }

    public Asistencia cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Asistencia addCurso(Curso curso) {
        this.cursos.add(curso);
        curso.setAsistencia(this);
        return this;
    }

    public Asistencia removeCurso(Curso curso) {
        this.cursos.remove(curso);
        curso.setAsistencia(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asistencia)) {
            return false;
        }
        return id != null && id.equals(((Asistencia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asistencia{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", estado='" + getEstado() + "'" +
            ", horaEntrada='" + getHoraEntrada() + "'" +
            ", horaSalida='" + getHoraSalida() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
