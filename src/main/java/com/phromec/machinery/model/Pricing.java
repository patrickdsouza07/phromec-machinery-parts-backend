package com.phromec.machinery.model;

import com.phromec.machinery.model.part.PartVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.*;

@Setter
@Getter
@Entity
@Table(name = "pricing")
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pricing_id")
    private Integer pricingId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private PartVariant variant;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;
    @Column(length = 10)
    private String currency = "INR";
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;
    @Column(name = "effective_to")
    private LocalDate effectiveTo;
    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", length = 8)
    private PriceType priceType = PriceType.SELLING;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Pricing() {
    }

}
