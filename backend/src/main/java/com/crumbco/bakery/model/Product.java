package com.crumbco.bakery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=120) private String name;
    @Column(nullable=false, length=40) private String category;
    @Column(nullable=false, precision=10, scale=2) private BigDecimal price;
    @Column(nullable=false, length=500) private String description;
    @Column(nullable=false, length=12) private String emoji;
    @Column(nullable=false) private Integer stock;
    @Column(nullable=false) private boolean active = true;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    public Product() {}
    public Product(String name,String category,BigDecimal price,String description,String emoji,Integer stock) { this.name=name;this.category=category;this.price=price;this.description=description;this.emoji=emoji;this.stock=stock;this.active=true;this.createdAt=LocalDateTime.now(); }
    public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getEmoji(){return emoji;} public void setEmoji(String v){emoji=v;}
    public Integer getStock(){return stock;} public void setStock(Integer v){stock=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
