package com.rts.gestor.academia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Padre;
import com.rts.gestor.academia.repository.PadreRepository;
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
        when(padreRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPadreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(padreRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPadresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(padreRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

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
