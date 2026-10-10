package com.phromec.machinery.dto.pricing;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PricingPartResponse {
    private Integer partId;
    private Integer variantId;
    private String partCode;
    private String partName;
    private String variantCode;
    private String unit;
    private BigDecimal currentSellingPrice;
}
