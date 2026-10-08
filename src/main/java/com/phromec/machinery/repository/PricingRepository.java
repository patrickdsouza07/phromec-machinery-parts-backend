package com.phromec.machinery.repository;

import com.phromec.machinery.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {
    @Query("""
        SELECT p.variant.part.partId, p.price
        FROM Pricing p
        WHERE p.variant.part.partId IN :partIds
          AND p.priceType = com.phromec.machinery.model.PriceType.PURCHASE
          AND p.effectiveFrom <= :today
          AND (p.effectiveTo IS NULL OR p.effectiveTo >= :today)
          AND p.effectiveFrom = (
              SELECT MAX(p2.effectiveFrom) FROM Pricing p2
              WHERE p2.variant = p.variant
                AND p2.priceType = p.priceType
                AND p2.effectiveFrom <= :today
                AND (p2.effectiveTo IS NULL OR p2.effectiveTo >= :today)
          )
        """)
    List<Object[]> getCurrentBasePrices(@Param("partIds") List<Integer> partIds,
                                        @Param("today") LocalDate today);
}
