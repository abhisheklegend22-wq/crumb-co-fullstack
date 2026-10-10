package com.crumbco.bakery.repository;
import com.crumbco.bakery.model.NewsletterSubscriber; import org.springframework.data.jpa.repository.JpaRepository;
public interface NewsletterRepository extends JpaRepository<NewsletterSubscriber,Long> { boolean existsByEmailIgnoreCase(String email); }
