package com.phromec.management.service;

import com.phromec.management.dto.OrderListResponse;
import com.phromec.management.dto.OrderResponse;
import com.phromec.management.model.Order;
import com.phromec.management.model.OrderStatus;

public interface OrderService {

    OrderListResponse getOrders(
            int page,
            int size,
            String search,
            OrderStatus status,
            String sortBy,
            String direction
    );

    OrderResponse getOrderById(Integer orderId);

    Order createOrder(Order order);

    Order updateOrder(
            Integer orderId,
            Order order
    );

    void deleteOrder(Integer orderId);
}