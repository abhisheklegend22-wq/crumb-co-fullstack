package com.crumbco.bakery.controller;
import com.crumbco.bakery.dto.NewsletterRequest;
import com.crumbco.bakery.model.NewsletterSubscriber;
import com.crumbco.bakery.repository.NewsletterRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController{

 private final NewsletterRepository repo;

 public NewsletterController(NewsletterRepository repo){
  this.repo = repo;
 }

 @PostMapping
 @ResponseStatus(HttpStatus.CREATED)
 public Map<String,String> subscribe(@Valid @RequestBody NewsletterRequest request){
  String email=request.email().trim().toLowerCase();
  if(repo.existsByEmailIgnoreCase(email))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Email is already subscribed");
  repo.save(new NewsletterSubscriber(email));
  return Map.of("message","Successfully subscribed");
 }
}
