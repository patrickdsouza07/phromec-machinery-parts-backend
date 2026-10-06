package com.phromec.machinery.model.part;

import com.phromec.machinery.model.machine.MachineType;
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
        name = "parts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_part_code",
                        columnNames = "part_code"
                )
        }
)
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Integer partId;

    @Column(name = "part_code", nullable = false, unique = true, length = 50)
    private String partCode;

    @Column(name = "part_name", nullable = false, length = 150)
    private String partName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_type_id")
    private MachineType machineType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "unit_of_measure", length = 30)
    private String unitOfMeasure = "PCS";

    @Convert(converter = PartStatusConverter.class)
    @Column(name = "status", length = 20)
    private PartStatus status = PartStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "part",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PartVariant> variants = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = PartStatus.ACTIVE;
        }

        if (unitOfMeasure == null) {
            unitOfMeasure = "PCS";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addVariant(PartVariant variant) {
        variants.add(variant);
        variant.setPart(this);
    }

    public void removeVariant(PartVariant variant) {
        variants.remove(variant);
        variant.setPart(null);
    }
}