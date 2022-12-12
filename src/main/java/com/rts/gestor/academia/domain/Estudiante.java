package com.rts.gestor.academia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Estudiante.
 */
@Entity
@Table(name = "estudiante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne
    @JsonIgnoreProperties(value = { "estudiantes", "cursos" }, allowSetters = true)
    private Asistencia asistencias;

    @ManyToOne
    @JsonIgnoreProperties(value = { "estudiantes", "pagos", "pago" }, allowSetters = true)
    private Factura facturas;

    @ManyToMany
    @JoinTable(
        name = "rel_estudiante__cursos",
        joinColumns = @JoinColumn(name = "estudiante_id"),
        inverseJoinColumns = @JoinColumn(name = "cursos_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asistencias", "asistencia", "tutors", "estudiantes" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "estudiantes", "cursos" }, allowSetters = true)
    private Asistencia asistencia;

    @ManyToOne
    @JsonIgnoreProperties(value = { "estudiantes", "pagos", "pago" }, allowSetters = true)
    private Factura factura;

    @ManyToMany(mappedBy = "estudiantes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "estudiantes" }, allowSetters = true)
    private Set<Padre> padres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estudiante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Estudiante nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public Estudiante email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Estudiante telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Estudiante observaciones(String observaciones) {
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

    public Estudiante asistencias(Asistencia asistencia) {
        this.setAsistencias(asistencia);
        return this;
    }

    public Factura getFacturas() {
        return this.facturas;
    }

    public void setFacturas(Factura factura) {
        this.facturas = factura;
    }

    public Estudiante facturas(Factura factura) {
        this.setFacturas(factura);
        return this;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

    public Estudiante cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Estudiante addCursos(Curso curso) {
        this.cursos.add(curso);
        curso.getEstudiantes().add(this);
        return this;
    }

    public Estudiante removeCursos(Curso curso) {
        this.cursos.remove(curso);
        curso.getEstudiantes().remove(this);
        return this;
    }

    public Asistencia getAsistencia() {
        return this.asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public Estudiante asistencia(Asistencia asistencia) {
        this.setAsistencia(asistencia);
        return this;
    }

    public Factura getFactura() {
        return this.factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Estudiante factura(Factura factura) {
        this.setFactura(factura);
        return this;
    }

    public Set<Padre> getPadres() {
        return this.padres;
    }

    public void setPadres(Set<Padre> padres) {
        if (this.padres != null) {
            this.padres.forEach(i -> i.removeEstudiantes(this));
        }
        if (padres != null) {
            padres.forEach(i -> i.addEstudiantes(this));
        }
        this.padres = padres;
    }

    public Estudiante padres(Set<Padre> padres) {
        this.setPadres(padres);
        return this;
    }

    public Estudiante addPadres(Padre padre) {
        this.padres.add(padre);
        padre.getEstudiantes().add(this);
        return this;
    }

    public Estudiante removePadres(Padre padre) {
        this.padres.remove(padre);
        padre.getEstudiantes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estudiante)) {
            return false;
        }
        return id != null && id.equals(((Estudiante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estudiante{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
