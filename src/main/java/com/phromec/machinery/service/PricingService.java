package com.phromec.machinery.service;

import com.phromec.machinery.dto.pricing.PricingPartResponse;
import com.phromec.machinery.dto.pricing.PricingResponse;
import com.phromec.machinery.dto.pricing.SavePricingRequest;

import java.util.List;

public interface PricingService {

    List<PricingPartResponse> getPricingParts();

    PricingResponse getPricingByVariant(Integer variantId);

    PricingResponse savePricing(SavePricingRequest request);
}
