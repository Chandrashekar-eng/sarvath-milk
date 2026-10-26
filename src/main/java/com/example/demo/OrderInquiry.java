package com.example.demo;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_inquiries")
public class OrderInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String phone;
    private String address;
    private String productName;
    private Integer dailyQuantityLiters;
    private LocalDateTime createdAt = LocalDateTime.now();

    public OrderInquiry() {}

    // Getters and Setters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getDailyQuantityLiters() { return dailyQuantityLiters; }
    public void setDailyQuantityLiters(Integer dailyQuantityLiters) { this.dailyQuantityLiters = dailyQuantityLiters; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
