package com.rts.gestor.academia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Asistencia;
import com.rts.gestor.academia.domain.enumeration.AsistenciaEstado;
import com.rts.gestor.academia.repository.AsistenciaRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AsistenciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AsistenciaResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final AsistenciaEstado DEFAULT_ESTADO = AsistenciaEstado.PRESENTE;
    private static final AsistenciaEstado UPDATED_ESTADO = AsistenciaEstado.AUSENTE;

    private static final Instant DEFAULT_HORA_ENTRADA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_ENTRADA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HORA_SALIDA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_SALIDA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/asistencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAsistenciaMockMvc;

    private Asistencia asistencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asistencia createEntity(EntityManager em) {
        Asistencia asistencia = new Asistencia()
            .fecha(DEFAULT_FECHA)
            .estado(DEFAULT_ESTADO)
            .horaEntrada(DEFAULT_HORA_ENTRADA)
            .horaSalida(DEFAULT_HORA_SALIDA)
            .observaciones(DEFAULT_OBSERVACIONES);
        return asistencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asistencia createUpdatedEntity(EntityManager em) {
        Asistencia asistencia = new Asistencia()
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .horaEntrada(UPDATED_HORA_ENTRADA)
            .horaSalida(UPDATED_HORA_SALIDA)
            .observaciones(UPDATED_OBSERVACIONES);
        return asistencia;
    }

    @BeforeEach
    public void initTest() {
        asistencia = createEntity(em);
    }

    @Test
    @Transactional
    void createAsistencia() throws Exception {
        int databaseSizeBeforeCreate = asistenciaRepository.findAll().size();
        // Create the Asistencia
        restAsistenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asistencia)))
            .andExpect(status().isCreated());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Asistencia testAsistencia = asistenciaList.get(asistenciaList.size() - 1);
        assertThat(testAsistencia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testAsistencia.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testAsistencia.getHoraEntrada()).isEqualTo(DEFAULT_HORA_ENTRADA);
        assertThat(testAsistencia.getHoraSalida()).isEqualTo(DEFAULT_HORA_SALIDA);
        assertThat(testAsistencia.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createAsistenciaWithExistingId() throws Exception {
        // Create the Asistencia with an existing ID
        asistencia.setId(1L);

        int databaseSizeBeforeCreate = asistenciaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAsistenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asistencia)))
            .andExpect(status().isBadRequest());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = asistenciaRepository.findAll().size();
        // set the field null
        asistencia.setFecha(null);

        // Create the Asistencia, which fails.

        restAsistenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asistencia)))
            .andExpect(status().isBadRequest());

        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = asistenciaRepository.findAll().size();
        // set the field null
        asistencia.setEstado(null);

        // Create the Asistencia, which fails.

        restAsistenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asistencia)))
            .andExpect(status().isBadRequest());

        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAsistencias() throws Exception {
        // Initialize the database
        asistenciaRepository.saveAndFlush(asistencia);

        // Get all the asistenciaList
        restAsistenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asistencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].horaEntrada").value(hasItem(DEFAULT_HORA_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].horaSalida").value(hasItem(DEFAULT_HORA_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getAsistencia() throws Exception {
        // Initialize the database
        asistenciaRepository.saveAndFlush(asistencia);

        // Get the asistencia
        restAsistenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, asistencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asistencia.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.horaEntrada").value(DEFAULT_HORA_ENTRADA.toString()))
            .andExpect(jsonPath("$.horaSalida").value(DEFAULT_HORA_SALIDA.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getNonExistingAsistencia() throws Exception {
        // Get the asistencia
        restAsistenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAsistencia() throws Exception {
        // Initialize the database
        asistenciaRepository.saveAndFlush(asistencia);

        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();

        // Update the asistencia
        Asistencia updatedAsistencia = asistenciaRepository.findById(asistencia.getId()).get();
        // Disconnect from session so that the updates on updatedAsistencia are not directly saved in db
        em.detach(updatedAsistencia);
        updatedAsistencia
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .horaEntrada(UPDATED_HORA_ENTRADA)
            .horaSalida(UPDATED_HORA_SALIDA)
            .observaciones(UPDATED_OBSERVACIONES);

        restAsistenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAsistencia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAsistencia))
            )
            .andExpect(status().isOk());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
        Asistencia testAsistencia = asistenciaList.get(asistenciaList.size() - 1);
        assertThat(testAsistencia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testAsistencia.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testAsistencia.getHoraEntrada()).isEqualTo(UPDATED_HORA_ENTRADA);
        assertThat(testAsistencia.getHoraSalida()).isEqualTo(UPDATED_HORA_SALIDA);
        assertThat(testAsistencia.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingAsistencia() throws Exception {
        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();
        asistencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsistenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, asistencia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(asistencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAsistencia() throws Exception {
        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();
        asistencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsistenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(asistencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAsistencia() throws Exception {
        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();
        asistencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsistenciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asistencia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAsistenciaWithPatch() throws Exception {
        // Initialize the database
        asistenciaRepository.saveAndFlush(asistencia);

        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();

        // Update the asistencia using partial update
        Asistencia partialUpdatedAsistencia = new Asistencia();
        partialUpdatedAsistencia.setId(asistencia.getId());

        partialUpdatedAsistencia.observaciones(UPDATED_OBSERVACIONES);

        restAsistenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsistencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsistencia))
            )
            .andExpect(status().isOk());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
        Asistencia testAsistencia = asistenciaList.get(asistenciaList.size() - 1);
        assertThat(testAsistencia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testAsistencia.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testAsistencia.getHoraEntrada()).isEqualTo(DEFAULT_HORA_ENTRADA);
        assertThat(testAsistencia.getHoraSalida()).isEqualTo(DEFAULT_HORA_SALIDA);
        assertThat(testAsistencia.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateAsistenciaWithPatch() throws Exception {
        // Initialize the database
        asistenciaRepository.saveAndFlush(asistencia);

        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();

        // Update the asistencia using partial update
        Asistencia partialUpdatedAsistencia = new Asistencia();
        partialUpdatedAsistencia.setId(asistencia.getId());

        partialUpdatedAsistencia
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .horaEntrada(UPDATED_HORA_ENTRADA)
            .horaSalida(UPDATED_HORA_SALIDA)
            .observaciones(UPDATED_OBSERVACIONES);

        restAsistenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsistencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsistencia))
            )
            .andExpect(status().isOk());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
        Asistencia testAsistencia = asistenciaList.get(asistenciaList.size() - 1);
        assertThat(testAsistencia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testAsistencia.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testAsistencia.getHoraEntrada()).isEqualTo(UPDATED_HORA_ENTRADA);
        assertThat(testAsistencia.getHoraSalida()).isEqualTo(UPDATED_HORA_SALIDA);
        assertThat(testAsistencia.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingAsistencia() throws Exception {
        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();
        asistencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsistenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, asistencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(asistencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAsistencia() throws Exception {
        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();
        asistencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsistenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(asistencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAsistencia() throws Exception {
        int databaseSizeBeforeUpdate = asistenciaRepository.findAll().size();
        asistencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsistenciaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(asistencia))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asistencia in the database
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAsistencia() throws Exception {
        // Initialize the database
        asistenciaRepository.saveAndFlush(asistencia);

        int databaseSizeBeforeDelete = asistenciaRepository.findAll().size();

        // Delete the asistencia
        restAsistenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, asistencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Asistencia> asistenciaList = asistenciaRepository.findAll();
        assertThat(asistenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
