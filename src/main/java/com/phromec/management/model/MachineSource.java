package com.phromec.management.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "machine_sources")
public class MachineSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private Integer sourceId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "power_id", nullable = false)
    private MachinePower power;
    @Column(name = "source_name", nullable = false, length = 100)
    private String sourceName;
    @Column(name = "source_model", length = 150)
    private String sourceModel;
    @Column(precision = 15, scale = 2)
    private BigDecimal price;
    @Column(length = 10)
    private String currency = "INR";
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private RecordStatus status = RecordStatus.ACTIVE;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "source")
    private List<MachineHead> heads = new ArrayList<>();

    public MachineSource() {
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer v) {
        sourceId = v;
    }

    public MachinePower getPower() {
        return power;
    }

    public void setPower(MachinePower v) {
        power = v;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String v) {
        sourceName = v;
    }

    public String getSourceModel() {
        return sourceModel;
    }

    public void setSourceModel(String v) {
        sourceModel = v;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal v) {
        price = v;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String v) {
        currency = v;
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

    public List<MachineHead> getHeads() {
        return heads;
    }

    public void setHeads(List<MachineHead> v) {
        heads = v;
    }
}
