package com.phromec.machinery.model;

import com.phromec.machinery.model.part.PartVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
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

}
