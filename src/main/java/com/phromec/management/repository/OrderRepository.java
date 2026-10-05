package com.phromec.management.repository;

import com.phromec.management.dto.OrderSummaryProjection;
import com.phromec.management.model.Order;
import com.phromec.management.model.OrderStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    /**
     * Fetch orders together with Customer and Quotation.
     *
     * @EntityGraph prevents N+1 queries when the service accesses:
     *
     * order.getCustomer()
     * order.getQuotation()
     */
    @EntityGraph(attributePaths = {
            "customer",
            "quotation"
    })
    @Query(
            value = """
            SELECT o
            FROM Order o
            LEFT JOIN o.customer c
            LEFT JOIN o.quotation q
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(o.orderNumber)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(c.companyName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(q.quotationNumber)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR o.status = :status
                )
            """,
            countQuery = """
            SELECT COUNT(o)
            FROM Order o
            LEFT JOIN o.customer c
            LEFT JOIN o.quotation q
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(o.orderNumber)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(c.companyName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(q.quotationNumber)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR o.status = :status
                )
            """
    )
    Page<Order> searchOrders(
            @Param("search") String search,
            @Param("status") OrderStatus status,
            Pageable pageable
    );


    /**
     * Returns dashboard/order-page summary in ONE database query.
     */
    @Query("""
        SELECT
            COUNT(o) AS totalOrders,

            COALESCE(
                SUM(o.totalAmount),
                0
            ) AS totalValue,

            COALESCE(
                SUM(
                    CASE
                        WHEN o.status = :processing
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS processing,

            COALESCE(
                SUM(
                    CASE
                        WHEN o.status = :confirmed
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS confirmed,

            COALESCE(
                SUM(
                    CASE
                        WHEN o.status = :inProduction
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS inProduction,

            COALESCE(
                SUM(
                    CASE
                        WHEN o.status = :shipped
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS shipped,

            COALESCE(
                SUM(
                    CASE
                        WHEN o.status = :completed
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS completed

        FROM Order o
        """)
    OrderSummaryProjection getOrderSummary(
            @Param("processing") OrderStatus processing,
            @Param("confirmed") OrderStatus confirmed,
            @Param("inProduction") OrderStatus inProduction,
            @Param("shipped") OrderStatus shipped,
            @Param("completed") OrderStatus completed
    );


    boolean existsByOrderNumber(String orderNumber);


    boolean existsByOrderNumberAndOrderIdNot(
            String orderNumber,
            Integer orderId
    );
}