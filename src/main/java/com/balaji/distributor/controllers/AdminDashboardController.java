package com.balaji.distributor.controllers;

import com.balaji.distributor.DTO.AdminDashboard;
import com.balaji.distributor.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService
            dashboardService;

    @GetMapping

    public AdminDashboard
    getDashboardData() {

        return dashboardService
                .getDashboardData();
    }
}
