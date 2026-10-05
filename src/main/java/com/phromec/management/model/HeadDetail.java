package com.phromec.management.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "head_details")
public class HeadDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "head_detail_id")
    private Integer headDetailId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "head_id", nullable = false, unique = true)
    private MachineHead head;

    @Column(length = 100)
    private String drive;
    @Column(name = "cl_value", precision = 10, scale = 2)
    private BigDecimal clValue;
    @Column(name = "fl_value", precision = 10, scale = 2)
    private BigDecimal flValue;
    @Column(name = "power_kw", precision = 10, scale = 2)
    private BigDecimal powerKw;
    @Column(name = "focus_value", precision = 10, scale = 2)
    private BigDecimal focusValue;
    @Column(length = 100)
    private String card;
    @Column(name = "tube_sheet_cutting", length = 150)
    private String tubeSheetCutting;
    @Column(name = "weight_kg", precision = 10, scale = 3)
    private BigDecimal weightKg;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public HeadDetail() {
    }

    public Integer getHeadDetailId() {
        return headDetailId;
    }

    public void setHeadDetailId(Integer v) {
        headDetailId = v;
    }

    public MachineHead getHead() {
        return head;
    }

    public void setHead(MachineHead v) {
        head = v;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String v) {
        drive = v;
    }

    public BigDecimal getClValue() {
        return clValue;
    }

    public void setClValue(BigDecimal v) {
        clValue = v;
    }

    public BigDecimal getFlValue() {
        return flValue;
    }

    public void setFlValue(BigDecimal v) {
        flValue = v;
    }

    public BigDecimal getPowerKw() {
        return powerKw;
    }

    public void setPowerKw(BigDecimal v) {
        powerKw = v;
    }

    public BigDecimal getFocusValue() {
        return focusValue;
    }

    public void setFocusValue(BigDecimal v) {
        focusValue = v;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String v) {
        card = v;
    }

    public String getTubeSheetCutting() {
        return tubeSheetCutting;
    }

    public void setTubeSheetCutting(String v) {
        tubeSheetCutting = v;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal v) {
        weightKg = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        createdAt = v;
    }
}
