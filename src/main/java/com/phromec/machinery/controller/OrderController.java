package com.phromec.machinery.controller;

import com.phromec.machinery.dto.order.OrderListResponse;
import com.phromec.machinery.dto.order.OrderResponse;
import com.phromec.machinery.model.order.Order;
import com.phromec.machinery.model.order.OrderStatus;
import com.phromec.machinery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Get paginated orders.
     *
     * Examples:
     *
     * GET /phromecManagement/api/v1/getAllOrders
     *
     * GET /phromecManagement/api/v1/getAllOrders?page=0&size=10
     *
     * GET /phromecManagement/api/v1/getAllOrders?search=tata
     *
     * GET /phromecManagement/api/v1/getAllOrders?status=PROCESSING
     *
     * GET /phromecManagement/api/v1/getAllOrders?status=In%20Production
     *
     * GET /phromecManagement/api/v1/getAllOrders?sortBy=orderDate&direction=desc
     *
     * GET /phromecManagement/api/v1/getAllOrders?search=tata&status=CONFIRMED
     */
    @GetMapping()
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    public ResponseEntity<OrderListResponse> getOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        OrderStatus orderStatus =
                parseStatus(status);

        OrderListResponse response =
                orderService.getOrders(
                        pageNo,
                        pageSize,
                        search,
                        orderStatus,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(response);
    }


    /**
     * Get one order.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Integer orderId
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(
                        orderId
                )
        );
    }


    /**
     * Create order.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody Order order
    ) {

        Order created =
                orderService.createOrder(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    /**
     * Update order.
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Integer orderId,
            @RequestBody Order order
    ) {

        return ResponseEntity.ok(
                orderService.updateOrder(
                        orderId,
                        order
                )
        );
    }


    /**
     * Delete order.
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Integer orderId
    ) {

        orderService.deleteOrder(
                orderId
        );

        return ResponseEntity.noContent()
                .build();
    }


    /**
     * Convert frontend status into OrderStatus.
     *
     * Supports both:
     *
     * PROCESSING & Processing
     *
     * IN_PRODUCTION & In Production
     */
    private OrderStatus parseStatus(
            String status
    ) {

        if (status == null
                || status.trim().isEmpty()
                || "ALL".equalsIgnoreCase(status)) {

            return null;
        }

        try {

            return OrderStatus.fromValue(
                    status.trim()
            );

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    "Invalid order status: "
                            + status
            );
        }
    }
}
