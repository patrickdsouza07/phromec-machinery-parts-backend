package com.phromec.machinery.repository;

import com.phromec.machinery.dto.inventory.InventoryItemProjection;
import com.phromec.machinery.dto.inventory.InventorySummaryProjection;
import com.phromec.machinery.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(
            value = """
            SELECT
                i.inventoryId AS inventoryId,
                v.variantId AS variantId,

                p.partName AS partName,
                p.partCode AS partCode,
                v.variantCode AS variantCode,

                mt.typeName AS machineTypeName,

                COALESCE(i.quantityInStock, 0)
                    AS quantityInStock,

                COALESCE(i.reservedQuantity, 0)
                    AS reservedQuantity,

                COALESCE(i.reorderLevel, 0)
                    AS reorderLevel,

                p.unitOfMeasure AS unit,
                i.lastUpdated AS lastUpdated

            FROM Inventory i

            JOIN i.variant v
            JOIN v.part p
            LEFT JOIN p.machineType mt

            WHERE
                (
                    :search IS NULL
                    OR :search = ''

                    OR LOWER(p.partName)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(p.partCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(v.variantCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mt.typeName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )

                AND
                (
                    :stockStatus IS NULL
                    OR
                    (
                        :stockStatus = 'OUT_OF_STOCK'
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) <= 0
                    )
                    OR
                    (
                        :stockStatus = 'LOW_STOCK'
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) > 0
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) <= COALESCE(i.reorderLevel, 0)
                    )
                    OR
                    (
                        :stockStatus = 'IN_STOCK'
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) > COALESCE(i.reorderLevel, 0)
                    )
                )
            """,

            countQuery = """
            SELECT COUNT(i)

            FROM Inventory i
            JOIN i.variant v
            JOIN v.part p
            LEFT JOIN p.machineType mt

            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(p.partName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(p.partCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(v.variantCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(mt.typeName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )

                AND
                (
                    :stockStatus IS NULL
                    OR
                    (
                        :stockStatus = 'OUT_OF_STOCK'
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) <= 0
                    )
                    OR
                    (
                        :stockStatus = 'LOW_STOCK'
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) > 0
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) <= COALESCE(i.reorderLevel, 0)
                    )
                    OR
                    (
                        :stockStatus = 'IN_STOCK'
                        AND
                        (
                            COALESCE(i.quantityInStock, 0)
                            - COALESCE(i.reservedQuantity, 0)
                        ) > COALESCE(i.reorderLevel, 0)
                    )
                )
            """
    )
    Page<InventoryItemProjection> searchInventory(
            @Param("search") String search,
            @Param("stockStatus") String stockStatus,
            Pageable pageable
    );


    @Query("""
        SELECT
            COUNT(i) AS totalParts,

            COALESCE(SUM(
                CASE
                    WHEN (
                        COALESCE(i.quantityInStock, 0)
                        - COALESCE(i.reservedQuantity, 0)
                    ) > COALESCE(i.reorderLevel, 0)
                    THEN 1 ELSE 0
                END
            ), 0) AS inStock,

            COALESCE(SUM(
                CASE
                    WHEN (
                        COALESCE(i.quantityInStock, 0)
                        - COALESCE(i.reservedQuantity, 0)
                    ) > 0
                    AND (
                        COALESCE(i.quantityInStock, 0)
                        - COALESCE(i.reservedQuantity, 0)
                    ) <= COALESCE(i.reorderLevel, 0)
                    THEN 1 ELSE 0
                END
            ), 0) AS lowStock,

            COALESCE(SUM(
                CASE
                    WHEN (
                        COALESCE(i.quantityInStock, 0)
                        - COALESCE(i.reservedQuantity, 0)
                    ) <= 0
                    THEN 1 ELSE 0
                END
            ), 0) AS outOfStock

        FROM Inventory i
        """)
    InventorySummaryProjection getInventorySummary();
}
