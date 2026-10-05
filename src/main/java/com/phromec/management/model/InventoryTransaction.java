package com.phromec.management.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private PartVariant variant;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 10)
    private TransactionType transactionType;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    @Column(name = "reference_id")
    private Integer referenceId;
    @Column(length = 255)
    private String remarks;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public InventoryTransaction() {
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer v) {
        transactionId = v;
    }

    public PartVariant getVariant() {
        return variant;
    }

    public void setVariant(PartVariant v) {
        variant = v;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType v) {
        transactionType = v;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal v) {
        quantity = v;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String v) {
        referenceType = v;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer v) {
        referenceId = v;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String v) {
        remarks = v;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User v) {
        createdBy = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        createdAt = v;
    }
}
