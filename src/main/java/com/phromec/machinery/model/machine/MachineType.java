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
        name = "machine_types",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_type_code", columnNames = "type_code"),
                @UniqueConstraint(name = "uq_type_name", columnNames = "type_name")
        }
)
public class MachineType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_type_id")
    private Integer machineTypeId;

    @Column(name = "type_code", nullable = false, unique = true, length = 30)
    private String typeCode;

    @Column(name = "type_name", nullable = false, unique = true, length = 100)
    private String typeName;

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