package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DairyController {

    private final ProductRepository productRepo;
    private final SiteSettingsRepository settingsRepo;
    private final OrderInquiryRepository inquiryRepo;
    private final NotificationService notificationService;

    public DairyController(ProductRepository productRepo, SiteSettingsRepository settingsRepo, 
                           OrderInquiryRepository inquiryRepo, 
                           NotificationService notificationService) {
        this.productRepo = productRepo;
        this.settingsRepo = settingsRepo;
        this.inquiryRepo = inquiryRepo;
        this.notificationService = notificationService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepo.findAll().stream()
            .filter(Product::isActive)
            .sorted(java.util.Comparator.comparing((Product p) -> p.isFeatured()).reversed()
                .thenComparing(p -> p.getSortOrder() == null ? 0 : p.getSortOrder()))
            .toList();
    }

    @GetMapping("/settings")
    public SiteSettings getSettings() {
        return settingsRepo.findById(1L).orElseGet(() -> settingsRepo.save(new SiteSettings()));
    }

    @PostMapping("/inquiry")
    public ResponseEntity<String> submitInquiry(@RequestBody OrderInquiry inquiry) {
        // 1. Save the order details into the database
        OrderInquiry savedOrder = inquiryRepo.save(inquiry);
        
        // 2. Automatically dispatch the confirmation email to the customer
        notificationService.sendOrderNotifications(savedOrder);
        
        // 🔥 NEW: Automatically dispatch a separate alert notification to the admin team
        notificationService.sendAdminAlertNotification(savedOrder);
        
        // 3. Return a success response to your Frontend
        return ResponseEntity.ok("Order #" + savedOrder.getId() + " placed successfully! Confirmation email sent to " + savedOrder.getEmail());
    }
}