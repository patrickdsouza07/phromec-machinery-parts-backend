package com.phromec.machinery.model;

import com.phromec.machinery.model.part.PartVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "brands")
public class Brand {
    // getters/setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;
    @Column(name = "brand_name", nullable = false, unique = true, length = 100)
    private String brandName;
    @Column(length = 255)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private RecordStatus status = RecordStatus.ACTIVE;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "brand")
    private List<PartVariant> variants = new ArrayList<>();

    public Brand() {
    }

}
