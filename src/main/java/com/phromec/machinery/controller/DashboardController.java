package com.phromec.machinery.controller;

import com.phromec.machinery.dto.dashboard.*;
import com.phromec.machinery.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // KPI cards
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewResponse> getOverview() {
        return ResponseEntity.ok(
                dashboardService.getOverview()
        );
    }

    // Total, pending, approved and rejected quotation cards
    @GetMapping("/quotation-summary")
    public ResponseEntity<QuotationSummaryResponse> getQuotationSummary() {
        return ResponseEntity.ok(
                dashboardService.getQuotationSummary()
        );
    }

    // Monthly quotation value bar chart
    @GetMapping("/monthly-quotation-value")
    public ResponseEntity<List<MonthlyQuotationValueResponse>>
    getMonthlyQuotationValue() {
        return ResponseEntity.ok(
                dashboardService.getMonthlyQuotationValue()
        );
    }

    // Quotation status donut chart
    @GetMapping("/quotation-status")
    public ResponseEntity<List<QuotationStatusResponse>>
    getQuotationStatus() {
        return ResponseEntity.ok(
                dashboardService.getQuotationStatus()
        );
    }

    // Monthly quotation trend line chart
    @GetMapping("/quotation-trend")
    public ResponseEntity<List<QuotationTrendResponse>>
    getQuotationTrend() {
        return ResponseEntity.ok(
                dashboardService.getQuotationTrend()
        );
    }

    // Machine type share bar chart
    @GetMapping("/machine-type-share")
    public ResponseEntity<List<MachineTypeShareResponse>>
    getMachineTypeShare() {
        return ResponseEntity.ok(
                dashboardService.getMachineTypeShare()
        );
    }
}
