package com.phromec.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "part_variants",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_variant_code",
                        columnNames = "variant_code"
                )
        }
)
public class PartVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private Integer variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "variant_code", nullable = false, unique = true, length = 60)
    private String variantCode;

    @Column(name = "variant_name", nullable = false, length = 200)
    private String variantName;

    @Column(name = "head_type", length = 100)
    private String headType;

    @Column(name = "weight", precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(name = "weight_unit", length = 20)
    private String weightUnit = "KG";

    @Column(name = "dimensions", length = 150)
    private String dimensions;

    @Column(
            name = "quantity_per_machine",
            precision = 10,
            scale = 2
    )
    private BigDecimal quantityPerMachine = BigDecimal.ONE;

    @Column(
            name = "reorder_level",
            precision = 10,
            scale = 2
    )
    private BigDecimal reorderLevel = BigDecimal.ZERO;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Convert(converter = PartStatusConverter.class)
    @Column(name = "status", length = 20)
    private PartStatus status = PartStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "variant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PartVariantMaterial> materials = new ArrayList<>();

    @OneToMany(
            mappedBy = "variant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PartSpecification> specifications = new ArrayList<>();

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = PartStatus.ACTIVE;
        }

        if (weightUnit == null) {
            weightUnit = "KG";
        }

        if (quantityPerMachine == null) {
            quantityPerMachine = BigDecimal.ONE;
        }

        if (reorderLevel == null) {
            reorderLevel = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}