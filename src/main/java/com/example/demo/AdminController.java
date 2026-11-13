package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final ProductRepository products;
    private final SiteSettingsRepository settings;
    private final CustomerOrderRepository orders;
    private final JavaMailSender mailSender;
    private final String adminEmail;
    private final Map<String, CodeEntry> codes = new ConcurrentHashMap<>();
    private final Map<String, Instant> sessions = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    private record CodeEntry(String code, Instant expiresAt) {}
    public AdminController(ProductRepository products, SiteSettingsRepository settings, CustomerOrderRepository orders, JavaMailSender mailSender,
        @Value("${sarvath.admin.email:sarvathdairy@gmail.com}") String adminEmail) {
        this.products=products; this.settings=settings; this.orders=orders; this.mailSender=mailSender; this.adminEmail=adminEmail;
    }

    @PostMapping("/request-code")
    public ResponseEntity<?> requestCode(@RequestBody Map<String,String> body) {
        if (body == null || !adminEmail.equalsIgnoreCase(body.getOrDefault("email","").trim()))
            return ResponseEntity.status(401).body(Map.of("message","Admin account not recognized."));
        String code=String.format("%06d", random.nextInt(1_000_000));
        try {
            SimpleMailMessage mail=new SimpleMailMessage();
            mail.setTo(adminEmail); mail.setSubject("Sarvath Admin sign-in code");
            mail.setText("Your Sarvath admin sign-in code is "+code+". It expires in 10 minutes. If you did not request it, ignore this email.");
            mailSender.send(mail);
            codes.put(adminEmail,new CodeEntry(code,Instant.now().plusSeconds(600)));
            return ResponseEntity.ok(Map.of("message","A one-time sign-in code has been sent to the admin email."));
        } catch(Exception ex) {
            return ResponseEntity.status(503).body(Map.of("message","Email delivery is not configured. Please configure MAIL_USERNAME and MAIL_PASSWORD."));
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String,String> body) {
        CodeEntry entry=codes.get(adminEmail);
        String supplied=body == null ? "" : body.getOrDefault("code","");
        if(entry==null || Instant.now().isAfter(entry.expiresAt()) || !entry.code().equals(supplied))
            return ResponseEntity.status(401).body(Map.of("message","Invalid or expired sign-in code."));
        codes.remove(adminEmail);
        String token=UUID.randomUUID().toString();
        sessions.put(token,Instant.now().plusSeconds(8*60*60));
        return ResponseEntity.ok(Map.of("token",token,"expiresIn",28800));
    }

    @GetMapping("/products")
    public ResponseEntity<?> adminProducts(@RequestHeader(value="X-Admin-Token",required=false) String token) {
        if(!valid(token)) return ResponseEntity.status(401).body(Map.of("message","Admin session expired."));
        return ResponseEntity.ok(products.findAll());
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestHeader(value="X-Admin-Token",required=false) String token,@RequestBody Product p) {
        if(!valid(token)) return ResponseEntity.status(401).body(Map.of("message","Admin session expired."));
        p.setId(null);
        return ResponseEntity.ok(products.save(p));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@RequestHeader(value="X-Admin-Token",required=false) String token,@PathVariable Long id,@RequestBody Product p) {
        if(!valid(token)) return ResponseEntity.status(401).body(Map.of("message","Admin session expired."));
        Optional<Product> found = products.findById(id);
        if (found.isEmpty()) return ResponseEntity.notFound().build();
        Product existing = found.get();
        existing.setName(p.getName()); existing.setCategory(p.getCategory()); existing.setPrice(p.getPrice());
        existing.setVolume(p.getVolume()); existing.setDescription(p.getDescription()); existing.setImageUrl(p.getImageUrl());
        existing.setImageUrls(p.getImageUrls()); existing.setFeatured(p.isFeatured()); existing.setActive(p.isActive());
        existing.setSortOrder(p.getSortOrder());
        return ResponseEntity.ok(products.save(existing));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@RequestHeader(value="X-Admin-Token",required=false) String token,@PathVariable Long id) {
        if(!valid(token)) return ResponseEntity.status(401).body(Map.of("message","Admin session expired."));
        if(!products.existsById(id)) return ResponseEntity.notFound().build();
        products.deleteById(id); return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value="X-Admin-Token",required=false) String token) {
        if(token!=null) sessions.remove(token);
        return ResponseEntity.ok(Map.of("message","Signed out."));
    }





    @GetMapping("/orders")
    public ResponseEntity<?> adminOrders(@RequestHeader(value="X-Admin-Token",required=false) String token) {
        if(!valid(token)) return ResponseEntity.status(401).body(Map.of("message","Admin session expired."));
        return ResponseEntity.ok(orders.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC,"createdAt")));
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@RequestHeader(value="X-Admin-Token",required=false) String token,
        @PathVariable String orderId, @RequestBody Map<String,String> body) {
        if(!valid(token)) return ResponseEntity.status(401).body(Map.of("message","Admin session expired."));
        String status=body==null?"":body.getOrDefault("status","").trim().toUpperCase();
        if(!List.of("RECEIVED","CONFIRMED","PREPARING","OUT_FOR_DELIVERY","DELIVERED","CANCELLED").contains(status))
            return ResponseEntity.badRequest().body(Map.of("message","Invalid order status."));
        Optional<CustomerOrder> found=orders.findByOrderId(orderId);
        if(found.isEmpty()) return ResponseEntity.notFound().build();
        CustomerOrder order=found.get(); order.setStatus(status); orders.save(order);
        return ResponseEntity.ok(Map.of("status",status,"orderId",orderId));
    }

    @GetMapping("/settings")
    public SiteSettings readSettings(@RequestHeader(value="X-Admin-Token",required=false) String token) {
        if(!valid(token)) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return settings.findById(1L).orElseGet(() -> settings.save(new SiteSettings()));
    }

    @PutMapping("/settings")
    public ResponseEntity<?> saveSettings(@RequestHeader(value="X-Admin-Token",required=false) String token,@RequestBody SiteSettings input) {
        if(!valid(token)) return ResponseEntity.status(401).body(Map.of("message","Admin session expired."));
        SiteSettings current=settings.findById(1L).orElseGet(() -> new SiteSettings());
        current.setPhone(input.getPhone()); current.setEmail(input.getEmail()); current.setLocation(input.getLocation());
        current.setYoutubeUrl(input.getYoutubeUrl()); current.setWhatsappUrl(input.getWhatsappUrl()); current.setAppLink(input.getAppLink());
        current.setFounderName(input.getFounderName()); current.setFounderPhotoUrl(input.getFounderPhotoUrl()); current.setLogoUrl(input.getLogoUrl());
        return ResponseEntity.ok(settings.save(current));
    }

    private boolean valid(String token) {
        if(token==null) return false;
        Instant expiry=sessions.get(token);
        if(expiry==null || Instant.now().isAfter(expiry)){sessions.remove(token);return false;}
        return true;
    }
}
