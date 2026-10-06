package com.phromec.machinery.service;

import com.phromec.machinery.dto.order.OrderListResponse;
import com.phromec.machinery.dto.order.OrderResponse;
import com.phromec.machinery.model.order.Order;
import com.phromec.machinery.model.order.OrderStatus;

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