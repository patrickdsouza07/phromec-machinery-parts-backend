package com.phromec.management.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "brands")
public class Brand {
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

    // getters/setters
    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer v) {
        brandId = v;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String v) {
        brandName = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        description = v;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus v) {
        status = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        createdAt = v;
    }

    public List<PartVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<PartVariant> v) {
        variants = v;
    }
}
