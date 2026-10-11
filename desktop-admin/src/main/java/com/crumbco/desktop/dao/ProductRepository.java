package com.crumbco.desktop.dao;

import com.crumbco.desktop.model.Product;
import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    Product create(String name, String category, String price, int stock);
}
