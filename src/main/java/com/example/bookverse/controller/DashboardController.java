package com.example.bookverse.controller;

import com.example.bookverse.dto.criteria.CriteriaFilterDashboard;
import com.example.bookverse.dto.response.ResDashboardDTO;
import com.example.bookverse.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overview")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    public ResponseEntity<ResDashboardDTO> getOverview(@ModelAttribute CriteriaFilterDashboard criteria) throws Exception {
        return ResponseEntity.ok(dashboardService.getOverview(criteria));
    }
}
