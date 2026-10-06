package com.phromec.machinery.model.machine;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "machine_models",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_model_code", columnNames = "model_code")
        }
)
public class MachineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_model_id")
    private Integer machineModelId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_type_id", nullable = false)
    private MachineType machineType;

    @Column(name = "model_code", nullable = false, unique = true, length = 50)
    private String modelCode;

    @Column(name = "model_name", nullable = false, length = 150)
    private String modelName;

    @Column(name = "brand_name", length = 100)
    private String brandName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", length = 20)
    private String status = "Active";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "Active";
        }
    }
}