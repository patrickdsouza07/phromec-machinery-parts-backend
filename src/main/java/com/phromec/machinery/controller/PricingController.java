package com.phromec.machinery.controller;

import com.phromec.machinery.dto.pricing.PricingPartResponse;
import com.phromec.machinery.dto.pricing.PricingResponse;
import com.phromec.machinery.dto.pricing.SavePricingRequest;
import com.phromec.machinery.service.PricingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    // API 1: populate the dropdown
    @GetMapping("/parts")
    public ResponseEntity<List<PricingPartResponse>> getPricingParts() {
        return ResponseEntity.ok(pricingService.getPricingParts());
    }

    // Load pricing for the selected variant
    @GetMapping("/parts/{variantId}")
    public ResponseEntity<PricingResponse> getPricingByVariant(
            @PathVariable Integer variantId) {
        return ResponseEntity.ok(
                pricingService.getPricingByVariant(variantId)
        );
    }

    // API 2: calculate and save pricing
    @PostMapping
    public ResponseEntity<PricingResponse> savePricing(
            @Valid @RequestBody SavePricingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pricingService.savePricing(request));
    }
}
