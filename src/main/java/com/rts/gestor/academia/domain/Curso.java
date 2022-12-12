package com.rts.gestor.academia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Curso.
 */
@Entity
@Table(name = "curso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne
    @JsonIgnoreProperties(value = { "estudiantes", "cursos" }, allowSetters = true)
    private Asistencia asistencias;

    @ManyToOne
    @JsonIgnoreProperties(value = { "estudiantes", "cursos" }, allowSetters = true)
    private Asistencia asistencia;

    @ManyToMany(mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cursos" }, allowSetters = true)
    private Set<Tutor> tutors = new HashSet<>();

    @ManyToMany(mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asistencias", "facturas", "cursos", "asistencia", "factura", "padres" }, allowSetters = true)
    private Set<Estudiante> estudiantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Curso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Curso nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Curso descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Curso price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Curso fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Curso fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Curso observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Asistencia getAsistencias() {
        return this.asistencias;
    }

    public void setAsistencias(Asistencia asistencia) {
        this.asistencias = asistencia;
    }

    public Curso asistencias(Asistencia asistencia) {
        this.setAsistencias(asistencia);
        return this;
    }

    public Asistencia getAsistencia() {
        return this.asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public Curso asistencia(Asistencia asistencia) {
        this.setAsistencia(asistencia);
        return this;
    }

    public Set<Tutor> getTutors() {
        return this.tutors;
    }

    public void setTutors(Set<Tutor> tutors) {
        if (this.tutors != null) {
            this.tutors.forEach(i -> i.removeCursos(this));
        }
        if (tutors != null) {
            tutors.forEach(i -> i.addCursos(this));
        }
        this.tutors = tutors;
    }

    public Curso tutors(Set<Tutor> tutors) {
        this.setTutors(tutors);
        return this;
    }

    public Curso addTutors(Tutor tutor) {
        this.tutors.add(tutor);
        tutor.getCursos().add(this);
        return this;
    }

    public Curso removeTutors(Tutor tutor) {
        this.tutors.remove(tutor);
        tutor.getCursos().remove(this);
        return this;
    }

    public Set<Estudiante> getEstudiantes() {
        return this.estudiantes;
    }

    public void setEstudiantes(Set<Estudiante> estudiantes) {
        if (this.estudiantes != null) {
            this.estudiantes.forEach(i -> i.removeCursos(this));
        }
        if (estudiantes != null) {
            estudiantes.forEach(i -> i.addCursos(this));
        }
        this.estudiantes = estudiantes;
    }

    public Curso estudiantes(Set<Estudiante> estudiantes) {
        this.setEstudiantes(estudiantes);
        return this;
    }

    public Curso addEstudiantes(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
        estudiante.getCursos().add(this);
        return this;
    }

    public Curso removeEstudiantes(Estudiante estudiante) {
        this.estudiantes.remove(estudiante);
        estudiante.getCursos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        return id != null && id.equals(((Curso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Curso{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", price=" + getPrice() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
