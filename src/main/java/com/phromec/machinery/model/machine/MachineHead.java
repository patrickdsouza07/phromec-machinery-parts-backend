package com.phromec.machinery.model.machine;

import com.phromec.machinery.model.HeadDetail;
import com.phromec.machinery.model.RecordStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "machine_heads",
        uniqueConstraints = @UniqueConstraint(name = "uq_head", columnNames = {"source_id", "head_model"})
)
public class MachineHead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "head_id")
    private Integer headId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private MachineSource source;

    @Column(name = "head_brand", nullable = false, length = 100)
    private String headBrand;

    @Column(name = "head_model", nullable = false, length = 150)
    private String headModel;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @Column(length = 10)
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private RecordStatus status = RecordStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "head", fetch = FetchType.LAZY)
    private HeadDetail detail;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (currency == null) {
            currency = "INR";
        }
        if (status == null) {
            status = RecordStatus.ACTIVE;
        }
    }
}