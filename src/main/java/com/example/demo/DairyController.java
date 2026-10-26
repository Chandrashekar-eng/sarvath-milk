package com.example.demo;


import com.example.demo.OrderInquiry;
import com.example.demo.Product;
import com.example.demo.OrderInquiryRepository;
import com.example.demo.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DairyController {

    private final ProductRepository productRepo;
    private final OrderInquiryRepository inquiryRepo;

    public DairyController(ProductRepository productRepo, OrderInquiryRepository inquiryRepo) {
        this.productRepo = productRepo;
        this.inquiryRepo = inquiryRepo;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @PostMapping("/inquiry")
    public ResponseEntity<String> submitInquiry(@RequestBody OrderInquiry inquiry) {
        inquiryRepo.save(inquiry);
        return ResponseEntity.ok("Your inquiry has been received! Sarvath team will contact you shortly.");
    }
}

