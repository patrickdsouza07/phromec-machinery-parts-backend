package com.phromec.management.repository;
import com.phromec.management.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Integer> {}
