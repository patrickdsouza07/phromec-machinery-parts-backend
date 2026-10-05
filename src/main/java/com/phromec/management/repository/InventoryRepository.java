package com.phromec.management.repository;
import com.phromec.management.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {}
