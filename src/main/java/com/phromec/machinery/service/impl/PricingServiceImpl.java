package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.pricing.PricingPartResponse;
import com.phromec.machinery.dto.pricing.PricingResponse;
import com.phromec.machinery.dto.pricing.SavePricingRequest;
import com.phromec.machinery.model.Pricing;
import com.phromec.machinery.model.PricingConfiguration;
import com.phromec.machinery.model.part.PartVariant;
import com.phromec.machinery.repository.PartVariantRepository;
import com.phromec.machinery.repository.PricingConfigurationRepository;
import com.phromec.machinery.repository.PricingRepository;
import com.phromec.machinery.service.PricingService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricingServiceImpl implements PricingService {

    private static final String CURRENCY = "INR";

    private final PartVariantRepository partVariantRepository;
    private final PricingConfigurationRepository configurationRepository;
    private final PricingRepository pricingRepository;

    // ---------------------------------------------------------
    // API 1: Get parts and variants for the dropdown
    // ---------------------------------------------------------

    @Override
    public List<PricingPartResponse> getPricingParts() {

        return partVariantRepository.findAll()
                .stream()
                .filter(variant ->
                        variant.getPart() != null
                                && "Active".equalsIgnoreCase(
                                String.valueOf(variant.getPart().getStatus()))
                                && "Active".equalsIgnoreCase(
                                String.valueOf(variant.getStatus())))
                .map(variant -> {
                    Pricing currentPrice = getCurrentSellingPrice(
                            variant.getVariantId()
                    );

                    return PricingPartResponse.builder()
                            .partId(variant.getPart().getPartId())
                            .variantId(variant.getVariantId())
                            .partCode(variant.getPart().getPartCode())
                            .partName(variant.getPart().getPartName())
                            .variantCode(variant.getVariantCode())
                            .unit(variant.getPart().getUnitOfMeasure())
                            .currentSellingPrice(
                                    currentPrice != null
                                            ? currentPrice.getPrice()
                                            : BigDecimal.ZERO
                            )
                            .build();
                })
                .toList();
    }

    // ---------------------------------------------------------
    // Load the existing pricing configuration for a selection
    // ---------------------------------------------------------

    @Override
    public PricingResponse getPricingByVariant(Integer variantId) {

        PartVariant variant = getVariant(variantId);

        PricingConfiguration configuration =
                configurationRepository
                        .findByVariant_VariantId(variantId)
                        .orElse(null);

        if (configuration != null) {
            return toResponse(configuration);
        }

        // If no configuration exists yet, initialize from the
        // current Selling price, if available.
        Pricing currentPrice = getCurrentSellingPrice(variantId);

        BigDecimal basePrice = currentPrice != null
                ? money(currentPrice.getPrice())
                : BigDecimal.ZERO;

        return PricingResponse.builder()
                .partId(variant.getPart().getPartId())
                .variantId(variant.getVariantId())
                .partCode(variant.getPart().getPartCode())
                .partName(variant.getPart().getPartName())
                .unit(variant.getPart().getUnitOfMeasure())
                .basePrice(basePrice)
                .materialCost(BigDecimal.ZERO)
                .labourCost(BigDecimal.ZERO)
                .machiningCost(BigDecimal.ZERO)
                .otherCharges(BigDecimal.ZERO)
                .discountPercent(BigDecimal.ZERO)
                .profitMarginPercent(new BigDecimal("18.00"))
                .taxPercent(new BigDecimal("18.00"))
                .totalCost(basePrice)
                .discountAmount(BigDecimal.ZERO)
                .profitAmount(percentOf(
                        basePrice,
                        new BigDecimal("18.00")
                ))
                .subtotal(basePrice.add(percentOf(
                        basePrice,
                        new BigDecimal("18.00")
                )))
                .taxAmount(BigDecimal.ZERO)
                .finalSellingPrice(basePrice)
                .currency(CURRENCY)
                .build();
    }

    // ---------------------------------------------------------
    // API 2: Calculate, save configuration and selling price
    // ---------------------------------------------------------

    @Override
    @Transactional
    public PricingResponse savePricing(SavePricingRequest request) {

        PartVariant variant = getVariant(request.getVariantId());

        // Validate all amounts and percentages on the server.
        validateRequest(request);

        BigDecimal basePrice = money(request.getBasePrice());
        BigDecimal materialCost = money(request.getMaterialCost());
        BigDecimal labourCost = money(request.getLabourCost());
        BigDecimal machiningCost = money(request.getMachiningCost());
        BigDecimal otherCharges = money(request.getOtherCharges());

        BigDecimal discountPercent = request.getDiscountPercent();
        BigDecimal profitMarginPercent = request.getProfitMarginPercent();
        BigDecimal taxPercent = request.getTaxPercent();

        // 1. Total cost
        BigDecimal totalCost = basePrice
                .add(materialCost)
                .add(labourCost)
                .add(machiningCost)
                .add(otherCharges)
                .setScale(2, RoundingMode.HALF_UP);

        // 2. Discount
        BigDecimal discountAmount = percentOf(
                totalCost,
                discountPercent
        );

        // 3. Profit margin applied after discount
        BigDecimal discountedCost = totalCost.subtract(discountAmount);

        BigDecimal profitAmount = percentOf(
                discountedCost,
                profitMarginPercent
        );

        // 4. Subtotal
        BigDecimal subtotal = discountedCost
                .add(profitAmount)
                .setScale(2, RoundingMode.HALF_UP);

        // 5. Tax
        BigDecimal taxAmount = percentOf(subtotal, taxPercent);

        // 6. Final selling price
        BigDecimal finalSellingPrice = subtotal
                .add(taxAmount)
                .setScale(2, RoundingMode.HALF_UP);

        // Load existing configuration or create a new one.
        PricingConfiguration configuration =
                configurationRepository
                        .findByVariant_VariantId(request.getVariantId())
                        .orElseGet(PricingConfiguration::new);

        configuration.setVariant(variant);
        configuration.setBasePrice(basePrice);
        configuration.setMaterialCost(materialCost);
        configuration.setLabourCost(labourCost);
        configuration.setMachiningCost(machiningCost);
        configuration.setOtherCharges(otherCharges);
        configuration.setDiscountPercent(discountPercent);
        configuration.setProfitMarginPercent(profitMarginPercent);
        configuration.setTaxPercent(taxPercent);
        configuration.setTotalCost(totalCost);
        configuration.setSubtotal(subtotal);
        configuration.setTaxAmount(taxAmount);
        configuration.setFinalSellingPrice(finalSellingPrice);
        configuration.setCurrency(CURRENCY);

        // Save configuration within the current transaction.
        configurationRepository.save(configuration);

        // Synchronize the current Selling price record.
        synchronizeSellingPrice(
                variant,
                finalSellingPrice
        );

        // Return the calculated values to the frontend.
        return PricingResponse.builder()
                .partId(variant.getPart().getPartId())
                .variantId(variant.getVariantId())
                .partCode(variant.getPart().getPartCode())
                .partName(variant.getPart().getPartName())
                .unit(variant.getPart().getUnitOfMeasure())
                .basePrice(basePrice)
                .materialCost(materialCost)
                .labourCost(labourCost)
                .machiningCost(machiningCost)
                .otherCharges(otherCharges)
                .discountPercent(discountPercent)
                .profitMarginPercent(profitMarginPercent)
                .taxPercent(taxPercent)
                .totalCost(totalCost)
                .discountAmount(discountAmount)
                .profitAmount(profitAmount)
                .subtotal(subtotal)
                .taxAmount(taxAmount)
                .finalSellingPrice(finalSellingPrice)
                .currency(CURRENCY)
                .build();
    }

    // ---------------------------------------------------------
    // Keep the existing pricing table synchronized
    // ---------------------------------------------------------

    private void synchronizeSellingPrice(
            PartVariant variant,
            BigDecimal finalSellingPrice
    ) {
        LocalDate today = LocalDate.now();

        Pricing currentPrice = getCurrentSellingPrice(
                variant.getVariantId()
        );

        // Avoid creating duplicate records when the price is unchanged.
        if (currentPrice != null
                && money(currentPrice.getPrice())
                .compareTo(finalSellingPrice) == 0
                && CURRENCY.equalsIgnoreCase(currentPrice.getCurrency())) {
            return;
        }

        // Close the previous current record to preserve price history.
        if (currentPrice != null) {
            currentPrice.setEffectiveTo(today.minusDays(1));
            pricingRepository.save(currentPrice);
        }

        Pricing newPrice = new Pricing();
        newPrice.setVariant(variant);
        newPrice.setPrice(finalSellingPrice);
        newPrice.setCurrency(CURRENCY);
        newPrice.setEffectiveFrom(today);
        newPrice.setEffectiveTo(null);

        pricingRepository.save(newPrice);
    }

    // ---------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------

    private PartVariant getVariant(Integer variantId) {
        return partVariantRepository.findById(variantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Part variant not found: " + variantId
                ));
    }

    private Pricing getCurrentSellingPrice(Integer variantId) {
        return pricingRepository
                .findTopByVariant_VariantIdAndEffectiveToIsNullOrderByEffectiveFromDesc(
                        variantId
                )
                .orElse(null);
    }

    private PricingResponse toResponse(
            PricingConfiguration configuration
    ) {
        PartVariant variant = configuration.getVariant();

        BigDecimal totalCost = money(configuration.getTotalCost());
        BigDecimal discountPercent =
                configuration.getDiscountPercent();
        BigDecimal profitMarginPercent =
                configuration.getProfitMarginPercent();
        BigDecimal taxPercent = configuration.getTaxPercent();

        BigDecimal discountAmount = percentOf(
                totalCost,
                discountPercent
        );

        BigDecimal profitAmount = percentOf(
                totalCost.subtract(discountAmount),
                profitMarginPercent
        );

        return PricingResponse.builder()
                .partId(variant.getPart().getPartId())
                .variantId(variant.getVariantId())
                .partCode(variant.getPart().getPartCode())
                .partName(variant.getPart().getPartName())
                .unit(variant.getPart().getUnitOfMeasure())
                .basePrice(money(configuration.getBasePrice()))
                .materialCost(money(configuration.getMaterialCost()))
                .labourCost(money(configuration.getLabourCost()))
                .machiningCost(money(configuration.getMachiningCost()))
                .otherCharges(money(configuration.getOtherCharges()))
                .discountPercent(discountPercent)
                .profitMarginPercent(profitMarginPercent)
                .taxPercent(taxPercent)
                .totalCost(totalCost)
                .discountAmount(discountAmount)
                .profitAmount(profitAmount)
                .subtotal(money(configuration.getSubtotal()))
                .taxAmount(money(configuration.getTaxAmount()))
                .finalSellingPrice(
                        money(configuration.getFinalSellingPrice())
                )
                .currency(configuration.getCurrency())
                .build();
    }

    private BigDecimal percentOf(
            BigDecimal amount,
            BigDecimal percentage
    ) {
        return amount.multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal money(BigDecimal value) {
        return value == null
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : value.setScale(2, RoundingMode.HALF_UP);
    }

    private void validateRequest(SavePricingRequest request) {

        validateNonNegative("basePrice", request.getBasePrice());
        validateNonNegative("materialCost", request.getMaterialCost());
        validateNonNegative("labourCost", request.getLabourCost());
        validateNonNegative("machiningCost", request.getMachiningCost());
        validateNonNegative("otherCharges", request.getOtherCharges());

        validatePercentage(
                "discountPercent",
                request.getDiscountPercent()
        );
        validatePercentage(
                "profitMarginPercent",
                request.getProfitMarginPercent()
        );
        validatePercentage(
                "taxPercent",
                request.getTaxPercent()
        );
    }

    private void validateNonNegative(String field, BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    field + " must be zero or greater"
            );
        }
    }

    private void validatePercentage(String field, BigDecimal value) {
        if (value == null
                || value.compareTo(BigDecimal.ZERO) < 0
                || value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(
                    field + " must be between 0 and 100"
            );
        }
    }
}
