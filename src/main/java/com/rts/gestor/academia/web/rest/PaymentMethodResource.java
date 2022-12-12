package com.rts.gestor.academia.web.rest;

import com.rts.gestor.academia.domain.PaymentMethod;
import com.rts.gestor.academia.repository.PaymentMethodRepository;
import com.rts.gestor.academia.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rts.gestor.academia.domain.PaymentMethod}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodResource.class);

    private static final String ENTITY_NAME = "paymentMethod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodResource(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * {@code POST  /payment-methods} : Create a new paymentMethod.
     *
     * @param paymentMethod the paymentMethod to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentMethod, or with status {@code 400 (Bad Request)} if the paymentMethod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-methods")
    public ResponseEntity<PaymentMethod> createPaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod) throws URISyntaxException {
        log.debug("REST request to save PaymentMethod : {}", paymentMethod);
        if (paymentMethod.getId() != null) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentMethod result = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity
            .created(new URI("/api/payment-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-methods/:id} : Updates an existing paymentMethod.
     *
     * @param id the id of the paymentMethod to save.
     * @param paymentMethod the paymentMethod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMethod,
     * or with status {@code 400 (Bad Request)} if the paymentMethod is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-methods/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentMethod paymentMethod
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentMethod : {}, {}", id, paymentMethod);
        if (paymentMethod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentMethod result = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethod.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-methods/:id} : Partial updates given fields of an existing paymentMethod, field will ignore if it is null
     *
     * @param id the id of the paymentMethod to save.
     * @param paymentMethod the paymentMethod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMethod,
     * or with status {@code 400 (Bad Request)} if the paymentMethod is not valid,
     * or with status {@code 404 (Not Found)} if the paymentMethod is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-methods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentMethod> partialUpdatePaymentMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentMethod paymentMethod
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentMethod partially : {}, {}", id, paymentMethod);
        if (paymentMethod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMethod> result = paymentMethodRepository
            .findById(paymentMethod.getId())
            .map(existingPaymentMethod -> {
                if (paymentMethod.getName() != null) {
                    existingPaymentMethod.setName(paymentMethod.getName());
                }

                return existingPaymentMethod;
            })
            .map(paymentMethodRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethod.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-methods} : get all the paymentMethods.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentMethods in body.
     */
    @GetMapping("/payment-methods")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PaymentMethods");
        Page<PaymentMethod> page = paymentMethodRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-methods/:id} : get the "id" paymentMethod.
     *
     * @param id the id of the paymentMethod to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMethod, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-methods/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethod(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethod : {}", id);
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paymentMethod);
    }

    /**
     * {@code DELETE  /payment-methods/:id} : delete the "id" paymentMethod.
     *
     * @param id the id of the paymentMethod to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-methods/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        log.debug("REST request to delete PaymentMethod : {}", id);
        paymentMethodRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
