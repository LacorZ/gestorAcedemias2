package com.rts.gestor.academia.web.rest;

import static com.rts.gestor.academia.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Estudiante;
import com.rts.gestor.academia.domain.Factura;
import com.rts.gestor.academia.domain.Pago;
import com.rts.gestor.academia.repository.FacturaRepository;
import com.rts.gestor.academia.service.criteria.FacturaCriteria;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaResourceIT {

    private static final BigDecimal DEFAULT_FACTURADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_FACTURADO = new BigDecimal(2);
    private static final BigDecimal SMALLER_FACTURADO = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_FECHA_FACTURA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FACTURA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaMockMvc;

    private Factura factura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .facturado(DEFAULT_FACTURADO)
            .fechaFactura(DEFAULT_FECHA_FACTURA)
            .observaciones(DEFAULT_OBSERVACIONES);
        return factura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura factura = new Factura()
            .facturado(UPDATED_FACTURADO)
            .fechaFactura(UPDATED_FECHA_FACTURA)
            .observaciones(UPDATED_OBSERVACIONES);
        return factura;
    }

    @BeforeEach
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();
        // Create the Factura
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFacturado()).isEqualByComparingTo(DEFAULT_FACTURADO);
        assertThat(testFactura.getFechaFactura()).isEqualTo(DEFAULT_FECHA_FACTURA);
        assertThat(testFactura.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createFacturaWithExistingId() throws Exception {
        // Create the Factura with an existing ID
        factura.setId(1L);

        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFacturadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setFacturado(null);

        // Create the Factura, which fails.

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFacturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setFechaFactura(null);

        // Create the Factura, which fails.

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].facturado").value(hasItem(sameNumber(DEFAULT_FACTURADO))))
            .andExpect(jsonPath("$.[*].fechaFactura").value(hasItem(DEFAULT_FECHA_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.facturado").value(sameNumber(DEFAULT_FACTURADO)))
            .andExpect(jsonPath("$.fechaFactura").value(DEFAULT_FECHA_FACTURA.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getFacturasByIdFiltering() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        Long id = factura.getId();

        defaultFacturaShouldBeFound("id.equals=" + id);
        defaultFacturaShouldNotBeFound("id.notEquals=" + id);

        defaultFacturaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacturaShouldNotBeFound("id.greaterThan=" + id);

        defaultFacturaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacturaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturadoIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where facturado equals to DEFAULT_FACTURADO
        defaultFacturaShouldBeFound("facturado.equals=" + DEFAULT_FACTURADO);

        // Get all the facturaList where facturado equals to UPDATED_FACTURADO
        defaultFacturaShouldNotBeFound("facturado.equals=" + UPDATED_FACTURADO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturadoIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where facturado in DEFAULT_FACTURADO or UPDATED_FACTURADO
        defaultFacturaShouldBeFound("facturado.in=" + DEFAULT_FACTURADO + "," + UPDATED_FACTURADO);

        // Get all the facturaList where facturado equals to UPDATED_FACTURADO
        defaultFacturaShouldNotBeFound("facturado.in=" + UPDATED_FACTURADO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where facturado is not null
        defaultFacturaShouldBeFound("facturado.specified=true");

        // Get all the facturaList where facturado is null
        defaultFacturaShouldNotBeFound("facturado.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByFacturadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where facturado is greater than or equal to DEFAULT_FACTURADO
        defaultFacturaShouldBeFound("facturado.greaterThanOrEqual=" + DEFAULT_FACTURADO);

        // Get all the facturaList where facturado is greater than or equal to UPDATED_FACTURADO
        defaultFacturaShouldNotBeFound("facturado.greaterThanOrEqual=" + UPDATED_FACTURADO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where facturado is less than or equal to DEFAULT_FACTURADO
        defaultFacturaShouldBeFound("facturado.lessThanOrEqual=" + DEFAULT_FACTURADO);

        // Get all the facturaList where facturado is less than or equal to SMALLER_FACTURADO
        defaultFacturaShouldNotBeFound("facturado.lessThanOrEqual=" + SMALLER_FACTURADO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturadoIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where facturado is less than DEFAULT_FACTURADO
        defaultFacturaShouldNotBeFound("facturado.lessThan=" + DEFAULT_FACTURADO);

        // Get all the facturaList where facturado is less than UPDATED_FACTURADO
        defaultFacturaShouldBeFound("facturado.lessThan=" + UPDATED_FACTURADO);
    }

    @Test
    @Transactional
    void getAllFacturasByFacturadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where facturado is greater than DEFAULT_FACTURADO
        defaultFacturaShouldNotBeFound("facturado.greaterThan=" + DEFAULT_FACTURADO);

        // Get all the facturaList where facturado is greater than SMALLER_FACTURADO
        defaultFacturaShouldBeFound("facturado.greaterThan=" + SMALLER_FACTURADO);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaFacturaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaFactura equals to DEFAULT_FECHA_FACTURA
        defaultFacturaShouldBeFound("fechaFactura.equals=" + DEFAULT_FECHA_FACTURA);

        // Get all the facturaList where fechaFactura equals to UPDATED_FECHA_FACTURA
        defaultFacturaShouldNotBeFound("fechaFactura.equals=" + UPDATED_FECHA_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaFacturaIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaFactura in DEFAULT_FECHA_FACTURA or UPDATED_FECHA_FACTURA
        defaultFacturaShouldBeFound("fechaFactura.in=" + DEFAULT_FECHA_FACTURA + "," + UPDATED_FECHA_FACTURA);

        // Get all the facturaList where fechaFactura equals to UPDATED_FECHA_FACTURA
        defaultFacturaShouldNotBeFound("fechaFactura.in=" + UPDATED_FECHA_FACTURA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaFacturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaFactura is not null
        defaultFacturaShouldBeFound("fechaFactura.specified=true");

        // Get all the facturaList where fechaFactura is null
        defaultFacturaShouldNotBeFound("fechaFactura.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultFacturaShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the facturaList where observaciones equals to UPDATED_OBSERVACIONES
        defaultFacturaShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllFacturasByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultFacturaShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the facturaList where observaciones equals to UPDATED_OBSERVACIONES
        defaultFacturaShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllFacturasByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where observaciones is not null
        defaultFacturaShouldBeFound("observaciones.specified=true");

        // Get all the facturaList where observaciones is null
        defaultFacturaShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where observaciones contains DEFAULT_OBSERVACIONES
        defaultFacturaShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the facturaList where observaciones contains UPDATED_OBSERVACIONES
        defaultFacturaShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllFacturasByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultFacturaShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the facturaList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultFacturaShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllFacturasByEstudianteIsEqualToSomething() throws Exception {
        Estudiante estudiante;
        if (TestUtil.findAll(em, Estudiante.class).isEmpty()) {
            facturaRepository.saveAndFlush(factura);
            estudiante = EstudianteResourceIT.createEntity(em);
        } else {
            estudiante = TestUtil.findAll(em, Estudiante.class).get(0);
        }
        em.persist(estudiante);
        em.flush();
        factura.addEstudiante(estudiante);
        facturaRepository.saveAndFlush(factura);
        Long estudianteId = estudiante.getId();

        // Get all the facturaList where estudiante equals to estudianteId
        defaultFacturaShouldBeFound("estudianteId.equals=" + estudianteId);

        // Get all the facturaList where estudiante equals to (estudianteId + 1)
        defaultFacturaShouldNotBeFound("estudianteId.equals=" + (estudianteId + 1));
    }

    @Test
    @Transactional
    void getAllFacturasByPagosIsEqualToSomething() throws Exception {
        Pago pagos;
        if (TestUtil.findAll(em, Pago.class).isEmpty()) {
            facturaRepository.saveAndFlush(factura);
            pagos = PagoResourceIT.createEntity(em);
        } else {
            pagos = TestUtil.findAll(em, Pago.class).get(0);
        }
        em.persist(pagos);
        em.flush();
        factura.setPagos(pagos);
        facturaRepository.saveAndFlush(factura);
        Long pagosId = pagos.getId();

        // Get all the facturaList where pagos equals to pagosId
        defaultFacturaShouldBeFound("pagosId.equals=" + pagosId);

        // Get all the facturaList where pagos equals to (pagosId + 1)
        defaultFacturaShouldNotBeFound("pagosId.equals=" + (pagosId + 1));
    }

    @Test
    @Transactional
    void getAllFacturasByPagoIsEqualToSomething() throws Exception {
        Pago pago;
        if (TestUtil.findAll(em, Pago.class).isEmpty()) {
            facturaRepository.saveAndFlush(factura);
            pago = PagoResourceIT.createEntity(em);
        } else {
            pago = TestUtil.findAll(em, Pago.class).get(0);
        }
        em.persist(pago);
        em.flush();
        factura.setPago(pago);
        facturaRepository.saveAndFlush(factura);
        Long pagoId = pago.getId();

        // Get all the facturaList where pago equals to pagoId
        defaultFacturaShouldBeFound("pagoId.equals=" + pagoId);

        // Get all the facturaList where pago equals to (pagoId + 1)
        defaultFacturaShouldNotBeFound("pagoId.equals=" + (pagoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacturaShouldBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].facturado").value(hasItem(sameNumber(DEFAULT_FACTURADO))))
            .andExpect(jsonPath("$.[*].fechaFactura").value(hasItem(DEFAULT_FECHA_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacturaShouldNotBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura.facturado(UPDATED_FACTURADO).fechaFactura(UPDATED_FECHA_FACTURA).observaciones(UPDATED_OBSERVACIONES);

        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFacturado()).isEqualByComparingTo(UPDATED_FACTURADO);
        assertThat(testFactura.getFechaFactura()).isEqualTo(UPDATED_FECHA_FACTURA);
        assertThat(testFactura.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura.observaciones(UPDATED_OBSERVACIONES);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFacturado()).isEqualByComparingTo(DEFAULT_FACTURADO);
        assertThat(testFactura.getFechaFactura()).isEqualTo(DEFAULT_FECHA_FACTURA);
        assertThat(testFactura.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura.facturado(UPDATED_FACTURADO).fechaFactura(UPDATED_FECHA_FACTURA).observaciones(UPDATED_OBSERVACIONES);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFacturado()).isEqualByComparingTo(UPDATED_FACTURADO);
        assertThat(testFactura.getFechaFactura()).isEqualTo(UPDATED_FECHA_FACTURA);
        assertThat(testFactura.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Delete the factura
        restFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, factura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
