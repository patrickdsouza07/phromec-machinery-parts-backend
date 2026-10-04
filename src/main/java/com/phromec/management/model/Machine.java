
package com.phromec.management.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    private Long machineId;

    @Column(name = "machine_name", nullable = false, length = 150)
    private String machineName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_type_id")
    private MachineType machineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_model_id")
    private MachineModel machineModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "power_id")
    private MachinePower machinePower;

    @Column(name = "manufacturer", length = 150)
    private String manufacturer;

    @Column(name = "capacity", length = 100)
    private String capacity;

    @Column(name = "parts_count")
    private Integer partsCount;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Machine() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}