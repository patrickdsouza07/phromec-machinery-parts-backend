package com.phromec.machinery.model;

import com.phromec.machinery.model.part.PartVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "pricing_configurations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_pricing_configuration_variant",
                columnNames = "variant_id"
        )
)
public class PricingConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "configuration_id")
    private Integer configurationId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false, unique = true)
    private PartVariant variant;

    @Column(name = "base_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "material_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal materialCost;

    @Column(name = "labour_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal labourCost;

    @Column(name = "machining_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal machiningCost;

    @Column(name = "other_charges", nullable = false, precision = 15, scale = 2)
    private BigDecimal otherCharges;

    @Column(name = "discount_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "profit_margin_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal profitMarginPercent;

    @Column(name = "tax_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercent;

    @Column(name = "total_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "final_selling_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal finalSellingPrice;

    @Column(nullable = false, length = 10)
    private String currency = "INR";

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }
}
