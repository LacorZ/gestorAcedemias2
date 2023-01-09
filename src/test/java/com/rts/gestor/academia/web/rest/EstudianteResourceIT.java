package com.rts.gestor.academia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Asistencia;
import com.rts.gestor.academia.domain.Curso;
import com.rts.gestor.academia.domain.Estudiante;
import com.rts.gestor.academia.domain.Factura;
import com.rts.gestor.academia.domain.Padre;
import com.rts.gestor.academia.repository.EstudianteRepository;
import com.rts.gestor.academia.service.EstudianteService;
import com.rts.gestor.academia.service.criteria.EstudianteCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EstudianteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EstudianteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/estudiantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Mock
    private EstudianteRepository estudianteRepositoryMock;

    @Mock
    private EstudianteService estudianteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstudianteMockMvc;

    private Estudiante estudiante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estudiante createEntity(EntityManager em) {
        Estudiante estudiante = new Estudiante()
            .nombre(DEFAULT_NOMBRE)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .observaciones(DEFAULT_OBSERVACIONES);
        return estudiante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estudiante createUpdatedEntity(EntityManager em) {
        Estudiante estudiante = new Estudiante()
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .observaciones(UPDATED_OBSERVACIONES);
        return estudiante;
    }

    @BeforeEach
    public void initTest() {
        estudiante = createEntity(em);
    }

    @Test
    @Transactional
    void createEstudiante() throws Exception {
        int databaseSizeBeforeCreate = estudianteRepository.findAll().size();
        // Create the Estudiante
        restEstudianteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estudiante)))
            .andExpect(status().isCreated());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeCreate + 1);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEstudiante.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEstudiante.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testEstudiante.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createEstudianteWithExistingId() throws Exception {
        // Create the Estudiante with an existing ID
        estudiante.setId(1L);

        int databaseSizeBeforeCreate = estudianteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstudianteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estudiante)))
            .andExpect(status().isBadRequest());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().size();
        // set the field null
        estudiante.setNombre(null);

        // Create the Estudiante, which fails.

        restEstudianteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estudiante)))
            .andExpect(status().isBadRequest());

        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = estudianteRepository.findAll().size();
        // set the field null
        estudiante.setEmail(null);

        // Create the Estudiante, which fails.

        restEstudianteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estudiante)))
            .andExpect(status().isBadRequest());

        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEstudiantes() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList
        restEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estudiante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstudiantesWithEagerRelationshipsIsEnabled() throws Exception {
        when(estudianteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEstudianteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(estudianteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstudiantesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(estudianteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEstudianteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(estudianteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEstudiante() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get the estudiante
        restEstudianteMockMvc
            .perform(get(ENTITY_API_URL_ID, estudiante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estudiante.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getEstudiantesByIdFiltering() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        Long id = estudiante.getId();

        defaultEstudianteShouldBeFound("id.equals=" + id);
        defaultEstudianteShouldNotBeFound("id.notEquals=" + id);

        defaultEstudianteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEstudianteShouldNotBeFound("id.greaterThan=" + id);

        defaultEstudianteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEstudianteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstudiantesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where nombre equals to DEFAULT_NOMBRE
        defaultEstudianteShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the estudianteList where nombre equals to UPDATED_NOMBRE
        defaultEstudianteShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstudiantesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEstudianteShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the estudianteList where nombre equals to UPDATED_NOMBRE
        defaultEstudianteShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstudiantesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where nombre is not null
        defaultEstudianteShouldBeFound("nombre.specified=true");

        // Get all the estudianteList where nombre is null
        defaultEstudianteShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEstudiantesByNombreContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where nombre contains DEFAULT_NOMBRE
        defaultEstudianteShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the estudianteList where nombre contains UPDATED_NOMBRE
        defaultEstudianteShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstudiantesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where nombre does not contain DEFAULT_NOMBRE
        defaultEstudianteShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the estudianteList where nombre does not contain UPDATED_NOMBRE
        defaultEstudianteShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstudiantesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where email equals to DEFAULT_EMAIL
        defaultEstudianteShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the estudianteList where email equals to UPDATED_EMAIL
        defaultEstudianteShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEstudiantesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEstudianteShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the estudianteList where email equals to UPDATED_EMAIL
        defaultEstudianteShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEstudiantesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where email is not null
        defaultEstudianteShouldBeFound("email.specified=true");

        // Get all the estudianteList where email is null
        defaultEstudianteShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEstudiantesByEmailContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where email contains DEFAULT_EMAIL
        defaultEstudianteShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the estudianteList where email contains UPDATED_EMAIL
        defaultEstudianteShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEstudiantesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where email does not contain DEFAULT_EMAIL
        defaultEstudianteShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the estudianteList where email does not contain UPDATED_EMAIL
        defaultEstudianteShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEstudiantesByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where telefono equals to DEFAULT_TELEFONO
        defaultEstudianteShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the estudianteList where telefono equals to UPDATED_TELEFONO
        defaultEstudianteShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllEstudiantesByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultEstudianteShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the estudianteList where telefono equals to UPDATED_TELEFONO
        defaultEstudianteShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllEstudiantesByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where telefono is not null
        defaultEstudianteShouldBeFound("telefono.specified=true");

        // Get all the estudianteList where telefono is null
        defaultEstudianteShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllEstudiantesByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where telefono contains DEFAULT_TELEFONO
        defaultEstudianteShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the estudianteList where telefono contains UPDATED_TELEFONO
        defaultEstudianteShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllEstudiantesByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where telefono does not contain DEFAULT_TELEFONO
        defaultEstudianteShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the estudianteList where telefono does not contain UPDATED_TELEFONO
        defaultEstudianteShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllEstudiantesByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultEstudianteShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the estudianteList where observaciones equals to UPDATED_OBSERVACIONES
        defaultEstudianteShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllEstudiantesByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultEstudianteShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the estudianteList where observaciones equals to UPDATED_OBSERVACIONES
        defaultEstudianteShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllEstudiantesByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where observaciones is not null
        defaultEstudianteShouldBeFound("observaciones.specified=true");

        // Get all the estudianteList where observaciones is null
        defaultEstudianteShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllEstudiantesByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where observaciones contains DEFAULT_OBSERVACIONES
        defaultEstudianteShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the estudianteList where observaciones contains UPDATED_OBSERVACIONES
        defaultEstudianteShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllEstudiantesByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        // Get all the estudianteList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultEstudianteShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the estudianteList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultEstudianteShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllEstudiantesByAsistenciasIsEqualToSomething() throws Exception {
        Asistencia asistencias;
        if (TestUtil.findAll(em, Asistencia.class).isEmpty()) {
            estudianteRepository.saveAndFlush(estudiante);
            asistencias = AsistenciaResourceIT.createEntity(em);
        } else {
            asistencias = TestUtil.findAll(em, Asistencia.class).get(0);
        }
        em.persist(asistencias);
        em.flush();
        estudiante.setAsistencias(asistencias);
        estudianteRepository.saveAndFlush(estudiante);
        Long asistenciasId = asistencias.getId();

        // Get all the estudianteList where asistencias equals to asistenciasId
        defaultEstudianteShouldBeFound("asistenciasId.equals=" + asistenciasId);

        // Get all the estudianteList where asistencias equals to (asistenciasId + 1)
        defaultEstudianteShouldNotBeFound("asistenciasId.equals=" + (asistenciasId + 1));
    }

    @Test
    @Transactional
    void getAllEstudiantesByFacturasIsEqualToSomething() throws Exception {
        Factura facturas;
        if (TestUtil.findAll(em, Factura.class).isEmpty()) {
            estudianteRepository.saveAndFlush(estudiante);
            facturas = FacturaResourceIT.createEntity(em);
        } else {
            facturas = TestUtil.findAll(em, Factura.class).get(0);
        }
        em.persist(facturas);
        em.flush();
        estudiante.setFacturas(facturas);
        estudianteRepository.saveAndFlush(estudiante);
        Long facturasId = facturas.getId();

        // Get all the estudianteList where facturas equals to facturasId
        defaultEstudianteShouldBeFound("facturasId.equals=" + facturasId);

        // Get all the estudianteList where facturas equals to (facturasId + 1)
        defaultEstudianteShouldNotBeFound("facturasId.equals=" + (facturasId + 1));
    }

    @Test
    @Transactional
    void getAllEstudiantesByCursosIsEqualToSomething() throws Exception {
        Curso cursos;
        if (TestUtil.findAll(em, Curso.class).isEmpty()) {
            estudianteRepository.saveAndFlush(estudiante);
            cursos = CursoResourceIT.createEntity(em);
        } else {
            cursos = TestUtil.findAll(em, Curso.class).get(0);
        }
        em.persist(cursos);
        em.flush();
        estudiante.addCursos(cursos);
        estudianteRepository.saveAndFlush(estudiante);
        Long cursosId = cursos.getId();

        // Get all the estudianteList where cursos equals to cursosId
        defaultEstudianteShouldBeFound("cursosId.equals=" + cursosId);

        // Get all the estudianteList where cursos equals to (cursosId + 1)
        defaultEstudianteShouldNotBeFound("cursosId.equals=" + (cursosId + 1));
    }

    @Test
    @Transactional
    void getAllEstudiantesByAsistenciaIsEqualToSomething() throws Exception {
        Asistencia asistencia;
        if (TestUtil.findAll(em, Asistencia.class).isEmpty()) {
            estudianteRepository.saveAndFlush(estudiante);
            asistencia = AsistenciaResourceIT.createEntity(em);
        } else {
            asistencia = TestUtil.findAll(em, Asistencia.class).get(0);
        }
        em.persist(asistencia);
        em.flush();
        estudiante.setAsistencia(asistencia);
        estudianteRepository.saveAndFlush(estudiante);
        Long asistenciaId = asistencia.getId();

        // Get all the estudianteList where asistencia equals to asistenciaId
        defaultEstudianteShouldBeFound("asistenciaId.equals=" + asistenciaId);

        // Get all the estudianteList where asistencia equals to (asistenciaId + 1)
        defaultEstudianteShouldNotBeFound("asistenciaId.equals=" + (asistenciaId + 1));
    }

    @Test
    @Transactional
    void getAllEstudiantesByFacturaIsEqualToSomething() throws Exception {
        Factura factura;
        if (TestUtil.findAll(em, Factura.class).isEmpty()) {
            estudianteRepository.saveAndFlush(estudiante);
            factura = FacturaResourceIT.createEntity(em);
        } else {
            factura = TestUtil.findAll(em, Factura.class).get(0);
        }
        em.persist(factura);
        em.flush();
        estudiante.setFactura(factura);
        estudianteRepository.saveAndFlush(estudiante);
        Long facturaId = factura.getId();

        // Get all the estudianteList where factura equals to facturaId
        defaultEstudianteShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the estudianteList where factura equals to (facturaId + 1)
        defaultEstudianteShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }

    @Test
    @Transactional
    void getAllEstudiantesByPadresIsEqualToSomething() throws Exception {
        Padre padres;
        if (TestUtil.findAll(em, Padre.class).isEmpty()) {
            estudianteRepository.saveAndFlush(estudiante);
            padres = PadreResourceIT.createEntity(em);
        } else {
            padres = TestUtil.findAll(em, Padre.class).get(0);
        }
        em.persist(padres);
        em.flush();
        estudiante.addPadres(padres);
        estudianteRepository.saveAndFlush(estudiante);
        Long padresId = padres.getId();

        // Get all the estudianteList where padres equals to padresId
        defaultEstudianteShouldBeFound("padresId.equals=" + padresId);

        // Get all the estudianteList where padres equals to (padresId + 1)
        defaultEstudianteShouldNotBeFound("padresId.equals=" + (padresId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstudianteShouldBeFound(String filter) throws Exception {
        restEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estudiante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstudianteShouldNotBeFound(String filter) throws Exception {
        restEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstudiante() throws Exception {
        // Get the estudiante
        restEstudianteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstudiante() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();

        // Update the estudiante
        Estudiante updatedEstudiante = estudianteRepository.findById(estudiante.getId()).get();
        // Disconnect from session so that the updates on updatedEstudiante are not directly saved in db
        em.detach(updatedEstudiante);
        updatedEstudiante.nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO).observaciones(UPDATED_OBSERVACIONES);

        restEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEstudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstudiante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEstudiante.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEstudiante.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();
        estudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();
        estudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();
        estudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudianteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estudiante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstudianteWithPatch() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();

        // Update the estudiante using partial update
        Estudiante partialUpdatedEstudiante = new Estudiante();
        partialUpdatedEstudiante.setId(estudiante.getId());

        partialUpdatedEstudiante.nombre(UPDATED_NOMBRE).telefono(UPDATED_TELEFONO);

        restEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstudiante.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEstudiante.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEstudiante.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateEstudianteWithPatch() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();

        // Update the estudiante using partial update
        Estudiante partialUpdatedEstudiante = new Estudiante();
        partialUpdatedEstudiante.setId(estudiante.getId());

        partialUpdatedEstudiante
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .observaciones(UPDATED_OBSERVACIONES);

        restEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
        Estudiante testEstudiante = estudianteList.get(estudianteList.size() - 1);
        assertThat(testEstudiante.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstudiante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEstudiante.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEstudiante.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();
        estudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();
        estudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = estudianteRepository.findAll().size();
        estudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(estudiante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estudiante in the database
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstudiante() throws Exception {
        // Initialize the database
        estudianteRepository.saveAndFlush(estudiante);

        int databaseSizeBeforeDelete = estudianteRepository.findAll().size();

        // Delete the estudiante
        restEstudianteMockMvc
            .perform(delete(ENTITY_API_URL_ID, estudiante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Estudiante> estudianteList = estudianteRepository.findAll();
        assertThat(estudianteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
