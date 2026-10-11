package com.crumbco.bakery.report;

import com.crumbco.bakery.dto.ProductStockRow;
import java.time.Instant;
import java.util.List;

public record StockReport(int activeProductCount, int lowStockThreshold, List<ProductStockRow> lowStockProducts, Instant generatedAt) { }
