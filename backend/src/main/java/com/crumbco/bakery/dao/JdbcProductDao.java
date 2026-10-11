package com.crumbco.bakery.dao;

import com.crumbco.bakery.dto.ProductStockRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/** Explicit JDBC DAO. SQL is parameterized and rows are mapped into typed Java objects. */
@Repository
public class JdbcProductDao implements GenericReadDao<ProductStockRow, Long> {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<ProductStockRow> MAPPER = (rs, rowNum) -> new ProductStockRow(
            rs.getLong("id"), rs.getString("name"), rs.getString("category"),
            rs.getBigDecimal("price"), rs.getInt("stock"), rs.getBoolean("active"));

    public JdbcProductDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public List<ProductStockRow> findAll() {
        return jdbcTemplate.query("SELECT id,name,category,price,stock,active FROM products ORDER BY id", MAPPER);
    }

    @Override
    public Optional<ProductStockRow> findById(Long id) {
        return jdbcTemplate.query("SELECT id,name,category,price,stock,active FROM products WHERE id = ?", MAPPER, id)
                .stream().findFirst();
    }

    public List<ProductStockRow> findLowStock(int threshold) {
        return jdbcTemplate.query("SELECT id,name,category,price,stock,active FROM products WHERE active = TRUE AND stock <= ? ORDER BY stock,id", MAPPER, threshold);
    }

    public int countActiveProducts() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products WHERE active = TRUE", Integer.class);
        return count == null ? 0 : count;
    }
}
