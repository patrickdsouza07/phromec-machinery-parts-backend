package com.phromec.machinery.model.part;

import com.phromec.machinery.model.Material;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "part_variant_materials")
public class PartVariantMaterial {

    @EmbeddedId
    private PartVariantMaterialId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("variantId")
    @JoinColumn(name = "variant_id", nullable = false)
    private PartVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("materialId")
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "quantity", precision = 10, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit", length = 20)
    private String unit;
}