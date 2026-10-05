package com.phromec.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class PartVariantMaterialId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "variant_id")
    private Integer variantId;

    @Column(name = "material_id")
    private Integer materialId;
}