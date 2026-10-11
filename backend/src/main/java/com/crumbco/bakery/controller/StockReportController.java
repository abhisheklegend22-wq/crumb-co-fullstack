package com.crumbco.bakery.controller;

import com.crumbco.bakery.report.StockReport;
import com.crumbco.bakery.service.StockReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/reports")
public class StockReportController {
    private final StockReportService service;
    public StockReportController(StockReportService service) { this.service = service; }
    @GetMapping("/stock") public CompletableFuture<StockReport> stock() { return service.generate(); }
}
