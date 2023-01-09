package com.rts.gestor.academia.web.rest;

import static com.rts.gestor.academia.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rts.gestor.academia.IntegrationTest;
import com.rts.gestor.academia.domain.Factura;
import com.rts.gestor.academia.domain.Pago;
import com.rts.gestor.academia.domain.enumeration.MetodoPago;
import com.rts.gestor.academia.repository.PagoRepository;
import com.rts.gestor.academia.service.criteria.PagoCriteria;
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
 * Integration tests for the {@link PagoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PagoResourceIT {

    private static final BigDecimal DEFAULT_CANTIDAD = new BigDecimal(1);
    private static final BigDecimal UPDATED_CANTIDAD = new BigDecimal(2);
    private static final BigDecimal SMALLER_CANTIDAD = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_FECHA_PAGO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_PAGO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final MetodoPago DEFAULT_METODO_PAGO = MetodoPago.EFECTIVO;
    private static final MetodoPago UPDATED_METODO_PAGO = MetodoPago.TARJETA;

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pagos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPagoMockMvc;

    private Pago pago;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createEntity(EntityManager em) {
        Pago pago = new Pago()
            .cantidad(DEFAULT_CANTIDAD)
            .fechaPago(DEFAULT_FECHA_PAGO)
            .metodoPago(DEFAULT_METODO_PAGO)
            .observaciones(DEFAULT_OBSERVACIONES);
        return pago;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createUpdatedEntity(EntityManager em) {
        Pago pago = new Pago()
            .cantidad(UPDATED_CANTIDAD)
            .fechaPago(UPDATED_FECHA_PAGO)
            .metodoPago(UPDATED_METODO_PAGO)
            .observaciones(UPDATED_OBSERVACIONES);
        return pago;
    }

    @BeforeEach
    public void initTest() {
        pago = createEntity(em);
    }

    @Test
    @Transactional
    void createPago() throws Exception {
        int databaseSizeBeforeCreate = pagoRepository.findAll().size();
        // Create the Pago
        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isCreated());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate + 1);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getCantidad()).isEqualByComparingTo(DEFAULT_CANTIDAD);
        assertThat(testPago.getFechaPago()).isEqualTo(DEFAULT_FECHA_PAGO);
        assertThat(testPago.getMetodoPago()).isEqualTo(DEFAULT_METODO_PAGO);
        assertThat(testPago.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createPagoWithExistingId() throws Exception {
        // Create the Pago with an existing ID
        pago.setId(1L);

        int databaseSizeBeforeCreate = pagoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setCantidad(null);

        // Create the Pago, which fails.

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setFechaPago(null);

        // Create the Pago, which fails.

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetodoPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setMetodoPago(null);

        // Create the Pago, which fails.

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPagos() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pago.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(sameNumber(DEFAULT_CANTIDAD))))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].metodoPago").value(hasItem(DEFAULT_METODO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getPago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get the pago
        restPagoMockMvc
            .perform(get(ENTITY_API_URL_ID, pago.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pago.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(sameNumber(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.fechaPago").value(DEFAULT_FECHA_PAGO.toString()))
            .andExpect(jsonPath("$.metodoPago").value(DEFAULT_METODO_PAGO.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getPagosByIdFiltering() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        Long id = pago.getId();

        defaultPagoShouldBeFound("id.equals=" + id);
        defaultPagoShouldNotBeFound("id.notEquals=" + id);

        defaultPagoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPagoShouldNotBeFound("id.greaterThan=" + id);

        defaultPagoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPagoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where cantidad equals to DEFAULT_CANTIDAD
        defaultPagoShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the pagoList where cantidad equals to UPDATED_CANTIDAD
        defaultPagoShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultPagoShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the pagoList where cantidad equals to UPDATED_CANTIDAD
        defaultPagoShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where cantidad is not null
        defaultPagoShouldBeFound("cantidad.specified=true");

        // Get all the pagoList where cantidad is null
        defaultPagoShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultPagoShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the pagoList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultPagoShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultPagoShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the pagoList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultPagoShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where cantidad is less than DEFAULT_CANTIDAD
        defaultPagoShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the pagoList where cantidad is less than UPDATED_CANTIDAD
        defaultPagoShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPagosByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where cantidad is greater than DEFAULT_CANTIDAD
        defaultPagoShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the pagoList where cantidad is greater than SMALLER_CANTIDAD
        defaultPagoShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago equals to DEFAULT_FECHA_PAGO
        defaultPagoShouldBeFound("fechaPago.equals=" + DEFAULT_FECHA_PAGO);

        // Get all the pagoList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultPagoShouldNotBeFound("fechaPago.equals=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsInShouldWork() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago in DEFAULT_FECHA_PAGO or UPDATED_FECHA_PAGO
        defaultPagoShouldBeFound("fechaPago.in=" + DEFAULT_FECHA_PAGO + "," + UPDATED_FECHA_PAGO);

        // Get all the pagoList where fechaPago equals to UPDATED_FECHA_PAGO
        defaultPagoShouldNotBeFound("fechaPago.in=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago is not null
        defaultPagoShouldBeFound("fechaPago.specified=true");

        // Get all the pagoList where fechaPago is null
        defaultPagoShouldNotBeFound("fechaPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByMetodoPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where metodoPago equals to DEFAULT_METODO_PAGO
        defaultPagoShouldBeFound("metodoPago.equals=" + DEFAULT_METODO_PAGO);

        // Get all the pagoList where metodoPago equals to UPDATED_METODO_PAGO
        defaultPagoShouldNotBeFound("metodoPago.equals=" + UPDATED_METODO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMetodoPagoIsInShouldWork() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where metodoPago in DEFAULT_METODO_PAGO or UPDATED_METODO_PAGO
        defaultPagoShouldBeFound("metodoPago.in=" + DEFAULT_METODO_PAGO + "," + UPDATED_METODO_PAGO);

        // Get all the pagoList where metodoPago equals to UPDATED_METODO_PAGO
        defaultPagoShouldNotBeFound("metodoPago.in=" + UPDATED_METODO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMetodoPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where metodoPago is not null
        defaultPagoShouldBeFound("metodoPago.specified=true");

        // Get all the pagoList where metodoPago is null
        defaultPagoShouldNotBeFound("metodoPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultPagoShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the pagoList where observaciones equals to UPDATED_OBSERVACIONES
        defaultPagoShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPagosByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultPagoShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the pagoList where observaciones equals to UPDATED_OBSERVACIONES
        defaultPagoShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPagosByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where observaciones is not null
        defaultPagoShouldBeFound("observaciones.specified=true");

        // Get all the pagoList where observaciones is null
        defaultPagoShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where observaciones contains DEFAULT_OBSERVACIONES
        defaultPagoShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the pagoList where observaciones contains UPDATED_OBSERVACIONES
        defaultPagoShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPagosByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultPagoShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the pagoList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultPagoShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPagosByFacturaIsEqualToSomething() throws Exception {
        Factura factura;
        if (TestUtil.findAll(em, Factura.class).isEmpty()) {
            pagoRepository.saveAndFlush(pago);
            factura = FacturaResourceIT.createEntity(em);
        } else {
            factura = TestUtil.findAll(em, Factura.class).get(0);
        }
        em.persist(factura);
        em.flush();
        pago.addFactura(factura);
        pagoRepository.saveAndFlush(pago);
        Long facturaId = factura.getId();

        // Get all the pagoList where factura equals to facturaId
        defaultPagoShouldBeFound("facturaId.equals=" + facturaId);

        // Get all the pagoList where factura equals to (facturaId + 1)
        defaultPagoShouldNotBeFound("facturaId.equals=" + (facturaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPagoShouldBeFound(String filter) throws Exception {
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pago.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(sameNumber(DEFAULT_CANTIDAD))))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].metodoPago").value(hasItem(DEFAULT_METODO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPagoShouldNotBeFound(String filter) throws Exception {
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPago() throws Exception {
        // Get the pago
        restPagoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago
        Pago updatedPago = pagoRepository.findById(pago.getId()).get();
        // Disconnect from session so that the updates on updatedPago are not directly saved in db
        em.detach(updatedPago);
        updatedPago
            .cantidad(UPDATED_CANTIDAD)
            .fechaPago(UPDATED_FECHA_PAGO)
            .metodoPago(UPDATED_METODO_PAGO)
            .observaciones(UPDATED_OBSERVACIONES);

        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPago.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getCantidad()).isEqualByComparingTo(UPDATED_CANTIDAD);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPago.getMetodoPago()).isEqualTo(UPDATED_METODO_PAGO);
        assertThat(testPago.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pago.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago.cantidad(UPDATED_CANTIDAD).fechaPago(UPDATED_FECHA_PAGO);

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getCantidad()).isEqualByComparingTo(UPDATED_CANTIDAD);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPago.getMetodoPago()).isEqualTo(DEFAULT_METODO_PAGO);
        assertThat(testPago.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago
            .cantidad(UPDATED_CANTIDAD)
            .fechaPago(UPDATED_FECHA_PAGO)
            .metodoPago(UPDATED_METODO_PAGO)
            .observaciones(UPDATED_OBSERVACIONES);

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getCantidad()).isEqualByComparingTo(UPDATED_CANTIDAD);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
        assertThat(testPago.getMetodoPago()).isEqualTo(UPDATED_METODO_PAGO);
        assertThat(testPago.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pago))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeDelete = pagoRepository.findAll().size();

        // Delete the pago
        restPagoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pago.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
