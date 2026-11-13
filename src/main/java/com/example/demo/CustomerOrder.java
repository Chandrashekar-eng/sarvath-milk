package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true) private String orderId;
    @Column(nullable=false) private String customerId;
    private String customerName;
    private String customerEmail;
    private String phone;
    private String deliverySlot;
    private String deliveryAt;
    @Column(length=2000) private String address;
    @Lob private String itemsJson;
    private double totalAmount;
    private String status = "RECEIVED";
    private LocalDateTime createdAt = LocalDateTime.now();

    public CustomerOrder() {}
    public Long getId(){return id;}
    public String getOrderId(){return orderId;} public void setOrderId(String v){orderId=v;}
    public String getCustomerId(){return customerId;} public void setCustomerId(String v){customerId=v;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public String getCustomerEmail(){return customerEmail;} public void setCustomerEmail(String v){customerEmail=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getDeliverySlot(){return deliverySlot;} public void setDeliverySlot(String v){deliverySlot=v;}
    public String getDeliveryAt(){return deliveryAt;} public void setDeliveryAt(String v){deliveryAt=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getItemsJson(){return itemsJson;} public void setItemsJson(String v){itemsJson=v;}
    public double getTotalAmount(){return totalAmount;} public void setTotalAmount(double v){totalAmount=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
