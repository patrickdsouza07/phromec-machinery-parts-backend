package com.phromec.machinery.dto.quotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationResponse {

    private Integer quotationId;

    private String quotationNumber;

    private String customerName;

    /*
     * The current Quotation entity does not contain
     * a direct Machine/MachineModel relationship.
     *
     * This field is ready for the UI, but should only
     * be populated after the machine relationship is
     * defined in the domain model.
     */
    private String machineName;

    private LocalDate quotationDate;

    private LocalDate validUntil;

    private BigDecimal totalAmount;

    private String createdBy;

    private String status;
}