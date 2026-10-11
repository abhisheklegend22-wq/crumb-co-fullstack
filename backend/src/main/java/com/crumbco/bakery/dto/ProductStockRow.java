package com.crumbco.bakery.dto;

import java.math.BigDecimal;

/** Lightweight JDBC projection used by reports and the legacy servlet. */
public record ProductStockRow(Long id, String name, String category, BigDecimal price, Integer stock, boolean active) { }
