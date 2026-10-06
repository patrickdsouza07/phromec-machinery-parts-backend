package com.phromec.machinery.dto.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartResponse {

    private Integer partId;

    // Column: PART NO.
    private String partCode;

    // Column: PART NAME
    private String partName;
    private String leadTime; // e.g. "4-6 weeks lead time"

    // Column: MACHINE
    private Integer machineTypeId;
    private String machineName;     // e.g. "HyperJet 3015"
    private String machineTypeName; // e.g. "Sheet Cutting"

    // Column: CATEGORY
    private String category; // e.g. "Hydraulic", "Cutting", "Feed System", "Optical"

    // Column: MATERIAL
    private String material; // Primary or comma-separated materials

    // Column: UNIT
    private String unitOfMeasure; // e.g. "Set", "Piece"

    // Column: BASE PRICE
    private BigDecimal basePrice; // e.g. 185000.00

    // Column: STOCK
    private String stockStatus;   // "In Stock", "Out of Stock"
    private BigDecimal stockQuantity;

    // Column: STATUS
    private String status; // "Active", "Inactive"

    private String description;
}