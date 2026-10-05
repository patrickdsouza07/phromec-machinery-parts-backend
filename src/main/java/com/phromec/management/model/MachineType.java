package com.phromec.management.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "machine_types")
public class MachineType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_type_id")
    private Long machineTypeId;

    @Column(name = "type_code", nullable = false,
            unique = true, length = 30)
    private String typeCode;

    @Column(name = "type_name", nullable = false,
            unique = true, length = 100)
    private String typeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", length = 20)
    private String status = "Active";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public MachineType() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}