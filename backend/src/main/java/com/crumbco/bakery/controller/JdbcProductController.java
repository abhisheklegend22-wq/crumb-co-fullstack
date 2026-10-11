package com.crumbco.bakery.controller;

import com.crumbco.bakery.dao.JdbcProductDao;
import com.crumbco.bakery.dto.ProductStockRow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

/** Demonstrates direct JDBC-backed web endpoints alongside the JPA CRUD endpoints. */
@RestController
@RequestMapping("/api/jdbc/products")
public class JdbcProductController {
    private final JdbcProductDao dao;
    public JdbcProductController(JdbcProductDao dao) { this.dao = dao; }

    @GetMapping public List<ProductStockRow> list() { return dao.findAll(); }
    @GetMapping("/{id}") public ProductStockRow get(@PathVariable Long id) {
        return dao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}
