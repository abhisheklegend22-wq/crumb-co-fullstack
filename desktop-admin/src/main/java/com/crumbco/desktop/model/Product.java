package com.crumbco.desktop.model;

import java.math.BigDecimal;

public record Product(long id, String name, String category, BigDecimal price, int stock, boolean active) { }
