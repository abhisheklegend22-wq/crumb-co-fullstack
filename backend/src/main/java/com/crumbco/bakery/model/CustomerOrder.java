package com.crumbco.bakery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="customer_orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=120) private String customerName;
    @Column(nullable=false,length=160) private String email;
    @Column(nullable=false,length=20) private String phone;
    @Column(nullable=false,length=500) private String address;
    @Column(nullable=false,length=80) private String city;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal subtotal;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal deliveryFee;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal total;
    @Column(nullable=false,length=30) private String status="PLACED";
    @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
    private List<OrderItem> items=new ArrayList<>();
    public CustomerOrder() {}
    public void addItem(OrderItem item){items.add(item);item.setOrder(this);}
    public Long getId(){return id;} public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;} public String getCity(){return city;} public void setCity(String v){city=v;}
    public BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(BigDecimal v){subtotal=v;} public BigDecimal getDeliveryFee(){return deliveryFee;} public void setDeliveryFee(BigDecimal v){deliveryFee=v;}
    public BigDecimal getTotal(){return total;} public void setTotal(BigDecimal v){total=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public List<OrderItem> getItems(){return items;}
}
