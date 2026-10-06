package com.phromec.machinery.repository;
import com.phromec.machinery.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Integer> {}
