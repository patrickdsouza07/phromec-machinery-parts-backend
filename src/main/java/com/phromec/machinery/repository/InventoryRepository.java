package com.phromec.machinery.repository;
import com.phromec.machinery.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {}
