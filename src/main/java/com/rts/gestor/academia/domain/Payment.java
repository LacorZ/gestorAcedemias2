package com.rts.gestor.academia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "paid_at", nullable = false)
    private Instant paidAt;

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "students", "payment" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPaidAt() {
        return this.paidAt;
    }

    public Payment paidAt(Instant paidAt) {
        this.setPaidAt(paidAt);
        return this;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setPayment(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setPayment(this));
        }
        this.invoices = invoices;
    }

    public Payment invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Payment addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setPayment(this);
        return this;
    }

    public Payment removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setPayment(null);
        return this;
    }

    public Set<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }

    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        if (this.paymentMethods != null) {
            this.paymentMethods.forEach(i -> i.setPayment(null));
        }
        if (paymentMethods != null) {
            paymentMethods.forEach(i -> i.setPayment(this));
        }
        this.paymentMethods = paymentMethods;
    }

    public Payment paymentMethods(Set<PaymentMethod> paymentMethods) {
        this.setPaymentMethods(paymentMethods);
        return this;
    }

    public Payment addPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.add(paymentMethod);
        paymentMethod.setPayment(this);
        return this;
    }

    public Payment removePaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.remove(paymentMethod);
        paymentMethod.setPayment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", paidAt='" + getPaidAt() + "'" +
            "}";
    }
}
