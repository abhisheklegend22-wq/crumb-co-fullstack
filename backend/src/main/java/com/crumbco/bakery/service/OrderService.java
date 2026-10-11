package com.crumbco.bakery.service;

import com.crumbco.bakery.dto.OrderRequest;
import com.crumbco.bakery.model.*;
import com.crumbco.bakery.repository.*;
import com.crumbco.bakery.exception.InsufficientStockException;
import com.crumbco.bakery.pricing.DeliveryFeePolicy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@Service
public class OrderService implements OrderProcessingService {
 private final ProductRepository products; private final CustomerOrderRepository orders; private final DeliveryFeePolicy deliveryFeePolicy;
 public OrderService(ProductRepository products,CustomerOrderRepository orders,DeliveryFeePolicy deliveryFeePolicy){this.products=products;this.orders=orders;this.deliveryFeePolicy=deliveryFeePolicy;}
 @Override
 @Transactional
 public synchronized CustomerOrder place(OrderRequest request){
  CustomerOrder order=new CustomerOrder(); order.setCustomerName(request.customerName().trim()); order.setEmail(request.email().trim().toLowerCase());
  order.setPhone(request.phone().trim());order.setAddress(request.address().trim());order.setCity(request.city().trim());
  BigDecimal subtotal=BigDecimal.ZERO;
  for(OrderRequest.OrderLineRequest line:request.items()){
   Product p=products.findById(line.productId()).filter(Product::isActive).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product not found: "+line.productId()));
   if(p.getStock()<line.quantity()) throw new InsufficientStockException(p.getName(), line.quantity(), p.getStock());
   BigDecimal lineTotal=p.getPrice().multiply(BigDecimal.valueOf(line.quantity())); subtotal=subtotal.add(lineTotal);
   p.setStock(p.getStock()-line.quantity()); order.addItem(new OrderItem(p,line.quantity(),p.getPrice(),lineTotal));
  }
  BigDecimal delivery=deliveryFeePolicy.calculate(subtotal);
  order.setSubtotal(subtotal);order.setDeliveryFee(delivery);order.setTotal(subtotal.add(delivery));
  return orders.save(order);
 }
}
