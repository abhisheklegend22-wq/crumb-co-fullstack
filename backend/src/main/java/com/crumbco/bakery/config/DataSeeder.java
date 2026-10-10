package com.crumbco.bakery.config;
import com.crumbco.bakery.model.Product;import com.crumbco.bakery.repository.ProductRepository;import org.springframework.boot.CommandLineRunner;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import java.math.BigDecimal;
@Configuration public class DataSeeder {
 @Bean CommandLineRunner seedProducts(ProductRepository repo){return args->{if(repo.count()>0)return;
  repo.save(new Product("Butter Croissant","Pastries",new BigDecimal("129.00"),"Golden, flaky, all-butter layers.","🥐",40));
  repo.save(new Product("Chocolate Chunk Cookie","Cookies",new BigDecimal("99.00"),"Crisp edges, gooey chocolate middle.","🍪",60));
  repo.save(new Product("Strawberry Dream Cake","Cakes",new BigDecimal("699.00"),"Vanilla sponge, berries & soft cream.","🍰",8));
  repo.save(new Product("Sourdough Loaf","Bread",new BigDecimal("249.00"),"Crackly crust with a chewy heart.","🍞",20));
  repo.save(new Product("Cinnamon Morning Roll","Pastries",new BigDecimal("159.00"),"Warm cinnamon swirl, sweet glaze.","🥮",25));
  repo.save(new Product("Dark Chocolate Brownie","Cakes",new BigDecimal("149.00"),"Deep cocoa, shiny top, soft centre.","🍫",30));
  repo.save(new Product("Almond Biscotti","Cookies",new BigDecimal("179.00"),"Toasty almonds, perfect with coffee.","🥨",22));
  repo.save(new Product("Country Baguette","Bread",new BigDecimal("139.00"),"A crisp, rustic classic for your table.","🥖",18));
 };}
}
