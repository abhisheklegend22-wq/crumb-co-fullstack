package com.crumbco.bakery.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="newsletter_subscribers", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class NewsletterSubscriber {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=160) private String email;
 @Column(nullable=false) private LocalDateTime subscribedAt=LocalDateTime.now();
 public NewsletterSubscriber(){} public NewsletterSubscriber(String email){this.email=email;}
 public Long getId(){return id;} public String getEmail(){return email;} public LocalDateTime getSubscribedAt(){return subscribedAt;}
}
