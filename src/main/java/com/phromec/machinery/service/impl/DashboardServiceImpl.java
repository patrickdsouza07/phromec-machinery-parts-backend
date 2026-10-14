package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.dashboard.*;
import com.phromec.machinery.repository.DashboardRepository;
import com.phromec.machinery.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    private static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("MMM");

    private static final DateTimeFormatter YEAR_MONTH_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public DashboardOverviewResponse getOverview() {
        return dashboardRepository.getOverview();
    }

    @Override
    public QuotationSummaryResponse getQuotationSummary() {
        return dashboardRepository.getQuotationSummary();
    }

    @Override
    public List<MonthlyQuotationValueResponse> getMonthlyQuotationValue() {
        YearMonth endMonth = YearMonth.now();
        YearMonth startMonth = endMonth.minusMonths(5);

        Map<String, BigDecimal> values =
                dashboardRepository.getMonthlyQuotationValues(
                        startMonth,
                        endMonth
                );

        List<MonthlyQuotationValueResponse> result = new ArrayList<>();

        for (YearMonth month = startMonth;
             !month.isAfter(endMonth);
             month = month.plusMonths(1)) {

            String key = month.format(YEAR_MONTH_FORMAT);

            result.add(new MonthlyQuotationValueResponse(
                    month.format(MONTH_FORMAT),
                    key,
                    values.getOrDefault(key, BigDecimal.ZERO)
            ));
        }

        return result;
    }

    @Override
    public List<QuotationStatusResponse> getQuotationStatus() {
        return dashboardRepository.getQuotationStatus();
    }

    @Override
    public List<QuotationTrendResponse> getQuotationTrend() {
        YearMonth endMonth = YearMonth.now();
        YearMonth startMonth = endMonth.minusMonths(5);

        Map<String, Long> counts =
                dashboardRepository.getMonthlyQuotationCounts(
                        startMonth,
                        endMonth
                );

        List<QuotationTrendResponse> result = new ArrayList<>();

        for (YearMonth month = startMonth;
             !month.isAfter(endMonth);
             month = month.plusMonths(1)) {

            String key = month.format(YEAR_MONTH_FORMAT);

            result.add(new QuotationTrendResponse(
                    month.format(MONTH_FORMAT),
                    key,
                    counts.getOrDefault(key, 0L)
            ));
        }

        return result;
    }

    @Override
    public List<MachineTypeShareResponse> getMachineTypeShare() {
        return dashboardRepository.getMachineTypeShare();
    }
}
