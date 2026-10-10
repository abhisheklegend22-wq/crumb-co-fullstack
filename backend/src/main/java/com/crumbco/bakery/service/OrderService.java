package com.crumbco.bakery.service;

import com.crumbco.bakery.dto.OrderRequest;
import com.crumbco.bakery.model.*;
import com.crumbco.bakery.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@Service
public class OrderService {
 private final ProductRepository products; private final CustomerOrderRepository orders;
 public OrderService(ProductRepository products,CustomerOrderRepository orders){this.products=products;this.orders=orders;}
 @Transactional public CustomerOrder place(OrderRequest request){
  CustomerOrder order=new CustomerOrder(); order.setCustomerName(request.customerName().trim()); order.setEmail(request.email().trim().toLowerCase());
  order.setPhone(request.phone().trim());order.setAddress(request.address().trim());order.setCity(request.city().trim());
  BigDecimal subtotal=BigDecimal.ZERO;
  for(OrderRequest.OrderLineRequest line:request.items()){
   Product p=products.findById(line.productId()).filter(Product::isActive).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product not found: "+line.productId()));
   if(p.getStock()<line.quantity()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Insufficient stock for "+p.getName());
   BigDecimal lineTotal=p.getPrice().multiply(BigDecimal.valueOf(line.quantity())); subtotal=subtotal.add(lineTotal);
   p.setStock(p.getStock()-line.quantity()); order.addItem(new OrderItem(p,line.quantity(),p.getPrice(),lineTotal));
  }
  BigDecimal delivery=subtotal.compareTo(new BigDecimal("799.00"))>=0?BigDecimal.ZERO:new BigDecimal("49.00");
  order.setSubtotal(subtotal);order.setDeliveryFee(delivery);order.setTotal(subtotal.add(delivery));
  return orders.save(order);
 }
}
