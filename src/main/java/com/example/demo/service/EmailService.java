package com.example.demo.service;

import com.example.demo.controller.OrderController.OrderItem;
import com.example.demo.controller.OrderController.OrderRequest;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(@Value("${resend.api.key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    @Async
    public void sendOrderConfirmation(OrderRequest order, String orderId) {
        StringBuilder itemsHtml = new StringBuilder();
        if (order.items() != null) {
            for (OrderItem item : order.items()) {
                itemsHtml.append("""
                    <tr>
                      <td style="padding: 8px; border-bottom: 1px solid #e2e8f0;">%s</td>
                      <td style="padding: 8px; border-bottom: 1px solid #e2e8f0;">%s</td>
                      <td style="padding: 8px; border-bottom: 1px solid #e2e8f0;">₹%.2f</td>
                    </tr>
                    """.formatted(item.name(), item.quantity(), item.price()));
            }
        }

        String htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e2e8f0; border-radius: 12px; padding: 24px;">
              <h2 style="color: #059669; margin-top: 0;">🥛 Sarvath Dairy - Order Confirmed!</h2>
              <p>Hello <strong>%s</strong>,</p>
              <p>Thank you for choosing 100%% Pure &amp; Fresh dairy. Your order has been scheduled.</p>
              
              <div style="background: #f8fafc; padding: 14px; border-radius: 8px; margin: 16px 0;">
                <p style="margin: 4px 0;"><strong>Order ID:</strong> %s</p>
                <p style="margin: 4px 0;"><strong>Customer ID:</strong> %s</p>
                <p style="margin: 4px 0;"><strong>Delivery Slot:</strong> %s</p>
                <p style="margin: 4px 0;"><strong>Phone:</strong> %s</p>
                <p style="margin: 4px 0;"><strong>Delivery Address:</strong> %s</p>
              </div>

              <table style="width: 100%%; border-collapse: collapse; margin-top: 12px;">
                <thead>
                  <tr style="background: #ecfdf5; text-align: left;">
                    <th style="padding: 8px;">Item</th>
                    <th style="padding: 8px;">Quantity</th>
                    <th style="padding: 8px;">Price</th>
                  </tr>
                </thead>
                <tbody>
                  %s
                </tbody>
              </table>

              <h3 style="text-align: right; color: #0f172a;">Total: ₹%.2f</h3>
              <p style="font-size: 12px; color: #64748b;">Deliveries are strictly fulfilled between Morning (5:00 - 8:00 AM) and Evening (5:00 - 8:00 PM).</p>
            </div>
            """.formatted(
                order.customerName(),
                orderId,
                order.customerId(),
                order.deliverySlot(),
                order.phone(),
                order.address(),
                itemsHtml.toString(),
                order.totalAmount()
            );

        CreateEmailOptions params = CreateEmailOptions.builder()
                // Keep onboarding@resend.dev for testing with your own email; change to orders@yourdomain.com after domain verification
                .from("Sarvath Dairy <onboarding@resend.dev>")
                .to("chandrashekarnagolu@gmail.com")
                .replyTo(order.customerEmail())
                .subject("Sarvath Dairy Order Confirmed (#" + orderId + ")")
                .html(htmlContent)
                .build();

        try {
            resend.emails().send(params);
            System.out.println("Order email sent successfully for " + orderId);
        } catch (ResendException e) {
            System.err.println("Resend error: " + e.getMessage());
        }
    }
}
