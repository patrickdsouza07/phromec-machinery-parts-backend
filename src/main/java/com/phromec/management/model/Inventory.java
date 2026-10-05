package com.phromec.management.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false, unique = true)
    private PartVariant variant;
    @Column(name = "quantity_in_stock", precision = 12, scale = 2)
    private BigDecimal quantityInStock = BigDecimal.ZERO;
    @Column(name = "reserved_quantity", precision = 12, scale = 2)
    private BigDecimal reservedQuantity = BigDecimal.ZERO;
    @Column(name = "reorder_level", precision = 12, scale = 2)
    private BigDecimal reorderLevel = BigDecimal.ZERO;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public Inventory() {
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer v) {
        inventoryId = v;
    }

    public PartVariant getVariant() {
        return variant;
    }

    public void setVariant(PartVariant v) {
        variant = v;
    }

    public BigDecimal getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(BigDecimal v) {
        quantityInStock = v;
    }

    public BigDecimal getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(BigDecimal v) {
        reservedQuantity = v;
    }

    public BigDecimal getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(BigDecimal v) {
        reorderLevel = v;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime v) {
        lastUpdated = v;
    }
}
