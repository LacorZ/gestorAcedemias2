package com.rts.gestor.academia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Curso;
import com.rts.gestor.academia.domain.Tutor;
import com.rts.gestor.academia.repository.TutorRepository;
import com.rts.gestor.academia.service.TutorService;
import com.rts.gestor.academia.service.criteria.TutorCriteria;
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
 * Integration tests for the {@link TutorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TutorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tutors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TutorRepository tutorRepository;

    @Mock
    private TutorRepository tutorRepositoryMock;

    @Mock
    private TutorService tutorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTutorMockMvc;

    private Tutor tutor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tutor createEntity(EntityManager em) {
        Tutor tutor = new Tutor()
            .nombre(DEFAULT_NOMBRE)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .observaciones(DEFAULT_OBSERVACIONES);
        return tutor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tutor createUpdatedEntity(EntityManager em) {
        Tutor tutor = new Tutor()
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .observaciones(UPDATED_OBSERVACIONES);
        return tutor;
    }

    @BeforeEach
    public void initTest() {
        tutor = createEntity(em);
    }

    @Test
    @Transactional
    void createTutor() throws Exception {
        int databaseSizeBeforeCreate = tutorRepository.findAll().size();
        // Create the Tutor
        restTutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isCreated());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate + 1);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTutor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTutor.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testTutor.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createTutorWithExistingId() throws Exception {
        // Create the Tutor with an existing ID
        tutor.setId(1L);

        int databaseSizeBeforeCreate = tutorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setNombre(null);

        // Create the Tutor, which fails.

        restTutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = tutorRepository.findAll().size();
        // set the field null
        tutor.setEmail(null);

        // Create the Tutor, which fails.

        restTutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTutors() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList
        restTutorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tutor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTutorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(tutorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTutorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tutorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTutorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tutorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTutorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tutorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get the tutor
        restTutorMockMvc
            .perform(get(ENTITY_API_URL_ID, tutor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tutor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getTutorsByIdFiltering() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        Long id = tutor.getId();

        defaultTutorShouldBeFound("id.equals=" + id);
        defaultTutorShouldNotBeFound("id.notEquals=" + id);

        defaultTutorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTutorShouldNotBeFound("id.greaterThan=" + id);

        defaultTutorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTutorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTutorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where nombre equals to DEFAULT_NOMBRE
        defaultTutorShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the tutorList where nombre equals to UPDATED_NOMBRE
        defaultTutorShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTutorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultTutorShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the tutorList where nombre equals to UPDATED_NOMBRE
        defaultTutorShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTutorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where nombre is not null
        defaultTutorShouldBeFound("nombre.specified=true");

        // Get all the tutorList where nombre is null
        defaultTutorShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTutorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where nombre contains DEFAULT_NOMBRE
        defaultTutorShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the tutorList where nombre contains UPDATED_NOMBRE
        defaultTutorShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTutorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where nombre does not contain DEFAULT_NOMBRE
        defaultTutorShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the tutorList where nombre does not contain UPDATED_NOMBRE
        defaultTutorShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTutorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where email equals to DEFAULT_EMAIL
        defaultTutorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the tutorList where email equals to UPDATED_EMAIL
        defaultTutorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTutorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTutorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the tutorList where email equals to UPDATED_EMAIL
        defaultTutorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTutorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where email is not null
        defaultTutorShouldBeFound("email.specified=true");

        // Get all the tutorList where email is null
        defaultTutorShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTutorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where email contains DEFAULT_EMAIL
        defaultTutorShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the tutorList where email contains UPDATED_EMAIL
        defaultTutorShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTutorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where email does not contain DEFAULT_EMAIL
        defaultTutorShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the tutorList where email does not contain UPDATED_EMAIL
        defaultTutorShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTutorsByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where telefono equals to DEFAULT_TELEFONO
        defaultTutorShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the tutorList where telefono equals to UPDATED_TELEFONO
        defaultTutorShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTutorsByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultTutorShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the tutorList where telefono equals to UPDATED_TELEFONO
        defaultTutorShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTutorsByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where telefono is not null
        defaultTutorShouldBeFound("telefono.specified=true");

        // Get all the tutorList where telefono is null
        defaultTutorShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllTutorsByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where telefono contains DEFAULT_TELEFONO
        defaultTutorShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the tutorList where telefono contains UPDATED_TELEFONO
        defaultTutorShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTutorsByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where telefono does not contain DEFAULT_TELEFONO
        defaultTutorShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the tutorList where telefono does not contain UPDATED_TELEFONO
        defaultTutorShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTutorsByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultTutorShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the tutorList where observaciones equals to UPDATED_OBSERVACIONES
        defaultTutorShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllTutorsByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultTutorShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the tutorList where observaciones equals to UPDATED_OBSERVACIONES
        defaultTutorShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllTutorsByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where observaciones is not null
        defaultTutorShouldBeFound("observaciones.specified=true");

        // Get all the tutorList where observaciones is null
        defaultTutorShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllTutorsByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where observaciones contains DEFAULT_OBSERVACIONES
        defaultTutorShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the tutorList where observaciones contains UPDATED_OBSERVACIONES
        defaultTutorShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllTutorsByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultTutorShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the tutorList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultTutorShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllTutorsByCursosIsEqualToSomething() throws Exception {
        Curso cursos;
        if (TestUtil.findAll(em, Curso.class).isEmpty()) {
            tutorRepository.saveAndFlush(tutor);
            cursos = CursoResourceIT.createEntity(em);
        } else {
            cursos = TestUtil.findAll(em, Curso.class).get(0);
        }
        em.persist(cursos);
        em.flush();
        tutor.addCursos(cursos);
        tutorRepository.saveAndFlush(tutor);
        Long cursosId = cursos.getId();

        // Get all the tutorList where cursos equals to cursosId
        defaultTutorShouldBeFound("cursosId.equals=" + cursosId);

        // Get all the tutorList where cursos equals to (cursosId + 1)
        defaultTutorShouldNotBeFound("cursosId.equals=" + (cursosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTutorShouldBeFound(String filter) throws Exception {
        restTutorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tutor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restTutorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTutorShouldNotBeFound(String filter) throws Exception {
        restTutorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTutorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTutor() throws Exception {
        // Get the tutor
        restTutorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor
        Tutor updatedTutor = tutorRepository.findById(tutor.getId()).get();
        // Disconnect from session so that the updates on updatedTutor are not directly saved in db
        em.detach(updatedTutor);
        updatedTutor.nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO).observaciones(UPDATED_OBSERVACIONES);

        restTutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTutor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTutor))
            )
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTutor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTutor.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testTutor.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tutor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTutorWithPatch() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor using partial update
        Tutor partialUpdatedTutor = new Tutor();
        partialUpdatedTutor.setId(tutor.getId());

        partialUpdatedTutor.nombre(UPDATED_NOMBRE).telefono(UPDATED_TELEFONO).observaciones(UPDATED_OBSERVACIONES);

        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTutor))
            )
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTutor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTutor.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testTutor.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateTutorWithPatch() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor using partial update
        Tutor partialUpdatedTutor = new Tutor();
        partialUpdatedTutor.setId(tutor.getId());

        partialUpdatedTutor.nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO).observaciones(UPDATED_OBSERVACIONES);

        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTutor))
            )
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTutor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTutor.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testTutor.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeDelete = tutorRepository.findAll().size();

        // Delete the tutor
        restTutorMockMvc
            .perform(delete(ENTITY_API_URL_ID, tutor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
