package com.phromec.machinery.repository;

import com.phromec.machinery.dto.quotation.QuotationSummaryProjection;
import com.phromec.machinery.model.quotation.Quotation;
import com.phromec.machinery.model.quotation.QuotationStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuotationRepository
        extends JpaRepository<Quotation, Integer> {


    /**
     * Get quotations for the quotation listing screen.
     *
     * Customer and createdBy are fetched together to
     * prevent N+1 queries when converting entities to DTOs.
     */
    @EntityGraph(attributePaths = {
            "customer",
            "createdBy"
    })
    @Query(
            value = """
            SELECT q
            FROM Quotation q
            LEFT JOIN q.customer c
            LEFT JOIN q.createdBy u
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(q.quotationNumber)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(c.companyName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.fullName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR q.status = :status
                )
            """,
            countQuery = """
            SELECT COUNT(q)
            FROM Quotation q
            LEFT JOIN q.customer c
            LEFT JOIN q.createdBy u
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(q.quotationNumber)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(c.companyName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.fullName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR q.status = :status
                )
            """
    )
    Page<Quotation> searchQuotations(
            @Param("search") String search,
            @Param("status") QuotationStatus status,
            Pageable pageable
    );


    /**
     * Fetch one quotation with its related Customer
     * and CreatedBy User.
     */
    @EntityGraph(attributePaths = {
            "customer",
            "createdBy"
    })
    @Query("""
        SELECT q
        FROM Quotation q
        WHERE q.quotationId = :quotationId
        """)
    Optional<Quotation> findQuotationWithDetails(
            @Param("quotationId") Integer quotationId
    );


    /**
     * Calculate all quotation status counters in ONE query.
     */
    @Query("""
        SELECT

            COUNT(q) AS totalQuotations,

            COALESCE(
                SUM(
                    CASE
                        WHEN q.status = :draft
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS draft,

            COALESCE(
                SUM(
                    CASE
                        WHEN q.status = :sent
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS sent,

            COALESCE(
                SUM(
                    CASE
                        WHEN q.status = :underReview
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS underReview,

            COALESCE(
                SUM(
                    CASE
                        WHEN q.status = :approved
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS approved,

            COALESCE(
                SUM(
                    CASE
                        WHEN q.status = :rejected
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS rejected,

            COALESCE(
                SUM(
                    CASE
                        WHEN q.status = :expired
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS expired

        FROM Quotation q
        """)
    QuotationSummaryProjection getQuotationSummary(

            @Param("draft")
            QuotationStatus draft,

            @Param("sent")
            QuotationStatus sent,

            @Param("underReview")
            QuotationStatus underReview,

            @Param("approved")
            QuotationStatus approved,

            @Param("rejected")
            QuotationStatus rejected,

            @Param("expired")
            QuotationStatus expired
    );


    boolean existsByQuotationNumber(
            String quotationNumber
    );


    boolean existsByQuotationNumberAndQuotationIdNot(
            String quotationNumber,
            Integer quotationId
    );
}