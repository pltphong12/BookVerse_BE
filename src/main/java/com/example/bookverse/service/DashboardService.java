package com.example.bookverse.service;

import com.example.bookverse.dto.criteria.CriteriaFilterDashboard;
import com.example.bookverse.dto.response.ResDashboardDTO;

public interface DashboardService {
    ResDashboardDTO getOverview(CriteriaFilterDashboard criteria) throws Exception;
}
