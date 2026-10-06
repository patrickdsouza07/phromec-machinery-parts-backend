package com.phromec.machinery.model;

import com.phromec.machinery.model.order.OrderStatus;
import com.phromec.machinery.model.order.OrderStatusConverter;
import com.phromec.machinery.model.part.PartVariantMaterial;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Integer materialId;

    @Column(name = "material_code", nullable = false, unique = true, length = 30)
    private String materialCode;

    @Column(name = "material_name", nullable = false, unique = true, length = 100)
    private String materialName;

    @Column(length = 255)
    private String description;

    @Column(length = 30)
    private String unit;

    @Convert(converter = RecordStatusConverter.class)
    @Column(name = "status", nullable = false, length = 30)
    private RecordStatus status = RecordStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "material")
    private List<PartVariantMaterial> variantMaterials = new ArrayList<>();

    public Material() {
    }

}
