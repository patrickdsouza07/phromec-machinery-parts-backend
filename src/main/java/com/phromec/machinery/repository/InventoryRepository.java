package com.phromec.machinery.repository;
import com.phromec.machinery.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    @Query("""
        SELECT i.variant.part.partId, SUM(i.quantityInStock - COALESCE(i.reservedQuantity, 0))
        FROM Inventory i
        WHERE i.variant.part.partId IN :partIds
        GROUP BY i.variant.part.partId
        """)
    List<Object[]> getAvailableQuantities(@Param("partIds") List<Integer> partIds);
}
