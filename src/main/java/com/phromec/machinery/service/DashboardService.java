package com.phromec.machinery.service;

import com.phromec.machinery.dto.dashboard.*;

import java.util.List;

public interface DashboardService {

    DashboardOverviewResponse getOverview();

    QuotationSummaryResponse getQuotationSummary();

    List<MonthlyQuotationValueResponse> getMonthlyQuotationValue();

    List<QuotationStatusResponse> getQuotationStatus();

    List<QuotationTrendResponse> getQuotationTrend();

    List<MachineTypeShareResponse> getMachineTypeShare();
}