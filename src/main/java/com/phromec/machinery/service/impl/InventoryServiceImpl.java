package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.inventory.*;
import com.phromec.machinery.repository.InventoryRepository;
import com.phromec.machinery.service.InventoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private static final Map<String, String> SORT_FIELDS = Map.of(
            "partName", "partName",
            "partCode", "partCode",
            "machineTypeName", "machineTypeName",
            "quantityInStock", "quantityInStock",
            "reservedQuantity", "reservedQuantity",
            "reorderLevel", "reorderLevel",
            "lastUpdated", "lastUpdated"
    );

    @Override
    public InventoryListResponse getInventory(
            int page,
            int size,
            String search,
            String stockStatus,
            String sortBy,
            String direction
    ) {

        page = Math.max(page, 0);

        if (size <= 0) {
            size = 10;
        }

        size = Math.min(size, 100);

        if (search != null) {
            search = search.trim();

            if (search.isEmpty()) {
                search = null;
            }
        }

        stockStatus = normalizeStockStatus(stockStatus);

        String sortProperty = SORT_FIELDS.getOrDefault(
                sortBy,
                "partName"
        );

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortProperty)
                .and(Sort.by(Sort.Direction.ASC, "inventoryId"));

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<InventoryItemProjection> inventoryPage =
                inventoryRepository.searchInventory(
                        search,
                        stockStatus,
                        pageable
                );

        List<InventoryItemResponse> items =
                inventoryPage.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        InventorySummaryResponse summary = getSummary();

        return InventoryListResponse.builder()
                .summary(summary)
                .inventory(items)
                .page(inventoryPage.getNumber())
                .size(inventoryPage.getSize())
                .totalElements(inventoryPage.getTotalElements())
                .totalPages(inventoryPage.getTotalPages())
                .build();
    }

    private InventoryItemResponse toResponse(
            InventoryItemProjection projection
    ) {

        BigDecimal quantityInStock =
                zeroIfNull(projection.getQuantityInStock());

        BigDecimal reserved =
                zeroIfNull(projection.getReservedQuantity());

        BigDecimal minStock =
                zeroIfNull(projection.getReorderLevel());

        BigDecimal available = quantityInStock.subtract(reserved);

        String stockStatus;

        if (available.compareTo(BigDecimal.ZERO) <= 0) {
            stockStatus = "Out of Stock";
        } else if (available.compareTo(minStock) <= 0) {
            stockStatus = "Low Stock";
        } else {
            stockStatus = "In Stock";
        }

        return InventoryItemResponse.builder()
                .inventoryId(projection.getInventoryId())
                .variantId(projection.getVariantId())
                .partName(projection.getPartName())
                .partCode(projection.getPartCode())
                .variantCode(projection.getVariantCode())
                .machineTypeName(projection.getMachineTypeName())
                .available(available)
                .reserved(reserved)
                .minStock(minStock)
                .unit(projection.getUnit())
                .stockStatus(stockStatus)
                .lastUpdated(projection.getLastUpdated())
                .build();
    }

    private InventorySummaryResponse getSummary() {

        InventorySummaryProjection projection =
                inventoryRepository.getInventorySummary();

        if (projection == null) {
            return InventorySummaryResponse.builder()
                    .totalParts(0)
                    .inStock(0)
                    .lowStock(0)
                    .outOfStock(0)
                    .build();
        }

        return InventorySummaryResponse.builder()
                .totalParts(safeLong(projection.getTotalParts()))
                .inStock(safeLong(projection.getInStock()))
                .lowStock(safeLong(projection.getLowStock()))
                .outOfStock(safeLong(projection.getOutOfStock()))
                .build();
    }

    private String normalizeStockStatus(String stockStatus) {

        if (stockStatus == null || stockStatus.isBlank()
                || "ALL".equalsIgnoreCase(stockStatus)) {
            return null;
        }

        return switch (stockStatus.trim()
                .toUpperCase()
                .replace(' ', '_')
                .replace('-', '_')) {

            case "IN_STOCK" -> "IN_STOCK";
            case "LOW_STOCK" -> "LOW_STOCK";
            case "OUT_OF_STOCK" -> "OUT_OF_STOCK";

            default -> throw new IllegalArgumentException(
                    "Invalid stock status. Use ALL, IN_STOCK, "
                            + "LOW_STOCK, or OUT_OF_STOCK."
            );
        };
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private long safeLong(Long value) {
        return value != null ? value : 0L;
    }
}
