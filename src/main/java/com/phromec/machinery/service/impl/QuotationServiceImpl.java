package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.quotation.QuotationListResponse;
import com.phromec.machinery.dto.quotation.QuotationResponse;
import com.phromec.machinery.dto.quotation.QuotationSummaryProjection;
import com.phromec.machinery.dto.quotation.QuotationSummaryResponse;
import com.phromec.machinery.model.quotation.Quotation;
import com.phromec.machinery.model.quotation.QuotationStatus;
import com.phromec.machinery.repository.QuotationRepository;
import com.phromec.machinery.service.QuotationService;

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
@Transactional
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;

    /*
     * Whitelist of fields that the frontend
     * is allowed to sort by.
     */
    private static final Map<String, String> SORT_FIELDS =
            Map.of(
                    "quotationNumber",
                    "quotationNumber",

                    "quotationDate",
                    "quotationDate",

                    "validUntil",
                    "validUntil",

                    "amount",
                    "totalAmount",

                    "totalAmount",
                    "totalAmount",

                    "status",
                    "status"
            );


    @Override
    @Transactional(readOnly = true)
    public QuotationListResponse getQuotations(

            int page,

            int size,

            String search,

            QuotationStatus status,

            String sortBy,

            String direction
    ) {


        /*
         * ------------------------------
         * Pagination validation
         * ------------------------------
         */

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 10;
        }

        /*
         * Prevent very large requests.
         */
        if (size > 100) {
            size = 100;
        }


        /*
         * ------------------------------
         * Search cleanup
         * ------------------------------
         */

        if (search != null) {

            search = search.trim();

            if (search.isEmpty()) {
                search = null;
            }
        }


        /*
         * ------------------------------
         * Sorting
         * ------------------------------
         */

        String sortProperty =
                resolveSortProperty(sortBy);

        Sort.Direction sortDirection =
                resolveSortDirection(direction);

        Sort sort = Sort.by(
                sortDirection,
                sortProperty
        );


        /*
         * Deterministic secondary sorting.
         */
        sort = sort.and(
                Sort.by(
                        Sort.Direction.DESC,
                        "quotationId"
                )
        );


        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );


        /*
         * ------------------------------
         * Fetch quotations
         * ------------------------------
         */

        Page<Quotation> quotationPage =
                quotationRepository.searchQuotations(
                        search,
                        status,
                        pageable
                );


        /*
         * ------------------------------
         * Entity -> DTO
         * ------------------------------
         */

        List<QuotationResponse> quotations =
                quotationPage
                        .getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();


        /*
         * ------------------------------
         * Summary
         * ------------------------------
         */

        QuotationSummaryResponse summary =
                buildSummary();


        /*
         * ------------------------------
         * Final response
         * ------------------------------
         */

        return QuotationListResponse.builder()
                .summary(summary)
                .quotations(quotations)
                .page(quotationPage.getNumber())
                .size(quotationPage.getSize())
                .totalElements(
                        quotationPage.getTotalElements()
                )
                .totalPages(
                        quotationPage.getTotalPages()
                )
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public QuotationResponse getQuotationById(
            Integer quotationId
    ) {

        Quotation quotation =
                quotationRepository
                        .findQuotationWithDetails(
                                quotationId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Quotation not found with id: "
                                                + quotationId
                                )
                        );

        return mapToResponse(quotation);
    }


    @Override
    public Quotation createQuotation(
            Quotation quotation
    ) {

        validateQuotation(quotation);


        if (quotationRepository
                .existsByQuotationNumber(
                        quotation.getQuotationNumber()
                )) {

            throw new IllegalArgumentException(
                    "Quotation number already exists: "
                            + quotation.getQuotationNumber()
            );
        }


        if (quotation.getStatus() == null) {

            quotation.setStatus(
                    QuotationStatus.DRAFT
            );
        }


        if (quotation.getSubtotal() == null) {
            quotation.setSubtotal(
                    BigDecimal.ZERO
            );
        }

        if (quotation.getDiscount() == null) {
            quotation.setDiscount(
                    BigDecimal.ZERO
            );
        }

        if (quotation.getTax() == null) {
            quotation.setTax(
                    BigDecimal.ZERO
            );
        }

        if (quotation.getTotalAmount() == null) {
            quotation.setTotalAmount(
                    BigDecimal.ZERO
            );
        }


        return quotationRepository.save(
                quotation
        );
    }


    @Override
    public Quotation updateQuotation(
            Integer quotationId,
            Quotation quotationDetails
    ) {


        Quotation existing =
                quotationRepository
                        .findById(quotationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Quotation not found with id: "
                                                + quotationId
                                )
                        );


        /*
         * Quotation number
         */
        if (quotationDetails
                .getQuotationNumber() != null
                && !quotationDetails
                .getQuotationNumber()
                .equals(
                        existing.getQuotationNumber()
                )) {


            if (quotationRepository
                    .existsByQuotationNumberAndQuotationIdNot(
                            quotationDetails
                                    .getQuotationNumber(),
                            quotationId
                    )) {

                throw new IllegalArgumentException(
                        "Quotation number already exists: "
                                + quotationDetails
                                .getQuotationNumber()
                );
            }


            existing.setQuotationNumber(
                    quotationDetails
                            .getQuotationNumber()
            );
        }


        /*
         * Customer
         */
        if (quotationDetails.getCustomer() != null) {

            existing.setCustomer(
                    quotationDetails.getCustomer()
            );
        }


        /*
         * Quotation date
         */
        if (quotationDetails
                .getQuotationDate() != null) {

            existing.setQuotationDate(
                    quotationDetails
                            .getQuotationDate()
            );
        }


        /*
         * Valid until
         */
        if (quotationDetails
                .getValidUntil() != null) {

            existing.setValidUntil(
                    quotationDetails
                            .getValidUntil()
            );
        }


        /*
         * Status
         */
        if (quotationDetails.getStatus() != null) {

            existing.setStatus(
                    quotationDetails.getStatus()
            );
        }


        /*
         * Subtotal
         */
        if (quotationDetails.getSubtotal()
                != null) {

            existing.setSubtotal(
                    quotationDetails.getSubtotal()
            );
        }


        /*
         * Discount
         */
        if (quotationDetails.getDiscount()
                != null) {

            existing.setDiscount(
                    quotationDetails.getDiscount()
            );
        }


        /*
         * Tax
         */
        if (quotationDetails.getTax()
                != null) {

            existing.setTax(
                    quotationDetails.getTax()
            );
        }


        /*
         * Total
         */
        if (quotationDetails.getTotalAmount()
                != null) {

            existing.setTotalAmount(
                    quotationDetails
                            .getTotalAmount()
            );
        }


        /*
         * Notes
         */
        if (quotationDetails.getNotes()
                != null) {

            existing.setNotes(
                    quotationDetails.getNotes()
            );
        }


        /*
         * Created By
         */
        if (quotationDetails.getCreatedBy()
                != null) {

            existing.setCreatedBy(
                    quotationDetails.getCreatedBy()
            );
        }


        return quotationRepository.save(
                existing
        );
    }


    @Override
    public void deleteQuotation(
            Integer quotationId
    ) {

        if (!quotationRepository
                .existsById(quotationId)) {

            throw new RuntimeException(
                    "Quotation not found with id: "
                            + quotationId
            );
        }


        quotationRepository.deleteById(
                quotationId
        );
    }


    /*
     * =========================================================
     * SUMMARY
     * =========================================================
     */

    private QuotationSummaryResponse buildSummary() {

        QuotationSummaryProjection result =
                quotationRepository.getQuotationSummary(

                        QuotationStatus.DRAFT,

                        QuotationStatus.SENT,

                        QuotationStatus.UNDER_REVIEW,

                        QuotationStatus.APPROVED,

                        QuotationStatus.REJECTED,

                        QuotationStatus.EXPIRED
                );


        if (result == null) {

            return QuotationSummaryResponse
                    .builder()
                    .totalQuotations(0L)
                    .draft(0L)
                    .sent(0L)
                    .underReview(0L)
                    .approved(0L)
                    .rejected(0L)
                    .expired(0L)
                    .build();
        }


        return QuotationSummaryResponse
                .builder()

                .totalQuotations(
                        safeLong(
                                result.getTotalQuotations()
                        )
                )

                .draft(
                        safeLong(
                                result.getDraft()
                        )
                )

                .sent(
                        safeLong(
                                result.getSent()
                        )
                )

                .underReview(
                        safeLong(
                                result.getUnderReview()
                        )
                )

                .approved(
                        safeLong(
                                result.getApproved()
                        )
                )

                .rejected(
                        safeLong(
                                result.getRejected()
                        )
                )

                .expired(
                        safeLong(
                                result.getExpired()
                        )
                )

                .build();
    }


    /*
     * =========================================================
     * ENTITY -> DTO
     * =========================================================
     */

    private QuotationResponse mapToResponse(
            Quotation quotation
    ) {


        String customerName = null;

        if (quotation.getCustomer() != null) {

            customerName =
                    quotation
                            .getCustomer()
                            .getCompanyName();
        }


        String createdBy = null;

        if (quotation.getCreatedBy() != null) {

            /*
             * Assumes User has fullName.
             */
            createdBy =
                    quotation
                            .getCreatedBy()
                            .getFullName();
        }


        String status = null;

        if (quotation.getStatus() != null) {

            status =
                    quotation
                            .getStatus()
                            .getValue();
        }


        return QuotationResponse.builder()

                .quotationId(
                        quotation.getQuotationId()
                )

                .quotationNumber(
                        quotation.getQuotationNumber()
                )

                .customerName(
                        customerName
                )

                /*
                 * Machine cannot currently be populated
                 * because Quotation has no Machine relation.
                 */
                .machineName(null)

                .quotationDate(
                        quotation.getQuotationDate()
                )

                .validUntil(
                        quotation.getValidUntil()
                )

                .totalAmount(
                        quotation.getTotalAmount()
                )

                .createdBy(
                        createdBy
                )

                .status(
                        status
                )

                .build();
    }


    /*
     * =========================================================
     * VALIDATION
     * =========================================================
     */

    private void validateQuotation(
            Quotation quotation
    ) {

        if (quotation == null) {

            throw new IllegalArgumentException(
                    "Quotation cannot be null"
            );
        }


        if (quotation.getQuotationNumber()
                == null
                || quotation.getQuotationNumber()
                .trim()
                .isEmpty()) {

            throw new IllegalArgumentException(
                    "Quotation number is required"
            );
        }


        if (quotation.getCustomer() == null) {

            throw new IllegalArgumentException(
                    "Customer is required"
            );
        }


        if (quotation.getQuotationDate()
                == null) {

            throw new IllegalArgumentException(
                    "Quotation date is required"
            );
        }
    }


    /*
     * =========================================================
     * SORTING
     * =========================================================
     */

    private String resolveSortProperty(
            String sortBy
    ) {

        if (sortBy == null
                || sortBy.trim().isEmpty()) {

            return "quotationDate";
        }


        return SORT_FIELDS.getOrDefault(
                sortBy,
                "quotationDate"
        );
    }


    private Sort.Direction resolveSortDirection(
            String direction
    ) {

        if (direction == null
                || direction.trim().isEmpty()) {

            return Sort.Direction.DESC;
        }


        if ("asc".equalsIgnoreCase(
                direction
        )) {

            return Sort.Direction.ASC;
        }


        return Sort.Direction.DESC;
    }


    private long safeLong(Long value) {

        return value != null
                ? value
                : 0L;
    }
}