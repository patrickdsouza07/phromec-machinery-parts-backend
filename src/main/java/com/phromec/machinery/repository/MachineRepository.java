package com.phromec.machinery.repository;

import com.phromec.machinery.dto.machine.MachineListProjection;
import com.phromec.machinery.dto.machine.MachineSummaryProjection;
import com.phromec.machinery.dto.machine.MachineTypeCountProjection;
import com.phromec.machinery.model.machine.MachineModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MachineRepository extends JpaRepository<MachineModel, Integer> {

    @Query("""
        SELECT mt.machineTypeId AS machineTypeId, COUNT(mm.machineModelId) AS count
        FROM MachineModel mm JOIN mm.machineType mt
        GROUP BY mt.machineTypeId
        """)
    java.util.List<MachineTypeCountProjection> countMachinesByType();

    /*
     * ============================================================
     * MACHINE MODEL LIST
     * ============================================================
     */
    @Query(
            value = """
            SELECT
                mm.machineModelId AS machineId,
                mm.modelName AS machineName,
                mm.description AS description,

                mt.machineTypeId AS machineTypeId,
                mt.typeCode AS machineTypeCode,
                mt.typeName AS machineTypeName,

                mm.machineModelId AS machineModelId,
                mm.modelCode AS modelNo,
                mm.modelName AS modelName,

                mm.brandName AS manufacturer,
                mm.status AS status

            FROM MachineModel mm

            LEFT JOIN mm.machineType mt

            WHERE
                (
                    :search IS NULL
                    OR :search = ''

                    OR LOWER(mm.modelName)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mm.modelCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mm.brandName)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mm.description)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mt.typeName)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mt.typeCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )

                AND
                (
                    :machineTypeId IS NULL
                    OR mt.machineTypeId = :machineTypeId
                )

                AND
                (
                    :status IS NULL
                    OR LOWER(mm.status) = LOWER(:status)
                )
            """,

            countQuery = """
            SELECT COUNT(mm)

            FROM MachineModel mm

            LEFT JOIN mm.machineType mt

            WHERE
                (
                    :search IS NULL
                    OR :search = ''

                    OR LOWER(mm.modelName)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mm.modelCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mm.brandName)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mm.description)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mt.typeName)
                        LIKE LOWER(CONCAT('%', :search, '%'))

                    OR LOWER(mt.typeCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )

                AND
                (
                    :machineTypeId IS NULL
                    OR mt.machineTypeId = :machineTypeId
                )

                AND
                (
                    :status IS NULL
                    OR LOWER(mm.status) = LOWER(:status)
                )
            """
    )
    Page<MachineListProjection> searchMachines(
            @Param("search") String search,
            @Param("machineTypeId") Integer machineTypeId,
            @Param("status") String status,
            Pageable pageable
    );

    /*
     * ============================================================
     * SINGLE MACHINE MODEL
     * ============================================================
     */
    @Query("""
        SELECT mm
        FROM MachineModel mm
        LEFT JOIN FETCH mm.machineType
        WHERE mm.machineModelId = :machineModelId
        """)
    Optional<MachineModel> findMachineWithDetails(
            @Param("machineModelId") Integer machineModelId
    );

    /*
     * ============================================================
     * SUMMARY
     * ============================================================
     */
    @Query("""
        SELECT
            COUNT(mm.machineModelId) AS totalMachines,

            COALESCE(
                SUM(
                    CASE
                        WHEN LOWER(mm.status) = 'active'
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS activeMachines,

            COALESCE(
                SUM(
                    CASE
                        WHEN LOWER(mm.status) = 'inactive'
                        THEN 1
                        ELSE 0
                    END
                ),
                0
            ) AS inactiveMachines

        FROM MachineModel mm
        """)
    MachineSummaryProjection getMachineSummary();

    /*
     * ============================================================
     * DUPLICATE CHECKS
     * ============================================================
     */
    boolean existsByModelCodeIgnoreCase(String modelCode);

    boolean existsByModelCodeIgnoreCaseAndMachineModelIdNot(String modelCode, Integer machineModelId);

    boolean existsByModelNameIgnoreCase(String modelName);

    boolean existsByModelNameIgnoreCaseAndMachineModelIdNot(String modelName, Integer machineModelId);

    @Query("""
        SELECT COUNT(DISTINCT mt.machineTypeId)
        FROM MachineModel mm
        JOIN mm.machineType mt
        """)
    long countDistinctMachineTypes();
}
