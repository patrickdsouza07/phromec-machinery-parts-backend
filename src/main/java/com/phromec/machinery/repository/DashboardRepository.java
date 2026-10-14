package com.phromec.machinery.repository;

import com.phromec.machinery.dto.dashboard.*;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    // 1. Top KPI cards
    public DashboardOverviewResponse getOverview() {
        String sql = """
            SELECT
                (SELECT COUNT(*) FROM customers) AS total_customers,
                (SELECT COUNT(*) FROM machine_models) AS total_machines,
                (SELECT COUNT(*) FROM parts WHERE status = 'Active') AS total_parts,
                (SELECT COALESCE(SUM(total_amount), 0)
                   FROM quotations) AS quotation_value
            """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new DashboardOverviewResponse(
                        rs.getLong("total_customers"),
                        rs.getLong("total_machines"),
                        rs.getLong("total_parts"),
                        rs.getBigDecimal("quotation_value")
                )
        );
    }

    // 2. Quotation summary cards
    public QuotationSummaryResponse getQuotationSummary() {
        String sql = """
            SELECT
                COUNT(*) AS total_quotations,
                SUM(CASE WHEN status IN
                    ('Draft', 'Sent', 'Under Review')
                    THEN 1 ELSE 0 END) AS pending_quotations,
                SUM(CASE WHEN status = 'Approved'
                    THEN 1 ELSE 0 END) AS approved_quotations,
                SUM(CASE WHEN status = 'Rejected'
                    THEN 1 ELSE 0 END) AS rejected_quotations
            FROM quotations
            """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new QuotationSummaryResponse(
                        rs.getLong("total_quotations"),
                        rs.getLong("pending_quotations"),
                        rs.getLong("approved_quotations"),
                        rs.getLong("rejected_quotations")
                )
        );
    }

    // 3. Monthly quotation values
    public Map<String, BigDecimal> getMonthlyQuotationValues(
            YearMonth startMonth,
            YearMonth endMonth
    ) {
        String sql = """
            SELECT
                DATE_FORMAT(quotation_date, '%Y-%m') AS quotation_month,
                COALESCE(SUM(total_amount), 0) AS quotation_value
            FROM quotations
            WHERE quotation_date >= ?
              AND quotation_date < ?
            GROUP BY DATE_FORMAT(quotation_date, '%Y-%m')
            ORDER BY quotation_month
            """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                sql,
                startMonth.atDay(1),
                endMonth.plusMonths(1).atDay(1)
        );

        return rows.stream().collect(Collectors.toMap(
                row -> (String) row.get("quotation_month"),
                row -> (BigDecimal) row.get("quotation_value")
        ));
    }

    // 4. Quotation status donut chart
    public List<QuotationStatusResponse> getQuotationStatus() {
        String sql = """
            SELECT status, COUNT(*) AS quotation_count
            FROM quotations
            GROUP BY status
            ORDER BY status
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new QuotationStatusResponse(
                        rs.getString("status"),
                        rs.getLong("quotation_count")
                )
        );
    }

    // 5. Monthly quotation count
    public Map<String, Long> getMonthlyQuotationCounts(
            YearMonth startMonth,
            YearMonth endMonth
    ) {
        String sql = """
            SELECT
                DATE_FORMAT(quotation_date, '%Y-%m') AS quotation_month,
                COUNT(*) AS quotation_count
            FROM quotations
            WHERE quotation_date >= ?
              AND quotation_date < ?
            GROUP BY DATE_FORMAT(quotation_date, '%Y-%m')
            ORDER BY quotation_month
            """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                sql,
                startMonth.atDay(1),
                endMonth.plusMonths(1).atDay(1)
        );

        return rows.stream().collect(Collectors.toMap(
                row -> (String) row.get("quotation_month"),
                row -> ((Number) row.get("quotation_count")).longValue()
        ));
    }

    // 6. Quotation amount share by machine type
    public List<MachineTypeShareResponse> getMachineTypeShare() {
        String sql = """
            SELECT
                mt.type_name AS machine_type,
                ROUND(
                    100.0 * SUM(qi.line_total) /
                    NULLIF((
                        SELECT SUM(qi2.line_total)
                        FROM quotation_items qi2
                        JOIN part_variants pv2
                            ON pv2.variant_id = qi2.variant_id
                        JOIN parts p2
                            ON p2.part_id = pv2.part_id
                        WHERE p2.machine_type_id IS NOT NULL
                    ), 0),
                    2
                ) AS share_percent
            FROM quotation_items qi
            JOIN part_variants pv
                ON pv.variant_id = qi.variant_id
            JOIN parts p
                ON p.part_id = pv.part_id
            JOIN machine_types mt
                ON mt.machine_type_id = p.machine_type_id
            GROUP BY mt.machine_type_id, mt.type_name
            ORDER BY share_percent DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MachineTypeShareResponse(
                        rs.getString("machine_type"),
                        rs.getBigDecimal("share_percent")
                )
        );
    }
}
