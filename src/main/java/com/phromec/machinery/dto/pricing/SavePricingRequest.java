package com.phromec.machinery.dto.pricing;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SavePricingRequest {

    @NotNull
    private Integer variantId;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal basePrice;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal materialCost = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal labourCost = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal machiningCost = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal otherCharges = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal profitMarginPercent = new BigDecimal("18.00");

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal taxPercent = new BigDecimal("18.00");
}
