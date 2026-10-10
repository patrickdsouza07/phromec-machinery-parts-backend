package com.phromec.machinery.controller;

import com.phromec.machinery.dto.inventory.InventoryListResponse;
import com.phromec.machinery.service.InventoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    public ResponseEntity<InventoryListResponse> getInventory(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "8")
            int size,
            @RequestParam(required = false)
            String search,
            @RequestParam(defaultValue = "ALL")
            String stockStatus,
            @RequestParam(defaultValue = "partName")
            String sortBy,
            @RequestParam(defaultValue = "asc")
            String direction
    ) {

        InventoryListResponse response =
                inventoryService.getInventory(
                        page,
                        size,
                        search,
                        stockStatus,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(response);
    }
}