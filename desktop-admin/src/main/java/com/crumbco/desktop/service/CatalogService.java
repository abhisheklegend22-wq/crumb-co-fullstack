package com.crumbco.desktop.service;

import com.crumbco.desktop.dao.ProductRepository;
import com.crumbco.desktop.model.Product;
import java.util.List;

/** Service abstraction used by the Swing UI; synchronized refresh prevents overlapping refreshes in this client. */
public class CatalogService {
    private final ProductRepository repository;
    public CatalogService(ProductRepository repository) { this.repository = repository; }
    public synchronized List<Product> refresh() { return List.copyOf(repository.findAll()); }
    public Product add(String name, String category, String price, int stock) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name is required.");
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        if (new java.math.BigDecimal(price).signum() <= 0) throw new IllegalArgumentException("Price must be greater than zero.");
        return repository.create(name, category, price, stock);
    }
}
