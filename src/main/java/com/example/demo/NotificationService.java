package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendOrderNotifications(OrderInquiry order) {
        // -------------------------------------------------------------
        // 1. AUTOMATED EMAIL DISPATCH TO CUSTOMER
        // -------------------------------------------------------------
        try {
            if (mailSender != null) {
                SimpleMailMessage email = new SimpleMailMessage();
                email.setFrom("sarvathdairy@gmail.com");
                email.setTo(order.getEmail());
                email.setSubject("Sarvath Dairy - Order Confirmation #" + order.getId());
                email.setText("Dear " + order.getCustomerName() + ",\n\n" +
                        "Thank you for your order with Sarvath Dairy!\n\n" +
                        "Order Details:\n" +
                        "- Order ID: #" + order.getId() + "\n" +
                        "- Product: " + order.getProductName() + "\n" +
                        "- Message / Address: " + order.getMessage() + "\n\n" +
                        "We will process your order and contact you shortly.\n\n" +
                        "Warm regards,\n" +
                        "Sarvath Dairy Team");

                mailSender.send(email);
                System.out.println("[EMAIL SENT SUCCESSFULLY] Order confirmation sent to: " + order.getEmail());
            } else {
                System.out.println("[EMAIL SIMULATED] To: " + order.getEmail() + " | Order #" + order.getId());
            }
        } catch (Exception e) {
            System.err.println("[EMAIL FAILED] Could not send email to " + order.getEmail() + ": " + e.getMessage());
        }

        // -------------------------------------------------------------
        // 2. PHONE SMS NOTIFICATION (COMMENTED OUT)
        // -------------------------------------------------------------
        /*
        try {
            String customerPhone = order.getPhone();
            String smsMessage = "Hi " + order.getCustomerName() + ", your Sarvath Dairy order #" + order.getId() + 
                                " for " + order.getProductName() + " is confirmed!";
            
            // TODO: Uncomment when ready to use a real SMS gateway / Twilio
            // Message.creator(new PhoneNumber(customerPhone), new PhoneNumber("YOUR_TWILIO_NUMBER"), smsMessage).create();

            System.out.println("[SMS COMMENTED OUT] Target phone number: " + customerPhone);
        } catch (Exception e) {
            System.err.println("[SMS ERROR] " + e.getMessage());
        }
        */
    }
}