package com.rts.gestor.academia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Asistencia;
import com.rts.gestor.academia.domain.Curso;
import com.rts.gestor.academia.domain.Estudiante;
import com.rts.gestor.academia.domain.Tutor;
import com.rts.gestor.academia.repository.CursoRepository;
import com.rts.gestor.academia.service.criteria.CursoCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CursoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CursoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cursos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCursoMockMvc;

    private Curso curso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createEntity(EntityManager em) {
        Curso curso = new Curso()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .observaciones(DEFAULT_OBSERVACIONES);
        return curso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createUpdatedEntity(EntityManager em) {
        Curso curso = new Curso()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .observaciones(UPDATED_OBSERVACIONES);
        return curso;
    }

    @BeforeEach
    public void initTest() {
        curso = createEntity(em);
    }

    @Test
    @Transactional
    void createCurso() throws Exception {
        int databaseSizeBeforeCreate = cursoRepository.findAll().size();
        // Create the Curso
        restCursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(curso)))
            .andExpect(status().isCreated());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeCreate + 1);
        Curso testCurso = cursoList.get(cursoList.size() - 1);
        assertThat(testCurso.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCurso.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCurso.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testCurso.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testCurso.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createCursoWithExistingId() throws Exception {
        // Create the Curso with an existing ID
        curso.setId(1L);

        int databaseSizeBeforeCreate = cursoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(curso)))
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursoRepository.findAll().size();
        // set the field null
        curso.setNombre(null);

        // Create the Curso, which fails.

        restCursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(curso)))
            .andExpect(status().isBadRequest());

        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCursos() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList
        restCursoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getCurso() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get the curso
        restCursoMockMvc
            .perform(get(ENTITY_API_URL_ID, curso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(curso.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getCursosByIdFiltering() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        Long id = curso.getId();

        defaultCursoShouldBeFound("id.equals=" + id);
        defaultCursoShouldNotBeFound("id.notEquals=" + id);

        defaultCursoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCursoShouldNotBeFound("id.greaterThan=" + id);

        defaultCursoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCursoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCursosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nombre equals to DEFAULT_NOMBRE
        defaultCursoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the cursoList where nombre equals to UPDATED_NOMBRE
        defaultCursoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCursosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultCursoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the cursoList where nombre equals to UPDATED_NOMBRE
        defaultCursoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCursosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nombre is not null
        defaultCursoShouldBeFound("nombre.specified=true");

        // Get all the cursoList where nombre is null
        defaultCursoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByNombreContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nombre contains DEFAULT_NOMBRE
        defaultCursoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the cursoList where nombre contains UPDATED_NOMBRE
        defaultCursoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCursosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nombre does not contain DEFAULT_NOMBRE
        defaultCursoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the cursoList where nombre does not contain UPDATED_NOMBRE
        defaultCursoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where descripcion equals to DEFAULT_DESCRIPCION
        defaultCursoShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the cursoList where descripcion equals to UPDATED_DESCRIPCION
        defaultCursoShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultCursoShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the cursoList where descripcion equals to UPDATED_DESCRIPCION
        defaultCursoShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where descripcion is not null
        defaultCursoShouldBeFound("descripcion.specified=true");

        // Get all the cursoList where descripcion is null
        defaultCursoShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where descripcion contains DEFAULT_DESCRIPCION
        defaultCursoShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the cursoList where descripcion contains UPDATED_DESCRIPCION
        defaultCursoShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultCursoShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the cursoList where descripcion does not contain UPDATED_DESCRIPCION
        defaultCursoShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultCursoShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the cursoList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultCursoShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultCursoShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the cursoList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultCursoShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaInicio is not null
        defaultCursoShouldBeFound("fechaInicio.specified=true");

        // Get all the cursoList where fechaInicio is null
        defaultCursoShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaInicio is greater than or equal to DEFAULT_FECHA_INICIO
        defaultCursoShouldBeFound("fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the cursoList where fechaInicio is greater than or equal to UPDATED_FECHA_INICIO
        defaultCursoShouldNotBeFound("fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaInicio is less than or equal to DEFAULT_FECHA_INICIO
        defaultCursoShouldBeFound("fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO);

        // Get all the cursoList where fechaInicio is less than or equal to SMALLER_FECHA_INICIO
        defaultCursoShouldNotBeFound("fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaInicio is less than DEFAULT_FECHA_INICIO
        defaultCursoShouldNotBeFound("fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);

        // Get all the cursoList where fechaInicio is less than UPDATED_FECHA_INICIO
        defaultCursoShouldBeFound("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaInicio is greater than DEFAULT_FECHA_INICIO
        defaultCursoShouldNotBeFound("fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);

        // Get all the cursoList where fechaInicio is greater than SMALLER_FECHA_INICIO
        defaultCursoShouldBeFound("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaFin equals to DEFAULT_FECHA_FIN
        defaultCursoShouldBeFound("fechaFin.equals=" + DEFAULT_FECHA_FIN);

        // Get all the cursoList where fechaFin equals to UPDATED_FECHA_FIN
        defaultCursoShouldNotBeFound("fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaFin in DEFAULT_FECHA_FIN or UPDATED_FECHA_FIN
        defaultCursoShouldBeFound("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN);

        // Get all the cursoList where fechaFin equals to UPDATED_FECHA_FIN
        defaultCursoShouldNotBeFound("fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaFin is not null
        defaultCursoShouldBeFound("fechaFin.specified=true");

        // Get all the cursoList where fechaFin is null
        defaultCursoShouldNotBeFound("fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaFin is greater than or equal to DEFAULT_FECHA_FIN
        defaultCursoShouldBeFound("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the cursoList where fechaFin is greater than or equal to UPDATED_FECHA_FIN
        defaultCursoShouldNotBeFound("fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaFin is less than or equal to DEFAULT_FECHA_FIN
        defaultCursoShouldBeFound("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the cursoList where fechaFin is less than or equal to SMALLER_FECHA_FIN
        defaultCursoShouldNotBeFound("fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaFin is less than DEFAULT_FECHA_FIN
        defaultCursoShouldNotBeFound("fechaFin.lessThan=" + DEFAULT_FECHA_FIN);

        // Get all the cursoList where fechaFin is less than UPDATED_FECHA_FIN
        defaultCursoShouldBeFound("fechaFin.lessThan=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where fechaFin is greater than DEFAULT_FECHA_FIN
        defaultCursoShouldNotBeFound("fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);

        // Get all the cursoList where fechaFin is greater than SMALLER_FECHA_FIN
        defaultCursoShouldBeFound("fechaFin.greaterThan=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllCursosByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultCursoShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the cursoList where observaciones equals to UPDATED_OBSERVACIONES
        defaultCursoShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllCursosByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultCursoShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the cursoList where observaciones equals to UPDATED_OBSERVACIONES
        defaultCursoShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllCursosByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where observaciones is not null
        defaultCursoShouldBeFound("observaciones.specified=true");

        // Get all the cursoList where observaciones is null
        defaultCursoShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllCursosByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where observaciones contains DEFAULT_OBSERVACIONES
        defaultCursoShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the cursoList where observaciones contains UPDATED_OBSERVACIONES
        defaultCursoShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllCursosByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultCursoShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the cursoList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultCursoShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllCursosByAsistenciasIsEqualToSomething() throws Exception {
        Asistencia asistencias;
        if (TestUtil.findAll(em, Asistencia.class).isEmpty()) {
            cursoRepository.saveAndFlush(curso);
            asistencias = AsistenciaResourceIT.createEntity(em);
        } else {
            asistencias = TestUtil.findAll(em, Asistencia.class).get(0);
        }
        em.persist(asistencias);
        em.flush();
        curso.setAsistencias(asistencias);
        cursoRepository.saveAndFlush(curso);
        Long asistenciasId = asistencias.getId();

        // Get all the cursoList where asistencias equals to asistenciasId
        defaultCursoShouldBeFound("asistenciasId.equals=" + asistenciasId);

        // Get all the cursoList where asistencias equals to (asistenciasId + 1)
        defaultCursoShouldNotBeFound("asistenciasId.equals=" + (asistenciasId + 1));
    }

    @Test
    @Transactional
    void getAllCursosByAsistenciaIsEqualToSomething() throws Exception {
        Asistencia asistencia;
        if (TestUtil.findAll(em, Asistencia.class).isEmpty()) {
            cursoRepository.saveAndFlush(curso);
            asistencia = AsistenciaResourceIT.createEntity(em);
        } else {
            asistencia = TestUtil.findAll(em, Asistencia.class).get(0);
        }
        em.persist(asistencia);
        em.flush();
        curso.setAsistencia(asistencia);
        cursoRepository.saveAndFlush(curso);
        Long asistenciaId = asistencia.getId();

        // Get all the cursoList where asistencia equals to asistenciaId
        defaultCursoShouldBeFound("asistenciaId.equals=" + asistenciaId);

        // Get all the cursoList where asistencia equals to (asistenciaId + 1)
        defaultCursoShouldNotBeFound("asistenciaId.equals=" + (asistenciaId + 1));
    }

    @Test
    @Transactional
    void getAllCursosByTutoresIsEqualToSomething() throws Exception {
        Tutor tutores;
        if (TestUtil.findAll(em, Tutor.class).isEmpty()) {
            cursoRepository.saveAndFlush(curso);
            tutores = TutorResourceIT.createEntity(em);
        } else {
            tutores = TestUtil.findAll(em, Tutor.class).get(0);
        }
        em.persist(tutores);
        em.flush();
        curso.addTutores(tutores);
        cursoRepository.saveAndFlush(curso);
        Long tutoresId = tutores.getId();

        // Get all the cursoList where tutores equals to tutoresId
        defaultCursoShouldBeFound("tutoresId.equals=" + tutoresId);

        // Get all the cursoList where tutores equals to (tutoresId + 1)
        defaultCursoShouldNotBeFound("tutoresId.equals=" + (tutoresId + 1));
    }

    @Test
    @Transactional
    void getAllCursosByEstudiantesIsEqualToSomething() throws Exception {
        Estudiante estudiantes;
        if (TestUtil.findAll(em, Estudiante.class).isEmpty()) {
            cursoRepository.saveAndFlush(curso);
            estudiantes = EstudianteResourceIT.createEntity(em);
        } else {
            estudiantes = TestUtil.findAll(em, Estudiante.class).get(0);
        }
        em.persist(estudiantes);
        em.flush();
        curso.addEstudiantes(estudiantes);
        cursoRepository.saveAndFlush(curso);
        Long estudiantesId = estudiantes.getId();

        // Get all the cursoList where estudiantes equals to estudiantesId
        defaultCursoShouldBeFound("estudiantesId.equals=" + estudiantesId);

        // Get all the cursoList where estudiantes equals to (estudiantesId + 1)
        defaultCursoShouldNotBeFound("estudiantesId.equals=" + (estudiantesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCursoShouldBeFound(String filter) throws Exception {
        restCursoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restCursoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCursoShouldNotBeFound(String filter) throws Exception {
        restCursoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCursoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCurso() throws Exception {
        // Get the curso
        restCursoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCurso() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();

        // Update the curso
        Curso updatedCurso = cursoRepository.findById(curso.getId()).get();
        // Disconnect from session so that the updates on updatedCurso are not directly saved in db
        em.detach(updatedCurso);
        updatedCurso
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .observaciones(UPDATED_OBSERVACIONES);

        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCurso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
        Curso testCurso = cursoList.get(cursoList.size() - 1);
        assertThat(testCurso.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCurso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCurso.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testCurso.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testCurso.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingCurso() throws Exception {
        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();
        curso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, curso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurso() throws Exception {
        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();
        curso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurso() throws Exception {
        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();
        curso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(curso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCursoWithPatch() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();

        // Update the curso using partial update
        Curso partialUpdatedCurso = new Curso();
        partialUpdatedCurso.setId(curso.getId());

        partialUpdatedCurso.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaInicio(UPDATED_FECHA_INICIO);

        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
        Curso testCurso = cursoList.get(cursoList.size() - 1);
        assertThat(testCurso.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCurso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCurso.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testCurso.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testCurso.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateCursoWithPatch() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();

        // Update the curso using partial update
        Curso partialUpdatedCurso = new Curso();
        partialUpdatedCurso.setId(curso.getId());

        partialUpdatedCurso
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .observaciones(UPDATED_OBSERVACIONES);

        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
        Curso testCurso = cursoList.get(cursoList.size() - 1);
        assertThat(testCurso.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCurso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCurso.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testCurso.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testCurso.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingCurso() throws Exception {
        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();
        curso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, curso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurso() throws Exception {
        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();
        curso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(curso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurso() throws Exception {
        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();
        curso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(curso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurso() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        int databaseSizeBeforeDelete = cursoRepository.findAll().size();

        // Delete the curso
        restCursoMockMvc
            .perform(delete(ENTITY_API_URL_ID, curso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
