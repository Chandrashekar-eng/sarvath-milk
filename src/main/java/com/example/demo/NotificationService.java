package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // 1. Method to send order confirmation to the CUSTOMER
    @Async
    public void sendOrderNotifications(OrderInquiry order) {
        try {
            if (mailSender != null) {
                SimpleMailMessage email = new SimpleMailMessage();
                email.setFrom("chandrashekarnagolu@gmail.com");
                email.setTo(order.getEmail());
                email.setSubject("Sarvath Dairy - Order Confirmation #" + order.getId());
                
                String emailText = "Dear " + order.getCustomerName() + ",\n\n" +
                                   "Thank you for your order with Sarvath Dairy!\n\n" +
                                   "Order Details:\n" +
                                   "• Order ID: #" + order.getId() + "\n" +
                                   "• Product: " + order.getProductName() + "\n" +
                                   "• Message / Address: " + order.getMessage() + "\n\n" +
                                   "We will process your order and contact you shortly.\n\n" +
                                   "Warm regards,\n" +
                                   "Sarvath Dairy Team";
                
                email.setText(emailText);
                mailSender.send(email);
                System.out.println("EMAIL SENT SUCCESSFULLY! Order confirmation sent to: " + order.getEmail());
            } else {
                System.out.println("[EMAIL SIMULATED] To: " + order.getEmail() + " | Order #" + order.getId());
            }
        } catch (Exception e) {
            System.err.println("[EMAIL FAILED] Could not send email to " + order.getEmail() + " -> " + e.getMessage());
        }
    }

    // 2. Method to send a dedicated alert to the ADMIN TEAM
    @Async
    public void sendAdminAlertNotification(OrderInquiry order) {
        try {
            if (mailSender != null) {
                SimpleMailMessage adminEmail = new SimpleMailMessage();
                adminEmail.setFrom("chandrashekarnagolu@gmail.com");
                adminEmail.setTo("chandrashekarnagolu@gmail.com"); // Directs alert to the admin inbox
                adminEmail.setSubject("🚨 NEW DAIRY INQUIRY RECEIVED - Order #" + order.getId());
                
                String emailText = "Hello Admin Team,\n\n" +
                                   "A new customer order inquiry has been submitted.\n\n" +
                                   "📋 INQUIRY DETAILS:\n" +
                                   "---------------------------\n" +
                                   "• Order ID: " + order.getId() + "\n" +
                                   "• Customer Name: " + order.getCustomerName() + "\n" +
                                   "• Phone Number: " + order.getPhone() + "\n" +
                                   "• Email Address: " + order.getEmail() + "\n" +
                                   "• Product Choice: " + order.getProductName() + "\n" +
                                   "• Special Instructions/Message: " + order.getMessage() + "\n\n" +
                                   "Please review the database dashboard and process the order schedule accordingly.\n\n" +
                                   "System automated message from Sarvath Dairy Backend.";
                
                adminEmail.setText(emailText);
                mailSender.send(adminEmail);
                System.out.println("LOG: Admin alert email sent successfully for Order #" + order.getId());
            } else {
                System.out.println("[EMAIL SIMULATED] To Admin: New Inquiry Received for Order #" + order.getId());
            }
        } catch (Exception e) {
            System.err.println("LOG ERROR: Failed to send admin alert email -> " + e.getMessage());
        }
    }
}