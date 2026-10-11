package com.crumbco.desktop.dao;

import com.crumbco.desktop.exception.DatabaseUnavailableException;
import com.crumbco.desktop.model.Product;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC DAO using try-with-resources and PreparedStatement to prevent resource leaks and SQL injection. */
public class JdbcProductRepository implements ProductRepository {
    private final String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/crumb_co_bakery?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private final String user = System.getenv().getOrDefault("DB_USERNAME", "root");
    private final String password = System.getenv().getOrDefault("DB_PASSWORD", "root");

    private Connection connect() throws SQLException { return DriverManager.getConnection(url, user, password); }

    @Override public List<Product> findAll() {
        String sql = "SELECT id,name,category,price,stock,active FROM products ORDER BY id";
        List<Product> products = new ArrayList<>();
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            while (rs.next()) products.add(map(rs));
            return products;
        } catch (SQLException ex) {
            throw new DatabaseUnavailableException("Could not read products. Start MySQL and verify DB_USERNAME/DB_PASSWORD.", ex);
        }
    }

    @Override public Product create(String name, String category, String price, int stock) {
        String sql = "INSERT INTO products(name,category,price,description,emoji,stock,active,created_at) VALUES(?,?,?,?,?,?,TRUE,NOW())";
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name.trim()); statement.setString(2, category); statement.setBigDecimal(3, new BigDecimal(price));
            statement.setString(4, "Added from Java Swing desktop admin"); statement.setString(5, "🧁"); statement.setInt(6, stock);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) return new Product(keys.getLong(1), name.trim(), category, new BigDecimal(price), stock, true);
            }
            throw new SQLException("Insert completed but no generated key was returned");
        } catch (SQLException | NumberFormatException ex) {
            throw new DatabaseUnavailableException("Could not create product: " + ex.getMessage(), ex);
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        return new Product(rs.getLong("id"), rs.getString("name"), rs.getString("category"), rs.getBigDecimal("price"), rs.getInt("stock"), rs.getBoolean("active"));
    }
}
