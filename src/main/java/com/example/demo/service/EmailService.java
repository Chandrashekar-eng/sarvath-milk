package com.example.demo.service;

import com.example.demo.controller.OrderController.OrderItem;
import com.example.demo.controller.OrderController.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final RestClient restClient;

    public EmailService(@Value("${brevo.api.key}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.brevo.com")
                .defaultHeader("api-key", apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Async
    public void sendOrderConfirmation(OrderRequest order, String orderId) {
        StringBuilder itemsHtml = new StringBuilder();
        if (order.items() != null) {
            for (OrderItem item : order.items()) {
                itemsHtml.append("<li>")
                        .append(item.name())
                        .append(" (")
                        .append(item.quantity())
                        .append(") - ₹")
                        .append(item.price())
                        .append("</li>");
            }
        }

        String htmlContent = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; padding: 20px; border-radius: 8px;">
                    <h2 style="color: #2e7d32;">🥛 SARVATH DAIRY - ORDER CONFIRMED!</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>Thank you for choosing 100%% Pure & Fresh Dairy! Your order <strong>#%s</strong> has been received.</p>
                    <h3>Order Summary:</h3>
                    <ul>%s</ul>
                    <p><strong>Total Amount:</strong> ₹%.2f</p>
                    <p><strong>Delivery Slot:</strong> %s</p>
                    <p><strong>Delivery Address:</strong> %s</p>
                    <p><strong>Phone:</strong> %s</p>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="font-size: 12px; color: #777;">Fresh from our farm to your doorstep.</p>
                </div>
                """.formatted(
                order.customerName(),
                orderId,
                itemsHtml.toString(),
                order.totalAmount(),
                order.deliverySlot(),
                order.address(),
                order.phone()
        );

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", "Sarvath Dairy", "email", "sarvathdairy@gmail.com"),
                "to", List.of(Map.of("email", order.customerEmail(), "name", order.customerName())),
                //"bcc",List.of(Map.of("email","sarvathdairy@gmail.com","name","Sarvath Dairy Admin")),
                "subject", "Sarvath Dairy Order Confirmed (#" + orderId + ")",
                "htmlContent", htmlContent
        );

        try {
            restClient.post()
                    .uri("/v3/smtp/email")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            System.out.println("Brevo email sent successfully to " + order.customerEmail());
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println("Brevo email error: " + e.getMessage());
        }
     // Send separate copy to Admin
        try {
            Map<String, Object> adminBody = Map.of(
                "sender", Map.of("name", "Sarvath Dairy Orders", "email", "sarvathdairy@gmail.com"),
                "to", List.of(Map.of("email", "sarvathdairy@gmail.com", "name", "Sarvath Admin")),
                "subject", "NEW ORDER RECEIVED #" + orderId + " (" + order.customerName() + ")",
                "htmlContent", htmlContent
            );

            restClient.post()
                .uri("/v3/smtp/email")
                .body(adminBody)
                .retrieve()
                .toBodilessEntity();
        } catch (Exception e) {
            System.err.println("Admin notification email failed: " + e.getMessage());
        }
    }
}