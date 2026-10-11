package com.crumbco.bakery.service;

import com.crumbco.bakery.dao.JdbcProductDao;
import com.crumbco.bakery.report.StockReport;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/** Runs the inventory report on Spring's background executor instead of blocking the request thread. */
@Service
public class StockReportService {
    private static final int LOW_STOCK_THRESHOLD = 5;
    private final JdbcProductDao dao;
    public StockReportService(JdbcProductDao dao) { this.dao = dao; }

    @Async("bakeryExecutor")
    public CompletableFuture<StockReport> generate() {
        StockReport report = new StockReport(dao.countActiveProducts(), LOW_STOCK_THRESHOLD,
                dao.findLowStock(LOW_STOCK_THRESHOLD), Instant.now());
        return CompletableFuture.completedFuture(report);
    }
}
