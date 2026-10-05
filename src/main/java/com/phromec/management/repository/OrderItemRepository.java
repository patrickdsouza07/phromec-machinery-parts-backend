package com.phromec.management.repository;
import com.phromec.management.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {}
