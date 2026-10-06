package com.phromec.machinery.controller;

import com.phromec.machinery.dto.quotation.QuotationListResponse;
import com.phromec.machinery.dto.quotation.QuotationResponse;
import com.phromec.machinery.model.quotation.Quotation;
import com.phromec.machinery.model.quotation.QuotationStatus;
import com.phromec.machinery.service.QuotationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    /**
     * Get quotations.
     *
     * Examples:
     *
     * GET /phromecManagement/api/v1/quotations
     *
     * GET /phromecManagement/api/v1/quotations?page=0&size=10
     *
     * GET /phromecManagement/api/v1/quotations?search=data
     *
     * GET /phromecManagement/api/v1/quotations?status=APPROVED
     *
     * GET /phromecManagement/api/v1/quotations?status=Approved
     *
     * GET /phromecManagement/api/v1/quotations?sortBy=quotationDate&direction=desc
     */
    @GetMapping
    public ResponseEntity<QuotationListResponse> getQuotations(
            @RequestParam( defaultValue = "0" ) int page,
            @RequestParam( defaultValue = "10" ) int size,
            @RequestParam( required = false )String search,
            @RequestParam( required = false )String status,
            @RequestParam( defaultValue = "quotationDate" ) String sortBy,
            @RequestParam( defaultValue = "desc" )String direction
    ) {

        QuotationStatus quotationStatus =
                parseStatus(status);

        QuotationListResponse response =
                quotationService.getQuotations(
                        page,
                        size,
                        search,
                        quotationStatus,
                        sortBy,
                        direction
                );


        return ResponseEntity.ok(response);
    }


    /**
     * Get quotation by ID.
     */
    @GetMapping("/{quotationId}")
    public ResponseEntity<QuotationResponse>
    getQuotation(
            @PathVariable Integer quotationId
    ) {

        return ResponseEntity.ok(
                quotationService
                        .getQuotationById(
                                quotationId
                        )
        );
    }


    /**
     * Create quotation.
     */
    @PostMapping
    public ResponseEntity<Quotation>
    createQuotation(
            @RequestBody Quotation quotation
    ) {

        Quotation created =
                quotationService
                        .createQuotation(
                                quotation
                        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    /**
     * Update quotation.
     */
    @PutMapping("/{quotationId}")
    public ResponseEntity<Quotation>
    updateQuotation(
            @PathVariable Integer quotationId,
            @RequestBody Quotation quotation
    ) {

        return ResponseEntity.ok(
                quotationService
                        .updateQuotation(
                                quotationId,
                                quotation
                        )
        );
    }


    /**
     * Delete quotation.
     */
    @DeleteMapping("/{quotationId}")
    public ResponseEntity<Void>
    deleteQuotation(
            @PathVariable Integer quotationId
    ) {

        quotationService.deleteQuotation(
                quotationId
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    /**
     * Converts both:
     *
     * APPROVED
     *
     * and
     *
     * Approved
     *
     * into QuotationStatus.APPROVED.
     */
    private QuotationStatus parseStatus(
            String status
    ) {

        if (status == null
                || status.trim().isEmpty()
                || "ALL".equalsIgnoreCase(status)) {

            return null;
        }

        for (QuotationStatus quotationStatus
                : QuotationStatus.values()) {

            if (quotationStatus.name()
                    .equalsIgnoreCase(
                            status.trim()
                    )
                    ||
                    quotationStatus.getValue()
                            .equalsIgnoreCase(
                                    status.trim()
                            )) {

                return quotationStatus;
            }
        }

        throw new IllegalArgumentException(
                "Invalid quotation status: "
                        + status
        );
    }
}