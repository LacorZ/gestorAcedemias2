package com.rts.gestor.academia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Estudiante;
import com.rts.gestor.academia.domain.Padre;
import com.rts.gestor.academia.repository.PadreRepository;
import com.rts.gestor.academia.service.PadreService;
import com.rts.gestor.academia.service.criteria.PadreCriteria;
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
 * Integration tests for the {@link PadreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PadreResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/padres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PadreRepository padreRepository;

    @Mock
    private PadreRepository padreRepositoryMock;

    @Mock
    private PadreService padreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPadreMockMvc;

    private Padre padre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Padre createEntity(EntityManager em) {
        Padre padre = new Padre()
            .nombre(DEFAULT_NOMBRE)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .observaciones(DEFAULT_OBSERVACIONES);
        return padre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Padre createUpdatedEntity(EntityManager em) {
        Padre padre = new Padre()
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .observaciones(UPDATED_OBSERVACIONES);
        return padre;
    }

    @BeforeEach
    public void initTest() {
        padre = createEntity(em);
    }

    @Test
    @Transactional
    void createPadre() throws Exception {
        int databaseSizeBeforeCreate = padreRepository.findAll().size();
        // Create the Padre
        restPadreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padre)))
            .andExpect(status().isCreated());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeCreate + 1);
        Padre testPadre = padreList.get(padreList.size() - 1);
        assertThat(testPadre.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPadre.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPadre.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPadre.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createPadreWithExistingId() throws Exception {
        // Create the Padre with an existing ID
        padre.setId(1L);

        int databaseSizeBeforeCreate = padreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPadreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padre)))
            .andExpect(status().isBadRequest());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = padreRepository.findAll().size();
        // set the field null
        padre.setNombre(null);

        // Create the Padre, which fails.

        restPadreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padre)))
            .andExpect(status().isBadRequest());

        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = padreRepository.findAll().size();
        // set the field null
        padre.setEmail(null);

        // Create the Padre, which fails.

        restPadreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padre)))
            .andExpect(status().isBadRequest());

        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPadres() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList
        restPadreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(padre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPadresWithEagerRelationshipsIsEnabled() throws Exception {
        when(padreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPadreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(padreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPadresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(padreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPadreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(padreRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPadre() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get the padre
        restPadreMockMvc
            .perform(get(ENTITY_API_URL_ID, padre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(padre.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getPadresByIdFiltering() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        Long id = padre.getId();

        defaultPadreShouldBeFound("id.equals=" + id);
        defaultPadreShouldNotBeFound("id.notEquals=" + id);

        defaultPadreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPadreShouldNotBeFound("id.greaterThan=" + id);

        defaultPadreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPadreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPadresByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where nombre equals to DEFAULT_NOMBRE
        defaultPadreShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the padreList where nombre equals to UPDATED_NOMBRE
        defaultPadreShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPadresByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPadreShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the padreList where nombre equals to UPDATED_NOMBRE
        defaultPadreShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPadresByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where nombre is not null
        defaultPadreShouldBeFound("nombre.specified=true");

        // Get all the padreList where nombre is null
        defaultPadreShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPadresByNombreContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where nombre contains DEFAULT_NOMBRE
        defaultPadreShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the padreList where nombre contains UPDATED_NOMBRE
        defaultPadreShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPadresByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where nombre does not contain DEFAULT_NOMBRE
        defaultPadreShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the padreList where nombre does not contain UPDATED_NOMBRE
        defaultPadreShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPadresByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where email equals to DEFAULT_EMAIL
        defaultPadreShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the padreList where email equals to UPDATED_EMAIL
        defaultPadreShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPadresByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPadreShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the padreList where email equals to UPDATED_EMAIL
        defaultPadreShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPadresByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where email is not null
        defaultPadreShouldBeFound("email.specified=true");

        // Get all the padreList where email is null
        defaultPadreShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPadresByEmailContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where email contains DEFAULT_EMAIL
        defaultPadreShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the padreList where email contains UPDATED_EMAIL
        defaultPadreShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPadresByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where email does not contain DEFAULT_EMAIL
        defaultPadreShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the padreList where email does not contain UPDATED_EMAIL
        defaultPadreShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPadresByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where telefono equals to DEFAULT_TELEFONO
        defaultPadreShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the padreList where telefono equals to UPDATED_TELEFONO
        defaultPadreShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPadresByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultPadreShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the padreList where telefono equals to UPDATED_TELEFONO
        defaultPadreShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPadresByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where telefono is not null
        defaultPadreShouldBeFound("telefono.specified=true");

        // Get all the padreList where telefono is null
        defaultPadreShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllPadresByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where telefono contains DEFAULT_TELEFONO
        defaultPadreShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the padreList where telefono contains UPDATED_TELEFONO
        defaultPadreShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPadresByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where telefono does not contain DEFAULT_TELEFONO
        defaultPadreShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the padreList where telefono does not contain UPDATED_TELEFONO
        defaultPadreShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPadresByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultPadreShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the padreList where observaciones equals to UPDATED_OBSERVACIONES
        defaultPadreShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPadresByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultPadreShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the padreList where observaciones equals to UPDATED_OBSERVACIONES
        defaultPadreShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPadresByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where observaciones is not null
        defaultPadreShouldBeFound("observaciones.specified=true");

        // Get all the padreList where observaciones is null
        defaultPadreShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllPadresByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where observaciones contains DEFAULT_OBSERVACIONES
        defaultPadreShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the padreList where observaciones contains UPDATED_OBSERVACIONES
        defaultPadreShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPadresByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        // Get all the padreList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultPadreShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the padreList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultPadreShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPadresByEstudiantesIsEqualToSomething() throws Exception {
        Estudiante estudiantes;
        if (TestUtil.findAll(em, Estudiante.class).isEmpty()) {
            padreRepository.saveAndFlush(padre);
            estudiantes = EstudianteResourceIT.createEntity(em);
        } else {
            estudiantes = TestUtil.findAll(em, Estudiante.class).get(0);
        }
        em.persist(estudiantes);
        em.flush();
        padre.addEstudiantes(estudiantes);
        padreRepository.saveAndFlush(padre);
        Long estudiantesId = estudiantes.getId();

        // Get all the padreList where estudiantes equals to estudiantesId
        defaultPadreShouldBeFound("estudiantesId.equals=" + estudiantesId);

        // Get all the padreList where estudiantes equals to (estudiantesId + 1)
        defaultPadreShouldNotBeFound("estudiantesId.equals=" + (estudiantesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPadreShouldBeFound(String filter) throws Exception {
        restPadreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(padre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restPadreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPadreShouldNotBeFound(String filter) throws Exception {
        restPadreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPadreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPadre() throws Exception {
        // Get the padre
        restPadreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPadre() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        int databaseSizeBeforeUpdate = padreRepository.findAll().size();

        // Update the padre
        Padre updatedPadre = padreRepository.findById(padre.getId()).get();
        // Disconnect from session so that the updates on updatedPadre are not directly saved in db
        em.detach(updatedPadre);
        updatedPadre.nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO).observaciones(UPDATED_OBSERVACIONES);

        restPadreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPadre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPadre))
            )
            .andExpect(status().isOk());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
        Padre testPadre = padreList.get(padreList.size() - 1);
        assertThat(testPadre.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPadre.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPadre.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPadre.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingPadre() throws Exception {
        int databaseSizeBeforeUpdate = padreRepository.findAll().size();
        padre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPadreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, padre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(padre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPadre() throws Exception {
        int databaseSizeBeforeUpdate = padreRepository.findAll().size();
        padre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(padre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPadre() throws Exception {
        int databaseSizeBeforeUpdate = padreRepository.findAll().size();
        padre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(padre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePadreWithPatch() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        int databaseSizeBeforeUpdate = padreRepository.findAll().size();

        // Update the padre using partial update
        Padre partialUpdatedPadre = new Padre();
        partialUpdatedPadre.setId(padre.getId());

        partialUpdatedPadre.nombre(UPDATED_NOMBRE).telefono(UPDATED_TELEFONO).observaciones(UPDATED_OBSERVACIONES);

        restPadreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPadre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPadre))
            )
            .andExpect(status().isOk());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
        Padre testPadre = padreList.get(padreList.size() - 1);
        assertThat(testPadre.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPadre.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPadre.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPadre.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdatePadreWithPatch() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        int databaseSizeBeforeUpdate = padreRepository.findAll().size();

        // Update the padre using partial update
        Padre partialUpdatedPadre = new Padre();
        partialUpdatedPadre.setId(padre.getId());

        partialUpdatedPadre.nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO).observaciones(UPDATED_OBSERVACIONES);

        restPadreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPadre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPadre))
            )
            .andExpect(status().isOk());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
        Padre testPadre = padreList.get(padreList.size() - 1);
        assertThat(testPadre.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPadre.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPadre.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPadre.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingPadre() throws Exception {
        int databaseSizeBeforeUpdate = padreRepository.findAll().size();
        padre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPadreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, padre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(padre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPadre() throws Exception {
        int databaseSizeBeforeUpdate = padreRepository.findAll().size();
        padre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(padre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPadre() throws Exception {
        int databaseSizeBeforeUpdate = padreRepository.findAll().size();
        padre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPadreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(padre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Padre in the database
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePadre() throws Exception {
        // Initialize the database
        padreRepository.saveAndFlush(padre);

        int databaseSizeBeforeDelete = padreRepository.findAll().size();

        // Delete the padre
        restPadreMockMvc
            .perform(delete(ENTITY_API_URL_ID, padre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Padre> padreList = padreRepository.findAll();
        assertThat(padreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
