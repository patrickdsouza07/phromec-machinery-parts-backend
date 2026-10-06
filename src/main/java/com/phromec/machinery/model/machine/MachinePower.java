package com.phromec.machinery.model.machine;

import java.math.BigDecimal;
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
        name = "machine_powers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_machine_power",
                        columnNames = {"machine_type_id", "power_kw"}
                )
        }
)
public class MachinePower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "power_id")
    private Integer powerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_type_id", nullable = false)
    private MachineType machineType;

    @Column(name = "power_kw", nullable = false, precision = 10, scale = 2)
    private BigDecimal powerKw;

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