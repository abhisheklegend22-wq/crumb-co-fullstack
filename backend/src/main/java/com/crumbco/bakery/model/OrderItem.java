package com.crumbco.bakery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="order_id",nullable=false) private CustomerOrder order;
    @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="product_id",nullable=false) private Product product;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal unitPrice;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal lineTotal;
    public OrderItem() {}
    public OrderItem(Product product,Integer quantity,BigDecimal unitPrice,BigDecimal lineTotal){this.product=product;this.quantity=quantity;this.unitPrice=unitPrice;this.lineTotal=lineTotal;}
    public void setOrder(CustomerOrder v){order=v;} public Long getId(){return id;} public Product getProduct(){return product;} public Integer getQuantity(){return quantity;}
    public BigDecimal getUnitPrice(){return unitPrice;} public BigDecimal getLineTotal(){return lineTotal;}
}
