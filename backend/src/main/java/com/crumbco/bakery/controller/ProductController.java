package com.crumbco.bakery.controller;
import com.crumbco.bakery.model.Product; import com.crumbco.bakery.repository.ProductRepository;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal; import java.util.List;
@RestController @RequestMapping("/api/products")
public class ProductController {
 private final ProductRepository repo; public ProductController(ProductRepository repo){this.repo=repo;}
 @GetMapping public List<Product> list(){return repo.findByActiveTrueOrderByIdAsc();}
 @GetMapping("/{id}") public Product get(@PathVariable Long id){return repo.findById(id).filter(Product::isActive).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));}
 public record ProductRequest(@NotBlank @Size(max=120) String name,@NotBlank @Size(max=40) String category,@NotNull @DecimalMin("0.01") BigDecimal price,@NotBlank @Size(max=500) String description,@NotBlank @Size(max=12) String emoji,@NotNull @Min(0) Integer stock){}
 @PostMapping public Product create(@Valid @RequestBody ProductRequest r){return repo.save(new Product(r.name(),r.category(),r.price(),r.description(),r.emoji(),r.stock()));}
 @PutMapping("/{id}") public Product update(@PathVariable Long id,@Valid @RequestBody ProductRequest r){Product p=get(id);p.setName(r.name());p.setCategory(r.category());p.setPrice(r.price());p.setDescription(r.description());p.setEmoji(r.emoji());p.setStock(r.stock());return repo.save(p);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){Product p=repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));p.setActive(false);repo.save(p);}
}
