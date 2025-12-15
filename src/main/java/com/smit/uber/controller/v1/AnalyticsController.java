package com.smit.uber.controller.v1;

import com.smit.uber.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analytics;

    public AnalyticsController(AnalyticsService a) { this.analytics = a; }

    @GetMapping("/driver/{driver}/earnings")
    public Double earnings(@PathVariable String driver) {
        return analytics.totalEarnings(driver);
    }
}

