package com.crumbco.bakery.controller;
import com.crumbco.bakery.dto.OrderRequest; import com.crumbco.bakery.model.CustomerOrder; import com.crumbco.bakery.repository.CustomerOrderRepository; import com.crumbco.bakery.service.OrderService;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/orders")
public class OrderController {
 private final OrderService service;private final CustomerOrderRepository repo;public OrderController(OrderService service,CustomerOrderRepository repo){this.service=service;this.repo=repo;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public CustomerOrder place(@Valid @RequestBody OrderRequest request){return service.place(request);}
 @GetMapping("/{id}") public CustomerOrder get(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));}
 @GetMapping public java.util.List<CustomerOrder> list(){return repo.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC,"createdAt"));}
}
