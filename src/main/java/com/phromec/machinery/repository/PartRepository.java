package com.phromec.machinery.repository;

import com.phromec.machinery.dto.part.PartSummaryProjection;
import com.phromec.machinery.dto.machine.MachineTypeCountProjection;
import com.phromec.machinery.model.part.Part;
import com.phromec.machinery.model.part.PartStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PartRepository
        extends JpaRepository<Part, Integer> {

    @Query("""
        SELECT mt.machineTypeId AS machineTypeId, COUNT(p.partId) AS count
        FROM Part p JOIN p.machineType mt
        GROUP BY mt.machineTypeId
        """)
    java.util.List<MachineTypeCountProjection> countPartsByType();


    /**
     * Search + status filter + pagination.
     *
     * MachineType is fetched together with Part.
     *
     * Variants are intentionally NOT fetched here because
     * this is a paginated query.
     */
    @EntityGraph(attributePaths = {
            "machineType"
    })
    @Query(
            value = """
            SELECT p
            FROM Part p
            LEFT JOIN p.machineType mt
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(p.partCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(p.partName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(p.description)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(mt.typeName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR p.status = :status
                )
            """,
            countQuery = """
            SELECT COUNT(p)
            FROM Part p
            LEFT JOIN p.machineType mt
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(p.partCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(p.partName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(p.description)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(mt.typeName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR p.status = :status
                )
            """
    )
    Page<Part> searchParts(
            @Param("search") String search,
            @Param("status") PartStatus status,
            Pageable pageable
    );


    /**
     * Fetch a single part with MachineType.
     */
    @EntityGraph(attributePaths = {
            "machineType"
    })
    @Query("""
        SELECT p
        FROM Part p
        WHERE p.partId = :partId
        """)
    Optional<Part> findPartWithDetails(
            @Param("partId") Integer partId
    );


    /**
     * Summary counters in one query.
     */
    @Query("""
        SELECT

            COUNT(p) AS totalParts,

            COALESCE(
                SUM(
                    CASE
                        WHEN p.status = :active
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS active,

            COALESCE(
                SUM(
                    CASE
                        WHEN p.status = :inactive
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS inactive

        FROM Part p
        """)
    PartSummaryProjection getPartSummary(

            @Param("active")
            PartStatus active,

            @Param("inactive")
            PartStatus inactive
    );


    boolean existsByPartCode(
            String partCode
    );


    boolean existsByPartCodeAndPartIdNot(
            String partCode,
            Integer partId
    );
}
