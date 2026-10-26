package com.example.demo;

import com.example.demo.Product;
import com.example.demo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Product("Sarvath Pure Farm Milk (A2)", "Milk", 70.0, "1 Litre", 
                    "Fresh, unprocessed organic cow milk delivered within 4 hours of milking.", 
                    "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=600&q=80"));
                
                repo.save(new Product("Sarvath Desi Danedar Ghee", "Ghee", 650.0, "500 ml", 
                    "Traditional bilona churned ghee from grass-fed cows.", 
                    "https://images.unsplash.com/photo-1631451095765-2c91616fc9e6?auto=format&fit=crop&w=600&q=80"));

                repo.save(new Product("Sarvath Malai Paneer", "Dairy", 120.0, "200 gm", 
                    "Ultra-soft, protein-rich cottage cheese made from fresh whole milk.", 
                    "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?auto=format&fit=crop&w=600&q=80"));

                repo.save(new Product("Sarvath Set Dahi (Curd)", "Curd", 45.0, "400 gm", 
                    "Naturally cultured thick artisan curd without any artificial thickeners.", 
                    "https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&w=600&q=80"));
            }
        };
    }
}