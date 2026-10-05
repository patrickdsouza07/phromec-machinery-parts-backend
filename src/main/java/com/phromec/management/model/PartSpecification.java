package com.phromec.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "part_specifications")
public class PartSpecification {

    @EmbeddedId
    private PartSpecificationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("variantId")
    @JoinColumn(name = "variant_id", nullable = false)
    private PartVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("specificationId")
    @JoinColumn(name = "specification_id", nullable = false)
    private Specification specification;

    @Column(name = "specification_value", length = 255)
    private String specificationValue;
}