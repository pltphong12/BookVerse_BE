package com.example.bookverse.dto.response;

import com.example.bookverse.dto.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResDashboardDTO {
    ResDashboardSummary summary;
    Map<OrderStatus, Long> orderStatusBreakdown;
    List<ResRevenuePoint> revenueSeries;
    List<ResTopProduct> topProducts;

    @Data
    public static class ResDashboardSummary {
        BigDecimal revenue;
        Long orders;
        Long customersNew;
        Long productsSold;
        BigDecimal aov;
        double cancelRate;
    }

    @Data
    public static class ResRevenuePoint {
        String label;
        BigDecimal revenue;
        Long orders;
    }

    @Data
    public static class ResTopProduct {
        Long productId;
        String title;
        Long soldQty;
        BigDecimal revenue;
    }
}
