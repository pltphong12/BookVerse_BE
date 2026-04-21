package com.example.bookverse.service.impl;

import com.example.bookverse.dto.criteria.CriteriaFilterDashboard;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.response.ResDashboardDTO;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.CustomerRepository;
import com.example.bookverse.repository.OrderDetailRepository;
import com.example.bookverse.repository.OrderRepository;
import com.example.bookverse.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final int DEFAULT_RANGE_DAYS = 30;
    private static final int DEFAULT_TOP_N = 5;
    private static final int MAX_TOP_N = 20;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;

    public DashboardServiceImpl(OrderRepository orderRepository,
                                OrderDetailRepository orderDetailRepository,
                                CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResDashboardDTO getOverview(CriteriaFilterDashboard criteria) throws Exception {
        DateRange dateRange = normalizeDateRange(criteria);
        int topN = normalizeTopN(criteria);
        String groupPattern = resolveGroupPattern(criteria);

        Instant from = toStartOfDay(dateRange.fromDate());
        Instant to = toEndOfDay(dateRange.toDate());

        Double revenueRaw = orderRepository.sumRevenueByCreatedAtBetween(from, to);
        long totalOrders = orderRepository.countByCreatedAtBetween(from, to);
        long cancelledOrders = orderRepository.countByStatusAndCreatedAtBetween(OrderStatus.CANCELLED, from, to);
        long customersNew = customerRepository.countByCreatedAtBetween(from, to);
        Long productsSoldRaw = orderDetailRepository.sumSoldQuantityBetween(from, to);

        BigDecimal revenue = toMoney(revenueRaw);
        long nonCancelledOrders = Math.max(0, totalOrders - cancelledOrders);
        long productsSold = productsSoldRaw == null ? 0 : productsSoldRaw;
        BigDecimal aov = nonCancelledOrders == 0
                ? BigDecimal.ZERO
                : revenue.divide(BigDecimal.valueOf(nonCancelledOrders), 2, RoundingMode.HALF_UP);
        double cancelRate = totalOrders == 0
                ? 0D
                : BigDecimal.valueOf(cancelledOrders * 100.0 / totalOrders)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        ResDashboardDTO.ResDashboardSummary summary = new ResDashboardDTO.ResDashboardSummary();
        summary.setRevenue(revenue);
        summary.setOrders(totalOrders);
        summary.setCustomersNew(customersNew);
        summary.setProductsSold(productsSold);
        summary.setAov(aov);
        summary.setCancelRate(cancelRate);

        Map<OrderStatus, Long> statusBreakdown = new EnumMap<>(OrderStatus.class);
        for (Object[] row : orderRepository.countOrderByStatusBetween(from, to)) {
            if (row == null || row.length < 2) {
                continue;
            }
            OrderStatus status = (OrderStatus) row[0];
            long value = ((Number) row[1]).longValue();
            statusBreakdown.put(status, value);
        }

        List<ResDashboardDTO.ResRevenuePoint> revenueSeries = new ArrayList<>();
        for (Object[] row : orderRepository.getRevenueSeries(from, to, groupPattern)) {
            if (row == null || row.length < 3) {
                continue;
            }
            ResDashboardDTO.ResRevenuePoint point = new ResDashboardDTO.ResRevenuePoint();
            point.setLabel(String.valueOf(row[0]));
            point.setRevenue(toMoney(row[1]));
            point.setOrders(((Number) row[2]).longValue());
            revenueSeries.add(point);
        }

        List<ResDashboardDTO.ResTopProduct> topProducts = new ArrayList<>();
        for (Object[] row : orderDetailRepository.findTopProducts(from, to, topN)) {
            if (row == null || row.length < 4) {
                continue;
            }
            ResDashboardDTO.ResTopProduct product = new ResDashboardDTO.ResTopProduct();
            product.setProductId(((Number) row[0]).longValue());
            product.setTitle(String.valueOf(row[1]));
            product.setSoldQty(((Number) row[2]).longValue());
            product.setRevenue(toMoney(row[3]));
            topProducts.add(product);
        }

        ResDashboardDTO result = new ResDashboardDTO();
        result.setSummary(summary);
        result.setOrderStatusBreakdown(statusBreakdown);
        result.setRevenueSeries(revenueSeries);
        result.setTopProducts(topProducts);
        return result;
    }

    private DateRange normalizeDateRange(CriteriaFilterDashboard criteria) throws IdInvalidException {
        LocalDate today = LocalDate.now();
        LocalDate from = criteria != null ? criteria.getFromDate() : null;
        LocalDate to = criteria != null ? criteria.getToDate() : null;

        if (from == null && to == null) {
            to = today;
            from = to.minusDays(DEFAULT_RANGE_DAYS - 1L);
        } else if (from == null) {
            from = to.minusDays(DEFAULT_RANGE_DAYS - 1L);
        } else if (to == null) {
            to = from.plusDays(DEFAULT_RANGE_DAYS - 1L);
        }

        if (from.isAfter(to)) {
            throw new IdInvalidException("fromDate không được lớn hơn toDate");
        }

        return new DateRange(from, to);
    }

    private int normalizeTopN(CriteriaFilterDashboard criteria) throws IdInvalidException {
        int topN = (criteria == null || criteria.getTopN() == null) ? DEFAULT_TOP_N : criteria.getTopN();
        if (topN <= 0) {
            throw new IdInvalidException("topN phải lớn hơn 0");
        }
        return Math.min(topN, MAX_TOP_N);
    }

    private String resolveGroupPattern(CriteriaFilterDashboard criteria) throws IdInvalidException {
        String groupBy = criteria == null || criteria.getGroupBy() == null ? "day" : criteria.getGroupBy().trim().toLowerCase();
        return switch (groupBy) {
            case "day" -> "%Y-%m-%d";
            case "week" -> "%x-W%v";
            case "month" -> "%Y-%m";
            default -> throw new IdInvalidException("groupBy chỉ hỗ trợ: day, week, month");
        };
    }

    private Instant toStartOfDay(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private Instant toEndOfDay(LocalDate date) {
        return date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
    }

    private BigDecimal toMoney(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal.setScale(2, RoundingMode.HALF_UP);
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue()).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private record DateRange(LocalDate fromDate, LocalDate toDate) {
    }
}
