package com.phromec.machinery.model;

import com.phromec.machinery.model.machine.MachineHead;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
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

}
