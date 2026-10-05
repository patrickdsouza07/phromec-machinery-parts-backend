package com.phromec.management.service.impl;

import com.phromec.management.dto.OrderListResponse;
import com.phromec.management.dto.OrderResponse;
import com.phromec.management.dto.OrderSummaryProjection;
import com.phromec.management.dto.OrderSummaryResponse;
import com.phromec.management.model.Order;
import com.phromec.management.model.OrderStatus;
import com.phromec.management.repository.OrderRepository;
import com.phromec.management.service.OrderService;

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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    /*
     * Only these fields can be used for sorting.
     *
     * This prevents the frontend from sending arbitrary
     * database/entity properties.
     */
    private static final Map<String, String> SORT_FIELDS = Map.of(
            "orderNumber", "orderNumber",
            "orderDate", "orderDate",
            "deliveryDate", "expectedDeliveryDate",
            "expectedDeliveryDate", "expectedDeliveryDate",
            "amount", "totalAmount",
            "totalAmount", "totalAmount",
            "status", "status"
    );


    @Override
    @Transactional(readOnly = true)
    public OrderListResponse getOrders(
            int page,
            int size,
            String search,
            OrderStatus status,
            String sortBy,
            String direction
    ) {

        /*
         * -----------------------------
         * Validate pagination
         * -----------------------------
         */

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 10;
        }

        /*
         * Prevent unnecessarily large requests.
         */
        if (size > 100) {
            size = 100;
        }


        /*
         * -----------------------------
         * Clean search
         * -----------------------------
         */

        if (search != null) {
            search = search.trim();

            if (search.isEmpty()) {
                search = null;
            }
        }


        /*
         * -----------------------------
         * Build sorting
         * -----------------------------
         */

        String sortProperty = resolveSortProperty(sortBy);

        Sort.Direction sortDirection =
                resolveSortDirection(direction);

        Sort sort = Sort.by(
                sortDirection,
                sortProperty
        );


        /*
         * Add orderId as a secondary sort.
         *
         * This gives deterministic pagination when
         * multiple orders have the same date/amount/etc.
         */
        sort = sort.and(
                Sort.by(
                        Sort.Direction.DESC,
                        "orderId"
                )
        );


        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );


        /*
         * -----------------------------
         * Fetch paginated orders
         * -----------------------------
         *
         * EntityGraph fetches:
         *
         * Order
         *   ├── Customer
         *   └── Quotation
         *
         * in the same operation, preventing N+1
         * queries during DTO mapping.
         */
        Page<Order> orderPage =
                orderRepository.searchOrders(
                        search,
                        status,
                        pageable
                );


        /*
         * -----------------------------
         * Convert entities to DTOs
         * -----------------------------
         */

        List<OrderResponse> orders =
                orderPage
                        .getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();


        /*
         * -----------------------------
         * Get summary
         * -----------------------------
         */

        OrderSummaryResponse summary =
                buildSummary();


        /*
         * -----------------------------
         * Build final response
         * -----------------------------
         */

        return OrderListResponse.builder()
                .summary(summary)
                .orders(orders)
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(
            Integer orderId
    ) {

        /*
         * EntityGraph is not automatically applied to
         * findById unless configured on that method.
         *
         * Therefore, use a dedicated query/repository method
         * if the detail page needs customer + quotation.
         */

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with id: "
                                        + orderId
                        )
                );

        return mapToResponse(order);
    }


    @Override
    public Order createOrder(Order order) {

        validateOrder(order);

        if (orderRepository.existsByOrderNumber(
                order.getOrderNumber()
        )) {

            throw new IllegalArgumentException(
                    "Order number already exists: "
                            + order.getOrderNumber()
            );
        }

        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }

        if (order.getTotalAmount() == null) {
            order.setTotalAmount(BigDecimal.ZERO);
        }

        return orderRepository.save(order);
    }


    @Override
    public Order updateOrder(
            Integer orderId,
            Order orderDetails
    ) {

        Order existingOrder =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found with id: "
                                                + orderId
                                )
                        );


        /*
         * Order number
         */
        if (orderDetails.getOrderNumber() != null
                && !orderDetails.getOrderNumber()
                .equals(existingOrder.getOrderNumber())) {

            if (orderRepository
                    .existsByOrderNumberAndOrderIdNot(
                            orderDetails.getOrderNumber(),
                            orderId
                    )) {

                throw new IllegalArgumentException(
                        "Order number already exists: "
                                + orderDetails.getOrderNumber()
                );
            }

            existingOrder.setOrderNumber(
                    orderDetails.getOrderNumber()
            );
        }


        /*
         * Quotation
         */
        if (orderDetails.getQuotation() != null) {

            existingOrder.setQuotation(
                    orderDetails.getQuotation()
            );
        }


        /*
         * Customer
         */
        if (orderDetails.getCustomer() != null) {

            existingOrder.setCustomer(
                    orderDetails.getCustomer()
            );
        }


        /*
         * Order date
         */
        if (orderDetails.getOrderDate() != null) {

            existingOrder.setOrderDate(
                    orderDetails.getOrderDate()
            );
        }


        /*
         * Expected delivery
         */
        if (orderDetails.getExpectedDeliveryDate()
                != null) {

            existingOrder.setExpectedDeliveryDate(
                    orderDetails
                            .getExpectedDeliveryDate()
            );
        }


        /*
         * Status
         */
        if (orderDetails.getStatus() != null) {

            existingOrder.setStatus(
                    orderDetails.getStatus()
            );
        }


        /*
         * Amount
         */
        if (orderDetails.getTotalAmount() != null) {

            existingOrder.setTotalAmount(
                    orderDetails.getTotalAmount()
            );
        }


        /*
         * Notes
         */
        if (orderDetails.getNotes() != null) {

            existingOrder.setNotes(
                    orderDetails.getNotes()
            );
        }


        /*
         * Created by
         */
        if (orderDetails.getCreatedBy() != null) {

            existingOrder.setCreatedBy(
                    orderDetails.getCreatedBy()
            );
        }


        return orderRepository.save(
                existingOrder
        );
    }


    @Override
    public void deleteOrder(Integer orderId) {

        if (!orderRepository.existsById(orderId)) {

            throw new RuntimeException(
                    "Order not found with id: "
                            + orderId
            );
        }

        orderRepository.deleteById(orderId);
    }


    /*
     * =========================================================
     * SUMMARY
     * =========================================================
     */

    private OrderSummaryResponse buildSummary() {

        OrderSummaryProjection result =
                orderRepository.getOrderSummary(
                        OrderStatus.PROCESSING,
                        OrderStatus.CONFIRMED,
                        OrderStatus.IN_PRODUCTION,
                        OrderStatus.SHIPPED,
                        OrderStatus.COMPLETED
                );


        if (result == null) {

            return OrderSummaryResponse.builder()
                    .totalOrders(0L)
                    .totalValue(BigDecimal.ZERO)
                    .processing(0L)
                    .confirmed(0L)
                    .inProduction(0L)
                    .shipped(0L)
                    .completed(0L)
                    .build();
        }


        return OrderSummaryResponse.builder()
                .totalOrders(
                        safeLong(result.getTotalOrders())
                )
                .totalValue(
                        result.getTotalValue() != null
                                ? result.getTotalValue()
                                : BigDecimal.ZERO
                )
                .processing(
                        safeLong(result.getProcessing())
                )
                .confirmed(
                        safeLong(result.getConfirmed())
                )
                .inProduction(
                        safeLong(result.getInProduction())
                )
                .shipped(
                        safeLong(result.getShipped())
                )
                .completed(
                        safeLong(result.getCompleted())
                )
                .build();
    }


    /*
     * =========================================================
     * ENTITY -> RESPONSE DTO
     * =========================================================
     */

    private OrderResponse mapToResponse(
            Order order
    ) {

        String customerName = null;

        if (order.getCustomer() != null) {

            customerName =
                    order.getCustomer()
                            .getCompanyName();
        }


        String quotationNumber = null;

        if (order.getQuotation() != null) {

            quotationNumber =
                    order.getQuotation()
                            .getQuotationNumber();
        }


        String status = null;

        if (order.getStatus() != null) {

            status =
                    order.getStatus()
                            .getDisplayName();
        }


        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .customerName(customerName)
                .quotationNumber(quotationNumber)
                .orderDate(order.getOrderDate())
                .expectedDeliveryDate(
                        order.getExpectedDeliveryDate()
                )
                .totalAmount(
                        order.getTotalAmount()
                )
                .status(status)
                .build();
    }


    /*
     * =========================================================
     * VALIDATION
     * =========================================================
     */

    private void validateOrder(Order order) {

        if (order == null) {

            throw new IllegalArgumentException(
                    "Order cannot be null"
            );
        }


        if (order.getOrderNumber() == null
                || order.getOrderNumber()
                .trim()
                .isEmpty()) {

            throw new IllegalArgumentException(
                    "Order number is required"
            );
        }


        if (order.getOrderDate() == null) {

            throw new IllegalArgumentException(
                    "Order date is required"
            );
        }


        if (order.getCustomer() == null) {

            throw new IllegalArgumentException(
                    "Customer is required"
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

            return "orderDate";
        }

        return SORT_FIELDS.getOrDefault(
                sortBy,
                "orderDate"
        );
    }


    private Sort.Direction resolveSortDirection(
            String direction
    ) {

        if (direction == null
                || direction.trim().isEmpty()) {

            return Sort.Direction.DESC;
        }

        if ("asc".equalsIgnoreCase(direction)) {

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