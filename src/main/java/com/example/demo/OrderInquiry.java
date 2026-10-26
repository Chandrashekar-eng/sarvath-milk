package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "order_inquiries")
public class OrderInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String email;
    private String phone;
    private String productName;
    private String message;

    public OrderInquiry() {}

    public OrderInquiry(String customerName, String email, String phone, String productName, String message) {
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.productName = productName;
        this.message = message;
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}