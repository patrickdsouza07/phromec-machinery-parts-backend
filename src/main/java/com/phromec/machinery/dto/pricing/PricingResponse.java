package com.phromec.machinery.dto.pricing;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PricingResponse {
    private Integer partId;
    private Integer variantId;
    private String partCode;
    private String partName;
    private String unit;

    private BigDecimal basePrice;
    private BigDecimal materialCost;
    private BigDecimal labourCost;
    private BigDecimal machiningCost;
    private BigDecimal otherCharges;

    private BigDecimal discountPercent;
    private BigDecimal profitMarginPercent;
    private BigDecimal taxPercent;

    private BigDecimal totalCost;
    private BigDecimal discountAmount;
    private BigDecimal profitAmount;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal finalSellingPrice;
    private String currency;
}
