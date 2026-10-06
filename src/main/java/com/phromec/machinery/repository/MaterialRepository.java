package com.phromec.machinery.repository;

import com.phromec.machinery.dto.material.MaterialSummaryProjection;
import com.phromec.machinery.model.Material;
import com.phromec.machinery.model.RecordStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaterialRepository
        extends JpaRepository<Material, Integer> {

    /**
     * Search + status filter + pagination.
     */
    @Query(
            value = """
            SELECT m
            FROM Material m
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(m.materialCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.materialName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.description)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR m.status = :status
                )
            """,
            countQuery = """
            SELECT COUNT(m)
            FROM Material m
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(m.materialCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.materialName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.description)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND
                (
                    :status IS NULL
                    OR m.status = :status
                )
            """
    )
    Page<Material> searchMaterials(
            @Param("search") String search,
            @Param("status") RecordStatus status,
            Pageable pageable
    );


    /**
     * All status counters in ONE query.
     */
    @Query("""
        SELECT

            COUNT(m) AS totalMaterials,

            COALESCE(
                SUM(
                    CASE
                        WHEN m.status = :Active
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS active,

            COALESCE(
                SUM(
                    CASE
                        WHEN m.status = :Inactive
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS inactive

        FROM Material m
        """)
    MaterialSummaryProjection getMaterialSummary(

            @Param("Active")
            RecordStatus active,

            @Param("Inactive")
            RecordStatus inactive
    );


    boolean existsByMaterialCode(
            String materialCode
    );


    boolean existsByMaterialCodeAndMaterialIdNot(
            String materialCode,
            Integer materialId
    );


    boolean existsByMaterialName(
            String materialName
    );


    boolean existsByMaterialNameAndMaterialIdNot(
            String materialName,
            Integer materialId
    );
}